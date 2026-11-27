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
import util.JsonUtil;

@Path("")
public class UsuarioResource {

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @FormParam("username") String usuario,
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

    /**
     * POST method to create a new user (operación extra)
     * @param username
     * @param password
     * @return 201 creado, 400 datos incorrectos, 409 ya existe, 500 error
     */
    @Path("createUser")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        // 1. Comprobar parámetros
        if (username == null || username.isBlank()
                || password == null || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtil.mensaje("Faltan el usuario o la contraseña").toString())
                    .build();
        }

        username = username.trim();

        if (username.length() > 30) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(JsonUtil.mensaje("El usuario es demasiado largo (máximo 30 caracteres)").toString())
                    .build();
        }

        // 2. Comprobar que no existe ya
        if (ConexionBDD.existeUsuario(username)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(JsonUtil.mensaje("Ese nombre de usuario ya existe").toString())
                    .build();
        }

        // 3. Crear el usuario en la BD
        if (!ConexionBDD.registrarUsuario(username, password)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(JsonUtil.mensaje("No se ha podido crear el usuario").toString())
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(JsonUtil.mensaje("Usuario creado correctamente").toString())
                .build();
    }
}