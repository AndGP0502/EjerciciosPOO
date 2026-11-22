package interfaz;

import java.time.LocalDate;

import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.Inventario;
import negocio.InventarioException;

/**
 * Punto de entrada del programa. Crea el inventario, le carga unos productos
 * de ejemplo (para poder probar el sistema sin escribir todo a mano) y abre el menú.
 *
 * Las fechas de vencimiento de los ejemplos se calculan a partir de la fecha
 * de hoy, así siempre hay productos vigentes, por vencer y vencidos.
 */
public class Main {

    public static void main(String[] args) {
        Inventario inventario = new Inventario();

        try {
            cargarDatosDeEjemplo(inventario);
        } catch (InventarioException e) {
            System.out.println("No se pudieron cargar los datos de ejemplo: " + e.getMessage());
        }

        new MenuConsola(inventario).iniciar();
    }

    private static void cargarDatosDeEjemplo(Inventario inventario) throws InventarioException {
        LocalDate hoy = LocalDate.now();

        // Perecederos: (código, nombre, cantidad, costo, precio, stock mínimo,
        //               fecha de ingreso, fecha de vencimiento, ¿necesita frío?)
        inventario.registrarProducto(new ProductoPerecedero("L001", "Leche entera 1L",
                40, 0.85, 1.10, 15, hoy.minusDays(3), hoy.plusDays(12), true));
        inventario.registrarProducto(new ProductoPerecedero("Y001", "Yogurt natural 1L",
                24, 1.20, 1.60, 10, hoy.minusDays(4), hoy.plusDays(5), true));
        inventario.registrarProducto(new ProductoPerecedero("Q001", "Queso fresco 500g",
                12, 2.50, 3.40, 5, hoy.minusDays(6), hoy.plusDays(2), true));
        inventario.registrarProducto(new ProductoPerecedero("P001", "Pan de molde",
                8, 1.00, 1.50, 6, hoy.minusDays(8), hoy.minusDays(1), false));

        // No perecederos: (código, nombre, cantidad, costo, precio, stock mínimo,
        //                  fecha de ingreso, estante)
        inventario.registrarProducto(new ProductoNoPerecedero("A001", "Arroz 1kg",
                120, 0.80, 1.05, 30, hoy.minusDays(10), "B2"));
        inventario.registrarProducto(new ProductoNoPerecedero("T001", "Atún en lata",
                5, 1.10, 1.50, 20, hoy.minusDays(15), "B1"));
        inventario.registrarProducto(new ProductoNoPerecedero("D001", "Detergente 1kg",
                30, 2.20, 3.00, 10, hoy.minusDays(20), "C1"));
        inventario.registrarProducto(new ProductoNoPerecedero("F001", "Fideos 400g",
                60, 0.55, 0.80, 20, hoy.minusDays(12), "B3"));
    }
}
