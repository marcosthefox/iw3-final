package org.mugiwaras.backend.auth;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.mugiwaras.backend.util.JsonUtiles;

import java.io.IOException;

public class RoleJsonSerializer extends StdSerializer<Role> {
 public RoleJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
  @Override
    public void serialize(Role role, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeString(role.getName());

}
}