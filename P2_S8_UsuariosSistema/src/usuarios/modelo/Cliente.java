package usuarios.modelo;

public class Cliente extends Usuario {

    public Cliente(String username, String correo) {
        super(username, correo);
    }

    @Override
    public String obtenerPermisos() {
        return "Consultar productos y realizar compras";
    }

    @Override
    public String mostrarInformacion() {
        return "Cliente | " +
                super.mostrarInformacion();
    }
}
