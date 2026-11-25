import modelo.Sprint;
import modelo.Tarea;

public class Main {

    public static void main(String[] args) {

        Sprint sprint = new Sprint();

        System.out.println("=== CREAR TAREAS ===");

        sprint.crearTarea("T01", "Diseñar interfaz", 5);
        sprint.crearTarea("T02", "Crear base de datos", 8);
        sprint.crearTarea("T03", "Implementar login", 6);
        sprint.crearTarea("T04", "Realizar pruebas", 4);
        sprint.crearTarea("T05", "Generar reportes", 7);

        System.out.println(sprint.mostrarTareas());

        System.out.println("=== COMPLETAR TAREAS ===");

        System.out.println(sprint.completarTarea("T01"));
        System.out.println(sprint.completarTarea("T03"));

        System.out.println("=== BUSCAR TAREA EXISTENTE ===");

        Tarea encontrada = sprint.buscarTarea("T02");

        if (encontrada != null) {
            System.out.println(encontrada.mostrarInformacion());
        } else {
            System.out.println("Tarea no encontrada");
        }

        System.out.println("=== BUSCAR TAREA INEXISTENTE ===");

        Tarea inexistente = sprint.buscarTarea("T10");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Tarea no encontrada");
        }

        System.out.println("=== TAREAS COMPLETADAS ===");

        System.out.println(sprint.contarCompletadas());

        System.out.println("=== HORAS PENDIENTES ===");

        System.out.println(sprint.calcularHorasPendientes());

        System.out.println("=== PROBAR CODIGO DUPLICADO ===");

        sprint.crearTarea("T01", "Tarea duplicada", 10);

        System.out.println(sprint.mostrarTareas());

        System.out.println("=== RESULTADO FINAL ===");

        System.out.println("Completadas: " + sprint.contarCompletadas());
        System.out.println("Horas pendientes: " +
                sprint.calcularHorasPendientes());
    }
}
