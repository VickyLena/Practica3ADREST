/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rest.resources;

/**
 *
 * @author alumne
 */

import bdd.ConexionBDD;
import bdd.ConexionBDD.Imagen;
import java.util.List;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import util.JsonUtil;
import util.Ficheros;

@Path("")
public class ImagenResource {

    @Path("searchID/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByID(@PathParam("id") int id) {

        Imagen imagen = ConexionBDD.obtenerImagenPorId(id);

        if (imagen == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(JsonUtil.mensaje("Imagen no encontrada").toString())
                    .build();
        }

        return Response.ok(
                JsonUtil.imagenToJson(imagen).toString()
        ).build();
    }

    @Path("searchTitle/{title}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle(
            @PathParam("title") String title) {

        List<Imagen> imagenes =
                ConexionBDD.buscarImagenes(title, "", "", "");

        return Response.ok(
                JsonUtil.imagenesToJson(imagenes).toString()
        ).build();
    }

    @Path("searchCreator/{creator}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCreator(
            @PathParam("creator") String creator) {

        List<Imagen> imagenes =
                ConexionBDD.buscarImagenes("", creator, "", "");

        return Response.ok(
                JsonUtil.imagenesToJson(imagenes).toString()
        ).build();
    }

    @Path("searchKeywords/{keywords}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByKeywords(
            @PathParam("keywords") String keywords) {

        List<Imagen> imagenes =
                ConexionBDD.buscarImagenes("", "", keywords, "");

        return Response.ok(
                JsonUtil.imagenesToJson(imagenes).toString()
        ).build();
    }

    @Path("searchCreationDate/{date}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCreationDate(
            @PathParam("date") String date) {

        List<Imagen> imagenes =
                ConexionBDD.buscarImagenes("", "", "", date);

        return Response.ok(
                JsonUtil.imagenesToJson(imagenes).toString()
        ).build();
    }

    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(
            @QueryParam("title") String title,
            @QueryParam("creator") String creator,
            @QueryParam("keywords") String keywords,
            @QueryParam("date") String date) {

        List<Imagen> imagenes =
                ConexionBDD.buscarImagenes(
                        title, creator, keywords, date
                );

        return Response.ok(
                JsonUtil.imagenesToJson(imagenes).toString()
        ).build();
    }

    /**
     * POST method to delete an existing image
     * @param id
     * @param creator
     * @return
     */
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteImage(@FormParam("id") String id,
                                @FormParam("creator") String creator) {

        // 1. Comprobar parámetros
        if (id == null || id.isBlank() || creator == null || creator.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtil.mensaje("Faltan los parámetros id y creator").toString())
                    .build();
        }

        int idImagen;
        try {
            idImagen = Integer.parseInt(id.trim());
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtil.mensaje("El id debe ser un número").toString())
                    .build();
        }

        // 2. Comprobar que la imagen existe
        Imagen imagen = ConexionBDD.obtenerImagenPorId(idImagen);
        if (imagen == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(JsonUtil.mensaje("Imagen no encontrada").toString())
                    .build();
        }

        // 3. Comprobar que la imagen es del usuario que la quiere borrar
        if (!imagen.getAutor().equalsIgnoreCase(creator.trim())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(JsonUtil.mensaje("Solo el propietario puede eliminar la imagen").toString())
                    .build();
        }

        // 4. Borrar de la BD y, si ha ido bien, el fichero del disco
        boolean eliminada = ConexionBDD.eliminarImagen(idImagen, imagen.getAutor());
        if (!eliminada) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(JsonUtil.mensaje("No se ha podido eliminar la imagen").toString())
                    .build();
        }
        Ficheros.borrar(imagen.getFichero());

        return Response.ok(
                JsonUtil.mensaje("Imagen eliminada correctamente").toString()
        ).build();
    }
}