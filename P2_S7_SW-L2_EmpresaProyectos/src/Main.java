
import modelo.Proyecto;
import modelo.EmpresaSoftware;

public class Main {

    public static void main(String[] args) {

        // Crear proyectos independientes
        Proyecto p1 = new Proyecto(
                "P01", "Sistema de ventas", 15000);

        Proyecto p2 = new Proyecto(
                "P02", "Aplicacion movil", 25000);

        Proyecto p3 = new Proyecto(
                "P03", "Pagina web", 10000);

        Proyecto p4 = new Proyecto(
                "P04", "Sistema de inventario", 20000);

        // Crear empresa
        EmpresaSoftware empresa = new EmpresaSoftware();

        System.out.println("=== EMPRESA SIN PROYECTOS ===");
        System.out.println(empresa.mostrarProyectos());

        // Agregar proyectos
        empresa.agregarProyecto(p1);
        empresa.agregarProyecto(p2);
        empresa.agregarProyecto(p3);
        empresa.agregarProyecto(p4);

        System.out.println("=== PROYECTOS REGISTRADOS ===");
        System.out.println(empresa.mostrarProyectos());

        System.out.println("=== PRESUPUESTO TOTAL ===");
        System.out.println(empresa.calcularPresupuestoTotal());

        System.out.println("=== BUSCAR PROYECTO EXISTENTE ===");

        Proyecto encontrado = empresa.buscarProyecto("P02");

        if (encontrado != null) {
            System.out.println(encontrado.mostrarInformacion());
        } else {
            System.out.println("Proyecto no encontrado");
        }

        System.out.println("=== BUSCAR PROYECTO INEXISTENTE ===");

        Proyecto inexistente = empresa.buscarProyecto("P10");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Proyecto no encontrado");
        }

        System.out.println("=== PROYECTO MAYOR PRESUPUESTO ===");

        Proyecto mayor = empresa.obtenerProyectoMayorPresupuesto();

        if (mayor != null) {
            System.out.println(mayor.mostrarInformacion());
        }

        System.out.println("=== ELIMINAR PROYECTO P03 ===");
        System.out.println(empresa.eliminarProyecto("P03"));

        System.out.println("=== PROYECTOS ACTUALIZADOS ===");
        System.out.println(empresa.mostrarProyectos());

        System.out.println("=== NUEVO PRESUPUESTO TOTAL ===");
        System.out.println(empresa.calcularPresupuestoTotal());

        System.out.println("=== COMPROBAR AGREGACION ===");

        System.out.println(p3.mostrarInformacion());
    }
}
