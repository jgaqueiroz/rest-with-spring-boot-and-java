package br.com.effecta.rest_with_spring_boot_and_java.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GenderSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String gender, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedGender = "Male".equals(gender) ? "M" : "F";
        gen.writeString(formattedGender);
    }

}
