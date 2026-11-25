package usuarios.modelo;

public class Desarrollador extends Usuario {

    public Desarrollador(String username, String correo) {
        super(username, correo);
    }

    @Override
    public String obtenerPermisos() {
        return "Desarrollar, modificar y mantener software";
    }

    @Override
    public String mostrarInformacion() {
        return "Desarrollador | " +
                super.mostrarInformacion();
    }
}
