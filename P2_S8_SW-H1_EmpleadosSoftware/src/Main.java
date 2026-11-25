
import modelo.Empleado;
import modelo.Programador;
import modelo.Tester;
import modelo.Disenador;
import modelo.ArquitectoSoftware;
import modelo.EmpresaSoftware;

public class Main {

    public static void main(String[] args) {

        // Crear empleados independientes
        Empleado e1 = new Programador(
                "E01", "Jose", 8000);

        Empleado e2 = new Tester(
                "E02", "Francisco", 1200);

        Empleado e3 = new Disenador(
                "E03", "Pepe", 1300);

        Empleado e4 = new ArquitectoSoftware(
                "E04", "Pancho", 2000);

        // Crear empresa
        EmpresaSoftware empresa = new EmpresaSoftware();

        // Agregar empleados
        empresa.agregarEmpleado(e1);
        empresa.agregarEmpleado(e2);
        empresa.agregarEmpleado(e3);
        empresa.agregarEmpleado(e4);

        System.out.println("=== EMPLEADOS REGISTRADOS ===");
        System.out.println(empresa.mostrarEmpleados());

        System.out.println("=== BONOS INDIVIDUALES ===");

        System.out.println("Programador: $" + e1.calcularBono());
        System.out.println("Tester: $" + e2.calcularBono());
        System.out.println("Disenador: $" + e3.calcularBono());
        System.out.println("Arquitecto: $" + e4.calcularBono());

        System.out.println("=== TOTAL DE BONOS ===");

        System.out.println("$" + empresa.calcularTotalBonos());
    }
}
