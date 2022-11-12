package org.mugiwaras.backend.model.deserealizer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.util.JsonUtiles;

import java.io.IOException;

public class CheckOutDesealizer extends StdDeserializer<Orden> {

    public CheckOutDesealizer(Class<?> src) {
        super(src);
    }

    @Override
    public Orden deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        Orden r = new Orden();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String tara = JsonUtiles.getString(node, "pesaje,pesaje_final".split(","),"0");
        r.setPesajeFinal(Long.parseLong(tara));
        return r;
    }
}
