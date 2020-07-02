package com.senla.training.yeutukhovich.bookstore.util.constant;

import com.senla.training.yeutukhovich.bookstore.util.configuration.ConfigurationData;

public enum PathConstant {

    SERIALIZED_DATA_PATH(ConfigurationData.getValue(ConfigurationData.SERIALIZED_DATA_PATH)),
    DIRECTORY_PATH(ConfigurationData.getValue(ConfigurationData.CVS_DIRECTORY_PATH)),
    CVS_FORMAT_TYPE(ConfigurationData.getValue(ConfigurationData.CVS_FORMAT_TYPE));

    private String pathConstant;

    PathConstant(String pathConstant) {
        this.pathConstant = pathConstant;
    }

    public String getPathConstant() {
        return pathConstant;
    }
}
