package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Gestión de los ficheros de imagen guardados en el servidor.
 */
public class Ficheros {

    public static final String DIRECTORIO_IMAGENES = "/home/alumne/uploads";

    private Ficheros() { }

    /**
     * Devuelve el fichero de una imagen, o null si no existe.
     * getName() evita rutas tipo "../../" (solo se permite leer dentro del directorio).
     */
    public static File obtener(String nombreFichero) {
        if (nombreFichero == null || nombreFichero.isBlank()) {
            return null;
        }
        File fichero = new File(DIRECTORIO_IMAGENES, new File(nombreFichero).getName());
        return fichero.isFile() ? fichero : null;
    }

    /** Tipo MIME del fichero (image/jpeg, image/png...). */
    public static String tipoMime(File fichero) {
        try {
            String tipo = Files.probeContentType(fichero.toPath());
            if (tipo != null) {
                return tipo;
            }
        } catch (IOException e) {
            // si no se puede detectar, se usa el genérico
        }
        return "application/octet-stream";
    }

    /** Borra un fichero del directorio de imágenes. Devuelve true si se ha borrado. */
    public static boolean borrar(String nombreFichero) {
        File fichero = obtener(nombreFichero);
        return fichero != null && fichero.delete();
    }
}