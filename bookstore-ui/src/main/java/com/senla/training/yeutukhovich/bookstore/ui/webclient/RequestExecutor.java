package com.senla.training.yeutukhovich.bookstore.ui.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.dto.ErrorDto;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RequestExecutor {

    private static final String DELIMITER = "\n";
    private static final int OK_STATUS_CODE = 200;

    @Getter
    private String token = "";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public <T> void executeRequestForEntity(String url, HttpMethod method, HttpEntity<?> request,
                                            Class<T> clazz) {
        ResponseEntity<String> responseEntity = executeRequest(url, method, request);
        UiConsolePrinter.printMessage(readObjectBody(responseEntity.getBody(), responseEntity.getStatusCode(), clazz));
    }

    public <T> void executeRequestForEntityArray(String url, HttpMethod method, HttpEntity<?> request,
                                                 Class<T[]> clazz) {
        ResponseEntity<String> responseEntity = executeRequest(url, method, request);
        UiConsolePrinter.printMessage(readArrayBody(responseEntity.getBody(), responseEntity.getStatusCode(), clazz));
    }

    public HttpEntity<MultiValueMap<String, String>> createUrlencodedRequest(List<String> values,
                                                                             String... keys) {
        if (values.size() != keys.length || keys.length == 0) {
            throw new RuntimeException("Wrong keys number.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, token);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        for (int i = 0; i < keys.length; i++) {
            body.add(keys[i], values.get(i));
        }
        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<String> executeRequest(String url, HttpMethod method, HttpEntity<?> request) {
        if (request == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, token);
            request = new HttpEntity<>(null, headers);
        }
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, method, request, String.class);
            Optional.ofNullable(responseEntity.getHeaders().get(HttpHeaders.AUTHORIZATION))
                    .ifPresent(list -> token = list.get(0));
            return responseEntity;
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    private <T> String readArrayBody(String json, HttpStatus status, Class<T[]> clazz) {
        if (status.value() != OK_STATUS_CODE) {
            return readErrorDtoMessage(json);
        }
        try {
            return Arrays.stream(objectMapper.readValue(json, clazz))
                    .map(Objects::toString)
                    .collect(Collectors.joining(DELIMITER));
        } catch (JsonProcessingException e) {
            return readErrorDtoMessage(json);
        }
    }

    private <T> String readObjectBody(String json, HttpStatus status, Class<T> clazz) {
        if (status.value() != OK_STATUS_CODE) {
            return readErrorDtoMessage(json);
        }
        try {
            return objectMapper.readValue(json, clazz).toString();
        } catch (JsonProcessingException e) {
            return readErrorDtoMessage(json);
        }
    }

    private String readErrorDtoMessage(String json) {
        try {
            return objectMapper.readValue((json), ErrorDto.class).getMessage();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
