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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("")
public class UsuarioResource {

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @FormParam("usuario") String usuario,
            @FormParam("password") String password) {

        boolean valido =
                ConexionBDD.validarUsuario(usuario, password);

        if (valido) {
            return Response.ok(
                    "{\"success\":true}"
            ).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"success\":false}")
                .build();
    }
}