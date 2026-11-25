package interfaz;

import negocio.SistemaPuente;

/**
 * Menú principal. Tiene una opción por cada módulo del sistema;
 * cada módulo es un menú que trabaja con el mismo objeto SistemaPuente.
 */
public class MenuPrincipal {

    private MenuEstudiantes menuEstudiantes;
    private MenuProgramas menuProgramas;
    private MenuCasos menuCasos;
    private MenuAlertas menuAlertas;
    private MenuRendimiento menuRendimiento;

    public MenuPrincipal(SistemaPuente sistema) {
        menuEstudiantes = new MenuEstudiantes(sistema);
        menuProgramas = new MenuProgramas(sistema);
        menuCasos = new MenuCasos(sistema);
        menuAlertas = new MenuAlertas();
        menuRendimiento = new MenuRendimiento(sistema);
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("PUENTE ESTUDIANTIL – DECE");
            System.out.println("1. Estudiantes y riesgo");
            System.out.println("2. Programas de apoyo");
            System.out.println("3. Casos de seguimiento");
            System.out.println("4. Alertas y reportes");
            System.out.println("5. Rendimiento académico");
            System.out.println("0. Salir");
            opcion = Consola.leerEntero("Elija una opción");

            switch (opcion) {
                case 1:
                    menuEstudiantes.mostrar();
                    break;
                case 2:
                    menuProgramas.mostrar();
                    break;
                case 3:
                    menuCasos.mostrar();
                    break;
                case 4:
                    menuAlertas.mostrar();
                    break;
                case 5:
                    menuRendimiento.mostrar();
                    break;
                case 0:
                    System.out.println("Fin del programa.");
                    break;
                default:
                    Consola.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }
}
