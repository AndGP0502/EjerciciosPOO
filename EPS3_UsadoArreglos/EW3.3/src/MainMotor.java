import java.util.Scanner;

// Modalidad: se ingresa una cantidad X de motores, siempre que haya espacio en el arreglo.
// Las operaciones sobre un motor piden su índice.
public class MainMotor {

    public static void main(String[] args) {

        String nombre;
        double potencia;
        int velocidad, cantidad, contador, posicion = 0, opc = 0;

        final int MAX = 3;
        Motor motores[] = new Motor[MAX];
        int indice = 0; // siguiente posición libre del arreglo
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n===== GESTIÓN DE MOTORES =====");
            System.out.println("1. Ingresar una cantidad de motores");
            System.out.println("2. Encender un motor");
            System.out.println("3. Apagar un motor");
            System.out.println("4. Cambiar la velocidad de un motor");
            System.out.println("5. Mostrar un motor");
            System.out.println("6. Mostrar todos los motores registrados");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opc = Integer.parseInt(sc.nextLine());

                // Las opciones 2 a 5 operan sobre un motor: se pide y valida su índice
                if (opc >= 2 && opc <= 5) {
                    if (indice == 0) {
                        System.out.println("Aún no hay motores registrados");
                        continue;
                    }
                    System.out.print("Ingrese el índice del motor (0 a " + (indice - 1) + "): ");
                    posicion = Integer.parseInt(sc.nextLine());
                    if (posicion < 0 || posicion >= indice) {
                        System.out.println("Índice inválido");
                        continue;
                    }
                }

                switch (opc) {
                    case 1:
                        System.out.print("¿Cuántos motores desea ingresar?: ");
                        cantidad = Integer.parseInt(sc.nextLine());

                        if (cantidad <= 0) {
                            System.out.println("La cantidad debe ser mayor a 0");
                        } else if ((indice + cantidad) <= MAX) {
                            contador = 0;
                            while (contador < cantidad) {
                                System.out.println("\nMotor " + (contador + 1) + " de " + cantidad);
                                System.out.print("Ingrese el nombre: ");
                                nombre = sc.nextLine();
                                System.out.print("Ingrese la potencia: ");
                                potencia = Double.parseDouble(sc.nextLine());
                                System.out.print("Ingrese la velocidad: ");
                                velocidad = Integer.parseInt(sc.nextLine());

                                Motor nuevo = new Motor();
                                nuevo.setNombre(nombre);
                                nuevo.setPotencia(potencia);
                                nuevo.setVelocidad(velocidad);

                                motores[indice++] = nuevo;
                                contador++;
                            }
                        } else {
                            System.out.println("No hay espacio. Lugares disponibles: " + (MAX - indice));
                        }
                        break;

                    case 2:
                        motores[posicion].encender();
                        break;

                    case 3:
                        motores[posicion].apagar();
                        break;

                    case 4:
                        System.out.print("Ingrese la nueva velocidad: ");
                        velocidad = Integer.parseInt(sc.nextLine());
                        motores[posicion].setVelocidad(velocidad);
                        break;

                    case 5:
                        System.out.println("Nombre: " + motores[posicion].getNombre());
                        System.out.println("Potencia: " + motores[posicion].getPotencia());
                        System.out.println("Velocidad: " + motores[posicion].getVelocidad());
                        System.out.println("Encendido: " + (motores[posicion].getEncendido() ? "Sí" : "No"));
                        break;

                    case 6:
                        if (indice == 0) {
                            System.out.println("Aún no hay motores registrados");
                        }
                        for (int i = 0; i < indice; i++) {
                            System.out.println("\nÍndice " + i);
                            System.out.println("Nombre: " + motores[i].getNombre());
                            System.out.println("Potencia: " + motores[i].getPotencia());
                            System.out.println("Velocidad: " + motores[i].getVelocidad());
                            System.out.println("Encendido: " + (motores[i].getEncendido() ? "Sí" : "No"));
                        }
                        break;

                    case 7:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido (use punto para los decimales)");
            }
        } while (opc != 7);

        sc.close();
    }
}
