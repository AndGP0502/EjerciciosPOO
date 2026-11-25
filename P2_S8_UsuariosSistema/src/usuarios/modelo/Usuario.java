package usuarios.modelo;

public class Usuario {

    private String username;
    private String correo;

    public Usuario(String username, String correo) {
        this.username = username;
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }

    public String obtenerPermisos() {
        return "Permisos basicos";
    }

    public String mostrarInformacion() {
        return "Usuario: " + username +
                " | Correo: " + correo +
                " | Permisos: " + obtenerPermisos();
    }
}
