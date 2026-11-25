
package usuarios;

import usuarios.modelo.*;
import usuarios.servicio.SistemaUsuarios;

public class Main {

    public static void main(String[] args) {

        // Crear los usuarios
        Usuario u1 = new Administrador(
                "admin1", "admin@demo.com");

        Usuario u2 = new Desarrollador(
                "dev1", "dev@demo.com");

        Usuario u3 = new Cliente(
                "cliente1", "cliente@demo.com");

        // Crear el sistema de usuarios
        SistemaUsuarios sistema = new SistemaUsuarios();

        // Agregar usuarios al sistema
        sistema.agregarUsuario(u1);
        sistema.agregarUsuario(u2);
        sistema.agregarUsuario(u3);

        // Mostrar todos los usuarios
        System.out.println("USUARIOS DEL SISTEMA");
        System.out.println(sistema.mostrarUsuarios());

        // Mostrar los permisos
        System.out.println("PERMISOS");
        System.out.println(u1.obtenerPermisos());
        System.out.println(u2.obtenerPermisos());
        System.out.println(u3.obtenerPermisos());

        // Buscar un usuario
        System.out.println("\nBUSCAR USUARIO");

        Usuario encontrado = sistema.buscarUsuario("dev1");

        if (encontrado != null) {
            System.out.println(encontrado.mostrarInformacion());
        } else {
            System.out.println("Usuario no encontrado");
        }

        // Eliminar un usuario
        System.out.println("\nELIMINAR USUARIO");

        boolean eliminado = sistema.eliminarUsuario("cliente1");

        System.out.println("Usuario eliminado: " + eliminado);

        // Mostrar usuarios restantes
        System.out.println("\nUSUARIOS RESTANTES");
        System.out.println(sistema.mostrarUsuarios());
    }
}
