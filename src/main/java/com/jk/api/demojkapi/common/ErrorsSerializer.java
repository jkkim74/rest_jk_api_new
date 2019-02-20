package com.jk.api.demojkapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;
// objectMapper에 등록을 해야 함.. JsonComponent를 넣어주면 objectMapper에 등록한다.
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 에러가 여러개이므로 배열로 담기 위해서 처리
        gen.writeStartArray();
        // Errors에는 field Error와 global Error가 있다. reject ==> global Erro, rejectValue ==> Field Error
        // field Error
        // jsonObject를 만들어줌...
        errors.getFieldErrors().stream().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("field",e.getField());
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null){
                    gen.writeStringField("rejectedValue",rejectedValue.toString());
                }
                gen.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        // global field, value부분은 없음..
        // global을 위한 json Object를 만들어줌..
        errors.getGlobalErrors().forEach(e ->{
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("code",e.getCode());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });

        gen.writeEndArray();
    }
}
