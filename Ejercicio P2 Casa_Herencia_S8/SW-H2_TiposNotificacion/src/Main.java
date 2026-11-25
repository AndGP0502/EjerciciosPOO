import modelo.*;

public class Main {
    public static void main(String[] args) {
        GestorNotificaciones gestor = new GestorNotificaciones();

        // Referencias de tipo Notificacion que apuntan a objetos de las subclases
        Notificacion n1 = new NotificacionEmail("ana.torres@correo.com", "Tu pedido fue enviado", "Estado del pedido");
        Notificacion n2 = new NotificacionSMS("Marco Lema", "Tu codigo de verificacion es 4821", "0991234567");
        Notificacion n3 = new NotificacionPush("Paola Ruiz", "Tienes una nueva actualizacion", "MiBanco");
        Notificacion n4 = new NotificacionSlack("Equipo Backend", "El despliegue termino sin errores", "#despliegues");

        gestor.agregarNotificacion(n1);
        gestor.agregarNotificacion(n2);
        gestor.agregarNotificacion(n3);
        gestor.agregarNotificacion(n4);

        System.out.println("===== PRUEBA 1: Mensajes generados diferentes =====");
        System.out.println(n1.generarMensaje());
        System.out.println(n2.generarMensaje());
        System.out.println(n3.generarMensaje());
        System.out.println(n4.generarMensaje());

        System.out.println();
        System.out.println("===== PRUEBA 2: Listado desde el gestor =====");
        System.out.println("Notificaciones registradas: " + gestor.getCantidadNotificaciones());
        System.out.print(gestor.listarNotificaciones());
    }
}
