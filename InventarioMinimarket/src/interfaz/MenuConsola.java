package interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.Inventario;
import negocio.InventarioException;

/**
 * Menú de consola del minimarket. Es la única clase que imprime en pantalla y
 * que lee datos del teclado; toda la lógica del inventario la hace la clase
 * {@link Inventario}.
 *
 * Cada opción del menú corresponde a un requerimiento funcional (RF1 a RF8).
 * Cuando un dato no es válido se muestra un mensaje de error y se regresa al
 * menú, sin cerrar el programa.
 */
public class MenuConsola {

    private final Inventario inventario;
    private final Scanner teclado = new Scanner(System.in);
    private final LocalDate hoy = LocalDate.now();

    public MenuConsola(Inventario inventario) {
        this.inventario = inventario;
    }

    /** Muestra el menú una y otra vez hasta que el usuario elija salir. */
    public void iniciar() {
        boolean salir = false;

        try {
            while (!salir) {
                mostrarMenu();
                String opcion = leer("Elija una opción: ");

                switch (opcion) {
                    case "1" -> registrarProducto();
                    case "2" -> listarInventario();
                    case "3" -> buscarProducto();
                    case "4" -> registrarVenta();
                    case "5" -> registrarEntrada();
                    case "6" -> mostrarAlertas();
                    case "7" -> mostrarValorInventario();
                    case "8" -> darDeBaja();
                    case "0" -> {
                        System.out.println("Hasta pronto.");
                        salir = true;
                    }
                    default -> System.out.println("Error: la opción no es válida.");
                }
            }
        } catch (NoSuchElementException e) {
            // Se acabó la entrada de datos (por ejemplo, al cerrar la consola).
            System.out.println();
            System.out.println("Fin de la entrada de datos. Hasta pronto.");
        }
    }

