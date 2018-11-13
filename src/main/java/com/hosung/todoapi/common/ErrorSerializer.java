package com.hosung.todoapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {

        jsonGenerator.writeStartArray();

        errors.getFieldErrors().forEach(e -> {
            try {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("field", e.getField());
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());

                Object rejectValue = e.getRejectedValue();

                if(rejectValue != null) {
                    jsonGenerator.writeStringField("rejectedValue", rejectValue.toString());
                } else {
                    jsonGenerator.writeStringField("rejectValue", "");
                }

                jsonGenerator.writeEndObject();
            } catch(IOException ie) {
                ie.printStackTrace();
            }
        });

        jsonGenerator.writeEndArray();
    }
}

