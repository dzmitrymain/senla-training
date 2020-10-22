package com.senla.training.yeutukhovich.bookstore.security.util;

public enum SecurityConstant {

    // для хэдера "Authorization" есть константа в org.springframework.http.HttpHeaders
    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer ");

    // можно делать final
    private String constant;

    SecurityConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
