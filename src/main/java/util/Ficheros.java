/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author alumne
 */

import java.io.File;

/**
 * Gestión de los ficheros de imagen guardados en el servidor.
 */
public class Ficheros {

    public static final String DIRECTORIO_IMAGENES = "/home/alumne/uploads";

    private Ficheros() { }

    /** Borra un fichero del directorio de imágenes. Devuelve true si se ha borrado. */
    public static boolean borrar(String nombreFichero) {
        if (nombreFichero == null || nombreFichero.isBlank()) {
            return false;
        }
        File fichero = new File(DIRECTORIO_IMAGENES, nombreFichero);
        return fichero.exists() && fichero.delete();
    }
}