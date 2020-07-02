package com.senla.training.yeutukhovich.bookstore.util.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationData {

    public static final String STALE_MONTH_NUMBER = "stale_month_number";
    public static final String REQUEST_AUTO_CLOSE = "request_auto_close";

    public static final String SERIALIZED_DATA_PATH = "serialized_data_path";
    public static final String CVS_DIRECTORY_PATH = "cvs_directory_path";
    public static final String CVS_FORMAT_TYPE = "cvs_format_type";

    private static final String CONFIGURATION_PROPERTIES = "configuration.properties";

    private static final Properties PROPERTIES;
    private static final Properties DEFAULT_PROPERTIES;

    static {
        DEFAULT_PROPERTIES = new Properties();
        DEFAULT_PROPERTIES.setProperty(STALE_MONTH_NUMBER, "6");
        DEFAULT_PROPERTIES.setProperty(REQUEST_AUTO_CLOSE, "true");

        PROPERTIES = new Properties(DEFAULT_PROPERTIES);
        loadStream();
    }

    private ConfigurationData() {

    }

    public static String getValue(String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }
        return PROPERTIES.getProperty(key);

    }

    private static void loadStream() {
        try (InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(ConfigurationData.CONFIGURATION_PROPERTIES)) {
            if (stream != null) {
                PROPERTIES.load(stream);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
