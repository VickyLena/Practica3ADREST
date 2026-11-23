/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author alumne
 */

import bdd.ConexionBDD.Imagen;
import jakarta.json.*;
import java.util.List;

public class JsonUtil {

    private static String seguro(String valor) {
        return valor == null ? "" : valor;
    }

    public static JsonObject imagenToJson(Imagen imagen) {

        return Json.createObjectBuilder()
                .add("id", imagen.getId())
                .add("titulo", seguro(imagen.getTitulo()))
                .add("descripcion", seguro(imagen.getDescripcion()))
                .add("keywords", seguro(imagen.getKeywords()))
                .add("autor", seguro(imagen.getAutor()))
                .add("fechaCreacion", seguro(imagen.getFechaCreacion()))
                .add("fechaAlta", seguro(imagen.getFechaAlta()))
                .add("fichero", seguro(imagen.getFichero()))
                .build();
    }

    // lista de imágenes a JSON
    public static JsonArray imagenesToJson(List<Imagen> imagenes) {

        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Imagen imagen : imagenes) {
            array.add(imagenToJson(imagen));
        }

        return array.build();
    }

    public static JsonObject mensaje(String texto) {

        return Json.createObjectBuilder()
                .add("mensaje", texto)
                .build();
    }
}
