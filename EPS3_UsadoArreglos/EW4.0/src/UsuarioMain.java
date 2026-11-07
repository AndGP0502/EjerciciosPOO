import java.util.Scanner;

// Modalidad: se ingresa una cantidad X de usuarios, siempre que haya espacio en el arreglo.
// Las búsquedas se hacen por un elemento específico: username o ID.
public class UsuarioMain {

    public static void main(String[] args) {

        String username, correo;
        int cantidad, id, activos, opc = 0;
        boolean encontrado;

        final int MAX = 3;
        Usuario usuarios[] = new Usuario[MAX];
        int indice = 0; // siguiente posición libre del arreglo
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("\n===== GESTIÓN DE USUARIOS =====");
            System.out.println("1. Ingresar una cantidad de usuarios");
            System.out.println("2. Mostrar todos los usuarios registrados");
            System.out.println("3. Buscar un usuario por username");
            System.out.println("4. Buscar un usuario por ID");
            System.out.println("5. Ver total de usuarios y activos");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opc = Integer.parseInt(sc.nextLine());

                switch (opc) {
                    case 1:
                        System.out.print("¿Cuántos usuarios desea ingresar?: ");
                        cantidad = Integer.parseInt(sc.nextLine());

                        if (cantidad <= 0) {
                            System.out.println("La cantidad debe ser mayor a 0");
                        } else if ((indice + cantidad) <= MAX) {
                            for (int i = 0; i < cantidad; i++) {
                                System.out.println("\nUsuario " + (i + 1) + " de " + cantidad);
                                System.out.print("Ingrese el username: ");
                                username = sc.nextLine();
                                System.out.print("Ingrese el correo: ");
                                correo = sc.nextLine();

                                usuarios[indice++] = new Usuario(username, correo);
                            }
                        } else {
                            System.out.println("No hay espacio. Lugares disponibles: " + (MAX - indice));
                        }
                        break;

                    case 2:
                        if (indice == 0) {
                            System.out.println("Aún no hay usuarios registrados");
                        }
                        for (int i = 0; i < indice; i++) {
                            usuarios[i].mostrarInformacion();
                        }
                        break;

                    case 3:
                        System.out.print("Ingrese el username a buscar: ");
                        username = sc.nextLine();
                        encontrado = false;
                        for (int i = 0; i < indice; i++) {
                            if (usuarios[i].getUsername().equalsIgnoreCase(username)) {
                                usuarios[i].mostrarInformacion();
                                encontrado = true;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("No existe un usuario con ese username");
                        }
                        break;

                    case 4:
                        System.out.print("Ingrese el ID a buscar: ");
                        id = Integer.parseInt(sc.nextLine());
                        encontrado = false;
                        for (int i = 0; i < indice; i++) {
                            if (usuarios[i].getId() == id) {
                                usuarios[i].mostrarInformacion();
                                encontrado = true;
                            }
                        }
                        if (!encontrado) {
                            System.out.println("No existe un usuario con ese ID");
                        }
                        break;

                    case 5:
                        activos = 0;
                        for (int i = 0; i < indice; i++) {
                            if (usuarios[i].getActivo()) {
                                activos++;
                            }
                        }
                        Usuario.mostrarTotal();
                        System.out.println("Usuarios activos: " + activos);
                        System.out.println("Usuarios inactivos: " + (indice - activos));
                        break;

                    case 6:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero");
            }
        } while (opc != 6);

        sc.close();
    }
}