    // ------------------------------------------------------------------
    // Menú
    // ------------------------------------------------------------------

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== MINIMARKET EL VECINO - CONTROL DE INVENTARIO =====");
        System.out.println("Fecha de hoy: " + hoy.format(Producto.FORMATO_FECHA));
        System.out.println("1. Registrar producto");
        System.out.println("2. Listar inventario");
        System.out.println("3. Buscar producto");
        System.out.println("4. Registrar venta");
        System.out.println("5. Registrar entrada de mercadería");
        System.out.println("6. Ver alertas (vencimientos y stock bajo)");
        System.out.println("7. Ver valor del inventario");
        System.out.println("8. Dar de baja un producto");
        System.out.println("0. Salir");
    }

    // ------------------------------------------------------------------
    // RF1: registrar producto
    // ------------------------------------------------------------------

    private void registrarProducto() {
        try {
            String tipo = leer("Tipo de producto (1 = Perecedero, 2 = No perecedero): ");
            if (!tipo.equals("1") && !tipo.equals("2")) {
                throw new IllegalArgumentException("El tipo de producto debe ser 1 o 2.");
            }

            String codigo = exigir(leer("Código: "), "El código es obligatorio.");
            String nombre = exigir(leer("Nombre: "), "El nombre es obligatorio.");
            int cantidad = aEntero(leer("Cantidad: "),
                    "La cantidad es obligatoria.", "La cantidad debe ser un número entero.");
            double costo = aDecimal(leer("Costo unitario ($): "),
                    "El costo es obligatorio.", "El costo debe ser un número.");
            double precio = aDecimal(leer("Precio de venta ($): "),
                    "El precio es obligatorio.", "El precio debe ser un número.");
            int stockMinimo = aEntero(leer("Stock mínimo: "),
                    "El stock mínimo es obligatorio.", "El stock mínimo debe ser un número entero.");

            Producto nuevo;
            if (tipo.equals("1")) {
                LocalDate vencimiento = aFecha(leer("Fecha de vencimiento (dd/mm/aaaa): "),
                        "La fecha de vencimiento es obligatoria.",
                        "La fecha no es válida. Use el formato dd/mm/aaaa.");
                String frio = leer("¿Requiere refrigeración? (S/N): ");
                boolean refrigeracion = frio.equalsIgnoreCase("S");
                nuevo = new ProductoPerecedero(codigo, nombre, cantidad, costo, precio,
                        stockMinimo, hoy, vencimiento, refrigeracion);
            } else {
                String estante = exigir(leer("Estante: "), "El estante es obligatorio.");
                nuevo = new ProductoNoPerecedero(codigo, nombre, cantidad, costo, precio,
                        stockMinimo, hoy, estante);
            }

            inventario.registrarProducto(nuevo);
            System.out.println("Producto registrado: " + nuevo + " - " + nuevo.getTipo());

        } catch (InventarioException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // RF2: listar inventario
    // ------------------------------------------------------------------

    private void listarInventario() {
        ArrayList<Producto> lista = inventario.listarProductos();

        if (lista.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }
        System.out.println();
        System.out.println("--- INVENTARIO ---");
        imprimirTabla(lista);
        System.out.println("Total de productos: " + lista.size());
    }

    // ------------------------------------------------------------------
    // RF3: buscar producto
    // ------------------------------------------------------------------

    private void buscarProducto() {
        String texto = leer("Código o parte del nombre: ");

        if (texto.isEmpty()) {
            System.out.println("Error: escriba un código o parte del nombre.");
            return;
        }
        ArrayList<Producto> encontrados = inventario.buscar(texto);

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron productos con \"" + texto + "\".");
        } else {
            System.out.println();
            System.out.println("--- RESULTADO DE LA BÚSQUEDA ---");
            imprimirTabla(encontrados);
        }
    }

    // ------------------------------------------------------------------
    // RF4: registrar venta
    // ------------------------------------------------------------------

    private void registrarVenta() {
        try {
            String codigo = exigir(leer("Código del producto: "), "El código es obligatorio.");
            int cantidad = aEntero(leer("Cantidad a vender: "),
                    "La cantidad es obligatoria.", "La cantidad debe ser un número entero.");

            Producto p = inventario.buscarPorCodigo(codigo);
            double total = inventario.registrarVenta(codigo, cantidad, hoy);

            System.out.println("Venta registrada: " + cantidad + " x " + p.getNombre());
            System.out.println("Precio unitario de hoy: " + dinero(p.calcularPrecioVenta(hoy))
                    + " (precio normal: " + dinero(p.getPrecio()) + ")");
            System.out.println("Total a cobrar: " + dinero(total));
            System.out.println("Stock restante: " + p.getCantidad());

        } catch (InventarioException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // RF5: registrar entrada de mercadería
    // ------------------------------------------------------------------

    private void registrarEntrada() {
        try {
            String codigo = exigir(leer("Código del producto: "), "El código es obligatorio.");
            int cantidad = aEntero(leer("Cantidad que ingresa: "),
                    "La cantidad es obligatoria.", "La cantidad debe ser un número entero.");

            inventario.registrarEntrada(codigo, cantidad, hoy);
            Producto p = inventario.buscarPorCodigo(codigo);

            System.out.println("Entrada registrada: +" + cantidad + " " + p.getNombre());
            System.out.println("Stock actual: " + p.getCantidad());

        } catch (InventarioException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // RF6: alertas
    // ------------------------------------------------------------------

    private void mostrarAlertas() {
        ArrayList<String> alertas = inventario.generarAlertas(hoy);

        System.out.println();
        System.out.println("--- ALERTAS AL " + hoy.format(Producto.FORMATO_FECHA) + " ---");
        if (alertas.isEmpty()) {
            System.out.println("No hay alertas. Todo en orden.");
        } else {
            for (String alerta : alertas) {
                System.out.println(alerta);
            }
            System.out.println("Total de alertas: " + alertas.size());
        }
    }

    // ------------------------------------------------------------------
    // RF7: valor del inventario
    // ------------------------------------------------------------------

    private void mostrarValorInventario() {
        System.out.println();
        System.out.println("--- VALOR DEL INVENTARIO ---");
        System.out.println("Productos registrados: " + inventario.contarProductos());
        System.out.println("Unidades en stock: " + inventario.contarUnidades());
        System.out.println("Valor total a costo: " + dinero(inventario.calcularValorTotal()));
        System.out.println("Valor en riesgo por vencimiento: " + dinero(inventario.calcularValorEnRiesgo(hoy)));
        System.out.println("Refrigeración ocupada: " + inventario.unidadesRefrigeradas()
                + " de " + Inventario.CAPACIDAD_REFRIGERACION + " unidades");
    }

    // ------------------------------------------------------------------
    // RF8: dar de baja
    // ------------------------------------------------------------------

    private void darDeBaja() {
        try {
            String codigo = exigir(leer("Código del producto a dar de baja: "), "El código es obligatorio.");
            Producto p = inventario.buscarPorCodigo(codigo);
            String nombre = p.getNombre();
            int unidades = p.getCantidad();

            double perdida = inventario.darDeBaja(codigo);
            System.out.println("Producto dado de baja: " + nombre + " (" + unidades + " unidades)");
            System.out.println("Pérdida registrada: " + dinero(perdida));

        } catch (InventarioException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo (lectura, validación y formato)
    // ------------------------------------------------------------------

    /** Imprime los productos en forma de tabla. Cada producto aporta su propio detalle. */
    private void imprimirTabla(ArrayList<Producto> lista) {
        System.out.println(String.format("%-5s %-13s %-18s %4s %6s %7s  %s",
                "COD", "TIPO", "NOMBRE", "CANT", "COSTO", "PVP HOY", "DETALLE"));
        System.out.println("-".repeat(92));

        for (Producto p : lista) {
            System.out.println(String.format(Locale.US, "%-5s %-13s %-18s %4d %6.2f %7.2f  %s",
                    p.getCodigo(), p.getTipo(), cortar(p.getNombre(), 18), p.getCantidad(),
                    p.getCosto(), p.calcularPrecioVenta(hoy), p.getDetalleEspecifico(hoy)));
        }
    }

    private String cortar(String texto, int largo) {
        return texto.length() <= largo ? texto : texto.substring(0, largo - 1) + ".";
    }

    private String dinero(double valor) {
        return String.format(Locale.US, "$%.2f", valor);
    }

    /** Muestra el mensaje y devuelve lo que escribió el usuario, sin espacios en los extremos. */
    private String leer(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private String exigir(String texto, String mensajeVacio) {
        if (texto.isEmpty()) {
            throw new IllegalArgumentException(mensajeVacio);
        }
        return texto;
    }

    private int aEntero(String texto, String mensajeVacio, String mensajeInvalido) {
        exigir(texto, mensajeVacio);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeInvalido);
        }
    }

    /** Acepta punto o coma como separador decimal (0.50 o 0,50). */
    private double aDecimal(String texto, String mensajeVacio, String mensajeInvalido) {
        exigir(texto, mensajeVacio);
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeInvalido);
        }
    }

    private LocalDate aFecha(String texto, String mensajeVacio, String mensajeInvalido) {
        exigir(texto, mensajeVacio);
        try {
            return LocalDate.parse(texto, Producto.FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(mensajeInvalido);
        }
    }
}
