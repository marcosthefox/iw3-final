package org.mugiwaras.backend.model.deserealizer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.mugiwaras.backend.model.*;
import org.mugiwaras.backend.util.JsonUtiles;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenDeserealizer extends StdDeserializer<Orden> {

    public OrdenDeserealizer(Class<?> src) {
        super(src);
    }


    @Override
    public Orden deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.readValueAsTree();
        JsonNode camionNode = node.get("camion");
        JsonNode cisternaNode = camionNode.get("datosCisterna");
        JsonNode choferNode = node.get("chofer");
        JsonNode clienteNode = node.get("cliente");
        JsonNode productoNode = node.get("producto");

        String numeroOrden = JsonUtiles.getString(node, "numeroOrden,numero_orden,numero-orden".split(","), "0");
        String patente = JsonUtiles.getString(camionNode, "patente".split(","), "0");
        String descripcion = JsonUtiles.getString(camionNode, "descripcion".split(","), "0");

        List<Cisternado> cisternadoList = new ArrayList<>();
        for (JsonNode nodo : cisternaNode) {
            Cisternado aux = new Cisternado();
            String tamanio = JsonUtiles.getString(nodo, "tamanio".split(","), "0");
            aux.setTamanio(Long.parseLong(tamanio));
            cisternadoList.add(aux);
        }

        String totalCisterna = JsonUtiles.getString(camionNode, "totalCisterna,total_cisterna,total-cisterna".split(","), "0");
        String dni = JsonUtiles.getString(choferNode, "dni".split(","), "0");
        String nombre = JsonUtiles.getString(choferNode, "nombre".split(","), "0");
        String apellido = JsonUtiles.getString(choferNode, "apellido".split(","), "0");
        String razonSocial = JsonUtiles.getString(clienteNode, "razonSocial,razon_social,razon-social".split(","), "0");
        String contacto = JsonUtiles.getString(clienteNode, "contacto".split(","), "0");
        String id = JsonUtiles.getString(productoNode, "id".split(","), "0");
        String nombreProducto = JsonUtiles.getString(productoNode, "nombre".split(","), "0");
        String descripcionProducto = JsonUtiles.getString(productoNode, "descripcion".split(","), "0");
        String preset = JsonUtiles.getString(node, "preset".split(","), "0");
        String fechaTurnoCarga = JsonUtiles.getString(node, "fechaTurnoCarga,fecha_turno_carga,fecha-turno-carga".split(","), "0");

        Orden r = new Orden();
        r.setNumeroOrden(Long.parseLong(numeroOrden));
        r.setCamion(Camion.builder().patente(patente).descripcion(descripcion).datosCisterna(cisternadoList).totalCisterna(Long.parseLong(totalCisterna)).build());
        r.setChofer(Chofer.builder().dni(Long.parseLong(dni)).nombre(nombre).apellido(apellido).build());
        r.setCliente(Cliente.builder().razonSocial(Long.parseLong(razonSocial)).contacto(Long.parseLong(contacto)).build());
        r.setProducto(Producto.builder().id(Long.parseLong(id)).nombre(nombreProducto).descripcion(descripcionProducto).build());
        r.setPreset(Float.parseFloat(preset));
        r.setFechaTurnoCarga(OffsetDateTime.parse(fechaTurnoCarga));
        return r;
    }
}
