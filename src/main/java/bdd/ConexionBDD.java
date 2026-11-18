/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author alumne
 */

public class ConexionBDD {

    private static final String URL = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";

    // Metodo conexión gral
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Metodo login
    public static boolean validarUsuario(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ? AND password = ?";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                return result.next(); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Metodo para suba de Imagenes
    public static boolean registrarImagen(String titulo, String descripcion, String keywords,
            String autor, String fechaCreacion, String fechaAlta, String nombreFichero) {

        String sql = "INSERT INTO image (TITLE, DESCRIPTION, KEYWORDS, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, titulo);
            statement.setString(2, descripcion);
            statement.setString(3, keywords);
            statement.setString(4, autor);
            statement.setString(5, fechaCreacion);
            statement.setString(6, fechaAlta);
            statement.setString(7, nombreFichero);

            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Metodo de busqueda de imàgenes + objeto Imagen
    public static class Imagen {
        private int id;
        private String titulo;
        private String descripcion;
        private String keywords;
        private String autor;
        private String fechaCreacion;
        private String fechaAlta;
        private String fichero;

        public Imagen(int id, String titulo, String descripcion, String keywords, String autor, String fechaCreacion, String fechaAlta, String fichero) {
            this.id = id;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.keywords = keywords;
            this.autor = autor;
            this.fechaCreacion = fechaCreacion;
            this.fechaAlta = fechaAlta;
            this.fichero = fichero;
        }
        
     public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public String getKeywords() { return keywords; }
        public String getAutor() { return autor; }
        public String getFechaCreacion() { return fechaCreacion; }
        public String getFechaAlta() { return fechaAlta; }
        public String getFichero() { return fichero; }
    }
    
    public static List<Imagen> buscarImagenes(String titulo, String autor, String keywords, String fechaCreacion) {
        List<Imagen> lista = new ArrayList<>();
        
        // x si el param viene vacío
        titulo = (titulo == null) ? "" : titulo.trim();
        autor = (autor == null) ? "" : autor.trim();
        keywords = (keywords == null) ? "" : keywords.trim();
        fechaCreacion = (fechaCreacion == null) ? "" : fechaCreacion.trim();
        
        String sql = "SELECT * FROM image WHERE LOWER(TITLE) LIKE LOWER(?) "
                   + "AND LOWER(CREATOR) LIKE LOWER(?) "
                   + "AND LOWER(KEYWORDS) LIKE LOWER(?)"
                   + "AND CAPTURE_DATE LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + titulo + "%"); // % se usa para que no importe lo que esta antes o despues
            statement.setString(2, "%" + autor + "%");
            statement.setString(3, "%" + keywords + "%");
            statement.setString(4, "%" + fechaCreacion + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Imagen(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("KEYWORDS"),
                        rs.getString("CREATOR"),
                        rs.getString("CAPTURE_DATE"),
                        rs.getString("STORAGE_DATE"),
                        rs.getString("FILENAME")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
        // Obtener una imagen por su ID (para modificar/eliminar)
    public static Imagen obtenerImagenPorId(int id) {
        String sql = "SELECT * FROM image WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Imagen(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("KEYWORDS"),
                        rs.getString("CREATOR"),
                        rs.getString("CAPTURE_DATE"),
                        rs.getString("STORAGE_DATE"),
                        rs.getString("FILENAME")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Modificar una imagen existente (solo si el autor coincide)
    public static boolean modificarImagen(int id, String titulo, String descripcion, String keywords,
            String fechaCreacion, String fichero, String autor) {

        String sql = "UPDATE image SET TITLE = ?, DESCRIPTION = ?, KEYWORDS = ?, "
                   + "CAPTURE_DATE = ?, FILENAME = ? WHERE ID = ? AND CREATOR = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, titulo);
            statement.setString(2, descripcion);
            statement.setString(3, keywords);
            statement.setString(4, fechaCreacion);
            statement.setString(5, fichero);
            statement.setInt(6, id);
            statement.setString(7, autor);

            int filasActualizadas = statement.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // Eliminar una imagen (solo si el autor coincide)
    public static boolean eliminarImagen(int id, String autor) {
        String sql = "DELETE FROM image WHERE ID = ? AND CREATOR = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, autor);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
        // Comprobar si un usuario ya existe (sin distinguir mayúsculas)
    public static boolean existeUsuario(String usuario) {
        String sql = "SELECT 1 FROM usuarios WHERE LOWER(id_usuario) = LOWER(?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // ante la duda, no dejamos registrar
        }
    }

    // Registrar un usuario nuevo
    public static boolean registrarUsuario(String usuario, String password) {
        String sql = "INSERT INTO usuarios (id_usuario, password) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setString(2, password);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
