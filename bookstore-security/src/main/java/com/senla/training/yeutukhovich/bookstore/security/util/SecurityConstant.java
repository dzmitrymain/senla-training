package com.senla.training.yeutukhovich.bookstore.security.util;

public enum SecurityConstant {

    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer ");

    private String constant;

    SecurityConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
