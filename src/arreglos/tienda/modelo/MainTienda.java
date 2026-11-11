package arreglos.tienda.modelo;

import utilitario.Valida;

import javax.swing.*;
import java.util.Scanner;

public class MainTienda {
    private static Scanner sc = new Scanner(System.in);
    private static AppStore tienda = new AppStore();

    public static void main(String[] args) {
        int opc;
        tienda.cargarDatos();

        do {
            menu();
            opc = Integer.parseInt(sc.nextLine());

            switch (opc) {

                case 1: {
                    ingresarjuegos();
                    mostrarinformacion();
                } break;

                case 2: {
                    System.out.println("*******Lista de juegos*******");
                    mostrarinformacion();
                } break;

                case 3: {
                    System.out.println("*******Buscar juego*******");
                    System.out.print("Ingrese el nombre del juego: ");
                    String nombre = sc.nextLine();

                    Juego j = tienda.buscarJuego(nombre);

                    if (j != null) {
                        System.out.println("Juego encontrado:");
                        System.out.println(j);
                    } else {
                        System.out.println("El juego no existe");
                    }
                } break;


                case 4: {
                    System.out.println("*******Comprar licencias*******");

                    System.out.print("Ingrese el nombre del juego: ");
                    String nombre = sc.nextLine();

                    Juego j = tienda.buscarJuego(nombre);

                    if (j != null) {
                        System.out.print("Ingrese la cantidad de licencias: ");
                        int cantidad = Integer.parseInt(sc.nextLine());

                        double total = j.getPrecio() * cantidad;

                        if (tienda.comprar(nombre, cantidad)) {
                            System.out.println("Compra realizada con exito");
                            System.out.println("Precio unitario: $" + j.getPrecio());
                            System.out.println("Cantidad comprada: " + cantidad);
                            System.out.println("Total a pagar: $" + total);
                        } else {
                            System.out.println("No se pudo realizar la compra");
                        }

                    } else {
                        System.out.println("El juego no existe");
                    }
                } break;
                case 5: {
                    System.out.println("*******Vender licencias*******");
                    System.out.print("Ingrese el nombre del juego: ");
                    String nombre = sc.nextLine();

                    System.out.print("Ingrese la cantidad de licencias: ");
                    int cantidad = Integer.parseInt(sc.nextLine());

                    double total = tienda.vender(nombre, cantidad);

                    if (total > 0) {
                        System.out.println("Venta realizada con exito");
                        System.out.println("Total de la venta: $" + total);
                    } else {
                        System.out.println("No se pudo realizar la venta");
                        System.out.println("Verifique el nombre o el stock disponible");
                    }
                } break;

                case 6: {
                    System.out.println("*******Juego mas vendido*******");

                    String nombre = tienda.masVendido();

                    if (nombre != null) {
                        System.out.println("El juego mas vendido es: " + nombre);
                    } else {
                        System.out.println("Todavia no se han vendido juegos");
                    }
                } break;

                case 7: {
                    System.out.println("Gracias por usar nuestros servicios");
                } break;

                default: {
                    System.out.println("No es una opcion valida");
                }
            }

        } while (opc != 7);
    }

    public static void menu() {
        System.out.println("**********AppStore Udla**********");
        System.out.println("1. Ingresar Juego");
        System.out.println("2. Mostrar juegos");
        System.out.println("3. Buscar juego");
        System.out.println("4. Comprar");
        System.out.println("5. Vender");
        System.out.println("6. Juego mas vendido");
        System.out.println("7. Salir");
        System.out.print("Ingrese una opcion: ");
    }

    public static void ingresarjuegos() {
        System.out.println("*******Ingreso de datos********");


        String nombre = Valida.leerString("Ingrese el nombre");

        System.out.print("Ingrese la categoria: ");
        String categoria = sc.nextLine();

        System.out.print("Ingrese el tamanio: ");
        int tamanio = Integer.parseInt(sc.nextLine());

        System.out.print("Ingrese el precio: ");
        double precio = Double.parseDouble(sc.nextLine());

        System.out.print("Ingrese el stock inicial: ");
        int licenciasDisponibles = Integer.parseInt(sc.nextLine());

        if (tienda.agregarJuego(new Juego(nombre, categoria, tamanio,
                precio, licenciasDisponibles))) {
            JOptionPane.showMessageDialog(null,
                    "El juego se ha adicionado con exito");
        } else {
            JOptionPane.showMessageDialog(null,
                    "El juego no pudo ser guardado");
        }
    }

    public static void mostrarinformacion() {
        if (tienda.getIndice() >= 0) {
            Juego juegos[] = tienda.getJuegos();

            for (int i = 0; i <= tienda.getIndice(); i++) {
                System.out.println(juegos[i]);
            }
        } else {
            System.out.println("No hay juegos que mostrar");
        }
    }
}