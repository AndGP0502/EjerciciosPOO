package usuarios.modelo;

public class Administrador extends Usuario {

    public Administrador(String username, String correo) {
        super(username, correo);
    }

    @Override
    public String obtenerPermisos() {
        return "Gestionar usuarios y configurar el sistema";
    }

    @Override
    public String mostrarInformacion() {
        return "Administrador | " +
                super.mostrarInformacion();
    }
}
