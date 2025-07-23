package com.jobhunter.jobhunter_be.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jobhunter.jobhunter_be.dto.response.AuthResponse;

import java.io.IOException;

public class AuthSerializer extends StdSerializer<AuthResponse<?>> {

    public AuthSerializer() {
        this(null);
    }

    public AuthSerializer(Class<AuthResponse<?>> t) {
        super(t);
    }

    @Override
    public void serialize(AuthResponse<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Object data = value.getData();
        if (data != null) {
            String name = resolveFieldName(data);
            gen.writeObjectField(name, data);
        }

        if (value.getToken() != null) {
            gen.writeStringField("token", value.getToken());
        }

        if (value.getRefreshToken() != null) {
            gen.writeStringField("refreshToken", value.getRefreshToken());
        }

        gen.writeEndObject();
    }

    private String resolveFieldName(Object data) {
        String className = data.getClass().getSimpleName();
        return Character.toLowerCase(className.charAt(0)) + className.substring(1).replace("Response", "");
    }
}
