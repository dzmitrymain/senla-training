package com.senla.training.yeutukhovich.bookstore.ui.util.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RestTemplateHelper {

    private static final String MESSAGE = "message";
    private static final String DELIMITER = "\n";
    private static final int OK_STATUS_CODE = 200;

    public static <T> void postForEntity(String url, HttpEntity<?> request, Class<T> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(
                url, request, clazz);
        printObjectResponseEntity(responseEntity);
    }

    public static <T> void postForEntityArray(String url, HttpEntity<?> request, Class<T[]> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                url, request, String.class);
        printArrayBody(responseEntity.getBody(), clazz);
    }

    public static <T> void getForEntity(String url, Class<T> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, clazz);
        printObjectResponseEntity(responseEntity);
    }

    public static <T> void getForEntityArray(String url, Class<T[]> clazz) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        printArrayBody(responseEntity.getBody(), clazz);
    }

    public static HttpEntity<MultiValueMap<String, String>> initUrlencodedRequest(List<String> values, String... keys) {
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

    private static <T> void printArrayBody(String json, Class<T[]> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T[] array = mapper.readValue(Objects.requireNonNull(json), clazz);
            UiConsolePrinter.printMessage(Arrays.stream(array)
                    .map(Objects::toString)
                    .collect(Collectors.joining(DELIMITER)));
        } catch (JsonProcessingException e) {
            try {
                Map<String, String> map = mapper.readValue(Objects.requireNonNull(json), new TypeReference<>() {
                });
                UiConsolePrinter.printMessage(map.get(MESSAGE));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static <T> void printObjectResponseEntity(ResponseEntity<T> responseEntity) {
        if (responseEntity.getStatusCodeValue() == OK_STATUS_CODE && !responseEntity.getHeaders().containsKey(MESSAGE)) {
            UiConsolePrinter.printMessage(Objects.requireNonNull(responseEntity.getBody()).toString());
        } else {
            UiConsolePrinter.printMessage(
                    Objects.requireNonNull(responseEntity.getHeaders().get(MESSAGE)).toString());
        }
    }
}
