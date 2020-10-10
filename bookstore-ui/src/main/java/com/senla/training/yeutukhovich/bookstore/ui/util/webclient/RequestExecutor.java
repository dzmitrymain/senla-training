package com.senla.training.yeutukhovich.bookstore.ui.util.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.dto.ErrorDto;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestExecutor {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String DELIMITER = "\n";
    private static final int OK_STATUS_CODE = 200;

    public static <T> void executeRequestForEntity(String url, HttpMethod method, HttpEntity<?> request,
                                                   Class<T> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, method, request, String.class);
        UiConsolePrinter.printMessage(readObjectBody(responseEntity.getBody(), responseEntity.getStatusCode(), clazz));
    }

    public static <T> void executeRequestForEntityArray(String url, HttpMethod method, HttpEntity<?> request,
                                                        Class<T[]> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, method, request, String.class);
        UiConsolePrinter.printMessage(readArrayBody(responseEntity.getBody(), responseEntity.getStatusCode(), clazz));
    }

    public static HttpEntity<MultiValueMap<String, String>> createUrlencodedRequest(List<String> values,
                                                                                    String... keys) {
        if (values.size() != keys.length || keys.length == 0) {
            throw new RuntimeException("Wrong keys number.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        for (int i = 0; i < keys.length; i++) {
            body.add(keys[i], values.get(i));
        }
        return new HttpEntity<>(body, headers);
    }

    private static <T> String readArrayBody(String json, HttpStatus status, Class<T[]> clazz) {
        if (status.value() != OK_STATUS_CODE) {
            return readErrorDtoMessage(json);
        }
        try {
            return Arrays.stream(MAPPER.readValue(json, clazz))
                    .map(Objects::toString)
                    .collect(Collectors.joining(DELIMITER));
        } catch (JsonProcessingException e) {
            return readErrorDtoMessage(json);
        }
    }

    private static <T> String readObjectBody(String json, HttpStatus status, Class<T> clazz) {
        if (status.value() != OK_STATUS_CODE) {
            return readErrorDtoMessage(json);
        }
        try {
            return MAPPER.readValue(json, clazz).toString();
        } catch (JsonProcessingException e) {
            return readErrorDtoMessage(json);
        }
    }

    private static String readErrorDtoMessage(String json) {
        try {
            return MAPPER.readValue((json), ErrorDto.class).getMessage();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
