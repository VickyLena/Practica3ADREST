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
}
