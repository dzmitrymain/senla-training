package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum ApplicationConstant {

    SRID_WGS84("4326");

    private String constant;

    ApplicationConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
