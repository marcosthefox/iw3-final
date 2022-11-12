package org.mugiwaras.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.mugiwaras.backend.model.*;
import org.mugiwaras.backend.util.JsonUtiles;

import java.io.IOException;

public class OrdenPassJsonSerializer extends StdSerializer<Orden> {
    public OrdenPassJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Orden value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("password", value.getPassword());

        gen.writeEndObject();
    }
}
