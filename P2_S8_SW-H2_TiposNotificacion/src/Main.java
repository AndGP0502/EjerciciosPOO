import modelo.Notificacion;
import modelo.NotificacionEmail;
import modelo.NotificacionSMS;
import modelo.NotificacionPush;
import modelo.NotificacionSlack;
import modelo.GestorNotificaciones;

public class Main {

    public static void main(String[] args) {

        // Crear notificaciones independientes
        Notificacion n1 = new NotificacionEmail(
                "pepepancho@gmail.com",
                "Bienvenido al sistema",
                "Registro exitoso");

        Notificacion n2 = new NotificacionSMS(
                "0991234567",
                "Tu codigo de verificacion es 1234",
                "Claro");

        Notificacion n3 = new NotificacionPush(
                "usuario01",
                "Tienes una nueva actualizacion",
                "AppStore");

        Notificacion n4 = new NotificacionSlack(
                "equipo_desarrollo",
                "Se ha completado el sprint",
                "#desarrollo");

        // Crear gestor
        GestorNotificaciones gestor = new GestorNotificaciones();

        // Agregar notificaciones
        gestor.agregarNotificacion(n1);
        gestor.agregarNotificacion(n2);
        gestor.agregarNotificacion(n3);
        gestor.agregarNotificacion(n4);

        // Mostrar los mensajes individuales
        System.out.println("=== MENSAJES INDIVIDUALES ===");

        System.out.println(n1.generarMensaje());
        System.out.println();

        System.out.println(n2.generarMensaje());
        System.out.println();

        System.out.println(n3.generarMensaje());
        System.out.println();

        System.out.println(n4.generarMensaje());
        System.out.println();

        // Mostrar todas las notificaciones
        System.out.println("=== LISTADO DEL GESTOR ===");
        System.out.println(gestor.mostrarNotificaciones());
    }
}
