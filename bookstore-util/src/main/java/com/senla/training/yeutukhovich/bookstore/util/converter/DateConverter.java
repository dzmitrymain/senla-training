package com.senla.training.yeutukhovich.bookstore.util.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateConverter {

    public static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
            Locale.ENGLISH);

    private DateConverter() {

    }

    public static Date parseDate(String dateString, SimpleDateFormat dateFormat) throws ParseException {
        if (dateString != null && dateFormat != null) {
            return dateFormat.parse(dateString);
        }
        return null;
    }

    public static String formatDate(Date date, SimpleDateFormat dateFormat) {
        if (date != null && dateFormat != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    public static class Serialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (date == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeString(STANDARD_DATE_FORMAT.format(date));
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            try {
                return STANDARD_DATE_FORMAT.parse(jsonParser.getText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
