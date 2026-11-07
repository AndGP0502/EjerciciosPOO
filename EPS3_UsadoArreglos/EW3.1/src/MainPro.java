import java.util.Scanner;

// Modalidad: todas las opciones trabajan con el arreglo completo,
// recorriéndolo con ciclos for apoyados en la propiedad length.
public class MainPro {

    public static void main(String[] args) {

        int porcentaje, suma, terminados, opc = 0;
        boolean lleno = false;

        final int MAX = 3;
        Proyecto proyectos[] = new Proyecto[MAX];
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n===== GESTIÓN DE PROYECTOS =====");
            System.out.println("1. Ingresar todos los proyectos");
            System.out.println("2. Mostrar todos los proyectos");
            System.out.println("3. Registrar avance en todos los proyectos");
            System.out.println("4. Ver resumen de todos los proyectos");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opc = Integer.parseInt(sc.nextLine());

                // Las opciones 2, 3 y 4 necesitan que el arreglo ya esté lleno
                if (opc >= 2 && opc <= 4 && !lleno) {
                    System.out.println("Primero debe ingresar todos los proyectos (opción 1)");
                    continue;
                }

                switch (opc) {
                    case 1:
                        for (int i = 0; i < proyectos.length; i++) {
                            System.out.println("\nProyecto " + (i + 1) + " de " + proyectos.length);
                            proyectos[i] = new Proyecto();

                            // Se repite hasta que el setter acepte el dato
                            do {
                                System.out.print("Ingrese el nombre: ");
                                proyectos[i].setNombre(sc.nextLine());
                            } while (proyectos[i].getNombre() == null);

                            do {
                                System.out.print("Ingrese el responsable: ");
                                proyectos[i].setResponsable(sc.nextLine());
                            } while (proyectos[i].getResponsable() == null);
                        }
                        lleno = true;
                        break;

                    case 2:
                        for (int i = 0; i < proyectos.length; i++) {
                            System.out.println("\nProyecto " + (i + 1));
                            System.out.println("Nombre: " + proyectos[i].getNombre());
                            System.out.println("Responsable: " + proyectos[i].getResponsable());
                            System.out.println("Progreso: " + proyectos[i].getProgreso() + "%");
                            System.out.println("Terminado: " + (proyectos[i].isTerminado() ? "Sí" : "No"));
                        }
                        break;

                    case 3:
                        for (int i = 0; i < proyectos.length; i++) {
                            System.out.print("\nIngrese el avance de " + proyectos[i].getNombre() + ": ");
                            porcentaje = Integer.parseInt(sc.nextLine());
                            proyectos[i].avanzar(porcentaje);
                        }
                        break;

                    case 4:
                        suma = 0;
                        terminados = 0;
                        for (int i = 0; i < proyectos.length; i++) {
                            suma += proyectos[i].getProgreso();
                            if (proyectos[i].isTerminado()) {
                                terminados++;
                            }
                        }
                        System.out.println("Proyectos terminados: " + terminados + " de " + proyectos.length);
                        System.out.printf("Progreso promedio: %.1f%%%n", (double) suma / proyectos.length);
                        break;

                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero");
            }
        } while (opc != 5);

        sc.close();
    }
}
