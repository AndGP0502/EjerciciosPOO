package usuarios.servicio;

import usuarios.modelo.Usuario;
import java.util.ArrayList;

public class SistemaUsuarios {

    private ArrayList<Usuario> usuarios;

    public SistemaUsuarios() {
        usuarios = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null &&
                buscarUsuario(usuario.getUsername()) == null) {
            usuarios.add(usuario);
        }
    }

    public Usuario buscarUsuario(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    public boolean eliminarUsuario(String username) {
        Usuario usuario = buscarUsuario(username);

        if (usuario != null) {
            usuarios.remove(usuario);
            return true;
        }

        return false;
    }

    public String mostrarUsuarios() {
        StringBuilder resultado = new StringBuilder();

        for (Usuario usuario : usuarios) {
            resultado.append(usuario.mostrarInformacion())
                    .append("\n");
        }

        return resultado.toString();
    }
}

