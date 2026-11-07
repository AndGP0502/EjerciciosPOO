import java.util.Scanner;

// Modalidad: los datos se ingresan uno por uno mientras exista espacio en el arreglo.
// Las demás opciones piden el índice o el elemento específico sobre el que se opera.
public class MainServicio {

    public static void main(String[] args) {

        String nombre;
        int puerto, posicion = 0, opc = 0;
        boolean encontrado;

        final int MAX = 3;
        Servicio servicios[] = new Servicio[MAX];
        int indice = 0; // siguiente posición libre del arreglo
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n===== GESTIÓN DE SERVICIOS =====");
            System.out.println("1. Registrar un servicio");
            System.out.println("2. Iniciar un servicio");
            System.out.println("3. Detener un servicio");
            System.out.println("4. Registrar una petición en un servicio");
            System.out.println("5. Consultar un servicio por índice");
            System.out.println("6. Buscar un servicio por nombre");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opc = Integer.parseInt(sc.nextLine());

                // Las opciones 2 a 5 operan sobre un servicio: se pide y valida su índice
                if (opc >= 2 && opc <= 5) {
                    if (indice == 0) {
                        System.out.println("Aún no hay servicios registrados");
                        continue;
                    }
                    System.out.print("Ingrese el índice del servicio (0 a " + (indice - 1) + "): ");
                    posicion = Integer.parseInt(sc.nextLine());
                    if (posicion < 0 || posicion >= indice) {
                        System.out.println("Índice inválido");
                        continue;
                    }
                }

                switch (opc) {
                    case 1:
                        if (indice < MAX) {
                            System.out.print("Ingrese el nombre del servicio: ");
                            nombre = sc.nextLine();
                            System.out.print("Ingrese el puerto: ");
                            puerto = Integer.parseInt(sc.nextLine());

                            Servicio nuevo = new Servicio();
                            nuevo.setNombre(nombre);
                            nuevo.setPuerto(puerto);

                            // Los setters ignoran los datos inválidos, por eso se verifica antes de guardar
                            if (nuevo.getNombre() != null && nuevo.getPuerto() > 0) {
                                servicios[indice++] = nuevo;
                                System.out.println("Servicio registrado en el índice " + (indice - 1));
                            } else {
                                System.out.println("No se registró: el nombre no puede estar vacío y el puerto debe ser mayor a 0");
                            }
                        } else {
                            System.out.println("No hay más memoria");
                        }
                        break;

                    case 2:
                        servicios[posicion].iniciar();
                        System.out.println("Servicio " + servicios[posicion].getNombre() + " iniciado");
                        break;

                    case 3:
                        servicios[posicion].detener();
                        System.out.println("Servicio " + servicios[posicion].getNombre() + " detenido");
                        break;

                    case 4:
                        if (servicios[posicion].isActivo()) {
                            servicios[posicion].registrarPeticion();
                            System.out.println("Peticiones de " + servicios[posicion].getNombre() + ": "
                                    + servicios[posicion].getCantidadPeticiones());
                        } else {
                            System.out.println("No se registró la petición: el servicio está detenido");
                        }
                        break;

                    case 5:
                        System.out.println("Nombre: " + servicios[posicion].getNombre());
                        System.out.println("Puerto: " + servicios[posicion].getPuerto());
                        System.out.println("Activo: " + (servicios[posicion].isActivo() ? "Sí" : "No"));
                        System.out.println("Peticiones: " + servicios[posicion].getCantidadPeticiones());
                        break;

                    case 6:
                        System.out.print("Ingrese el nombre a buscar: ");
                        nombre = sc.nextLine();
                        encontrado = false;
                        for (int i = 0; i < indice; i++) {
                            if (servicios[i].getNombre().equalsIgnoreCase(nombre)) {
                                System.out.println("Encontrado en el índice " + i);
                                System.out.println("Puerto: " + servicios[i].getPuerto());
                                System.out.println("Activo: " + (servicios[i].isActivo() ? "Sí" : "No"));
                                System.out.println("Peticiones: " + servicios[i].getCantidadPeticiones());
                                encontrado = true;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("No existe un servicio con ese nombre");
                        }
                        break;

                    case 7:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero");
            }
        } while (opc != 7);

        sc.close();
    }
}
