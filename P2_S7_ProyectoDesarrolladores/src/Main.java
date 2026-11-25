import modelo.Desarrollador;
import modelo.Proyecto;

public class Main {

    public static void main(String[] args) {

        Desarrollador d1 = new Desarrollador(
                "D01", "Ana", "Backend", 20);

        Desarrollador d2 = new Desarrollador(
                "D02", "Luis", "Frontend", 15);

        Desarrollador d3 = new Desarrollador(
                "D03", "Carlos", "Base de Datos", 30);

        Proyecto proyecto = new Proyecto("Sistema de ventas");

        System.out.println("=== PROYECTO VACIO ===");
        System.out.println(proyecto.mostrarEquipo());

        proyecto.agregarDesarrollador(d1);
        proyecto.agregarDesarrollador(d2);
        proyecto.agregarDesarrollador(d3);

        System.out.println("=== EQUIPO INICIAL ===");
        System.out.println(proyecto.mostrarEquipo());

        System.out.println("=== TOTAL DE HORAS ===");
        System.out.println(proyecto.calcularTotalHoras());

        System.out.println("=== BUSCAR DESARROLLADOR ===");

        Desarrollador encontrado =
                proyecto.buscarDesarrollador("D02");

        if (encontrado != null) {
            System.out.println(encontrado.mostrarInformacion());
        } else {
            System.out.println("Desarrollador no encontrado");
        }

        System.out.println("=== BUSCAR INEXISTENTE ===");

        Desarrollador inexistente =
                proyecto.buscarDesarrollador("D10");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Desarrollador no encontrado");
        }

        System.out.println("=== MAYOR CARGA ===");

        Desarrollador mayor = proyecto.obtenerMayorCarga();

        if (mayor != null) {
            System.out.println(mayor.mostrarInformacion());
        }

        System.out.println("=== ELIMINAR D02 ===");
        System.out.println(proyecto.eliminarDesarrollador("D02"));

        System.out.println("=== EQUIPO ACTUALIZADO ===");
        System.out.println(proyecto.mostrarEquipo());

        System.out.println("=== HORAS ACTUALIZADAS ===");
        System.out.println(proyecto.calcularTotalHoras());
    }
}
