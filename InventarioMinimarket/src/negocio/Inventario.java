package negocio;

import java.time.LocalDate;
import java.util.ArrayList;

import modelo.Producto;

/**
 * Clase que administra todos los productos del minimarket y aplica las reglas
 * del negocio. Aquí no se imprime nada ni se pide nada al usuario: eso le
 * corresponde al paquete interfaz. Los métodos devuelven datos o lanzan
 * {@link InventarioException} cuando algo no se puede hacer.
 *
 * Trabaja siempre con la clase {@link Producto}, sin importar si es
 * perecedero o no. Cada producto responde a su manera (polimorfismo), por eso
 * en esta clase no se usa instanceof.
 */
public class Inventario {

    /** Unidades que caben en las refrigeradoras de la tienda (restricción de espacio). */
    public static final int CAPACIDAD_REFRIGERACION = 150;

    private final ArrayList<Producto> productos = new ArrayList<>();

    // ------------------------------------------------------------------
    // RF1: registrar producto
    // ------------------------------------------------------------------

    /**
     * Agrega un producto nuevo al inventario.
     * Reglas: el código no puede repetirse y las unidades que necesiten frío
     * no pueden pasar la capacidad de las refrigeradoras.
     */
    public void registrarProducto(Producto nuevo) throws InventarioException {
        if (nuevo == null) {
            throw new InventarioException("No hay un producto para registrar.");
        }
        if (existeCodigo(nuevo.getCodigo())) {
            throw new InventarioException("Ya existe un producto con el código " + nuevo.getCodigo() + ".");
        }
        if (unidadesRefrigeradas() + nuevo.unidadesRefrigeradas() > CAPACIDAD_REFRIGERACION) {
            throw new InventarioException("No hay espacio en refrigeración para este producto.");
        }
        productos.add(nuevo);
    }

    // ------------------------------------------------------------------
    // RF2 y RF3: consultar
    // ------------------------------------------------------------------

    /** Devuelve una copia de la lista, así nadie de afuera puede modificar el inventario. */
    public ArrayList<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    /** Busca productos cuyo código o nombre contengan el texto (sin importar mayúsculas). */
    public ArrayList<Producto> buscar(String texto) {
        ArrayList<Producto> encontrados = new ArrayList<>();
        String criterio = texto.trim().toLowerCase();

        for (Producto p : productos) {
            if (p.getCodigo().toLowerCase().contains(criterio)
                    || p.getNombre().toLowerCase().contains(criterio)) {
                encontrados.add(p);
            }
        }
        return encontrados;
    }

    /** Busca un producto por su código exacto. */
    public Producto buscarPorCodigo(String codigo) throws InventarioException {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        throw new InventarioException("No existe un producto con el código " + codigo.trim().toUpperCase() + ".");
    }

    // ------------------------------------------------------------------
    // RF4: venta (salida de stock)
    // ------------------------------------------------------------------

    /**
     * Registra la venta de unidades de un producto y devuelve el total a cobrar.
     * Reglas: la cantidad debe ser mayor a cero, el producto no puede estar
     * vencido y no se puede vender más de lo que hay en stock.
     */
    public double registrarVenta(String codigo, int cantidad, LocalDate hoy) throws InventarioException {
        if (cantidad <= 0) {
            throw new InventarioException("La cantidad a vender debe ser mayor a cero.");
        }
        Producto p = buscarPorCodigo(codigo);

        if (p.estaVencido(hoy)) {
            throw new InventarioException("No se puede vender " + p.getNombre() + ": el producto está vencido.");
        }
        if (cantidad > p.getCantidad()) {
            throw new InventarioException("Stock insuficiente. Disponible: " + p.getCantidad()
                    + ", solicitado: " + cantidad + ".");
        }
        p.reducirStock(cantidad);
        return Math.round(p.calcularPrecioVenta(hoy) * cantidad * 100.0) / 100.0;
    }

    // ------------------------------------------------------------------
    // RF5: entrada de mercadería
    // ------------------------------------------------------------------

    /**
     * Suma unidades al stock de un producto que ya existe.
     * Reglas: cantidad mayor a cero, el producto no puede estar vencido y, si
     * necesita frío, no se puede pasar la capacidad de las refrigeradoras.
     */
    public void registrarEntrada(String codigo, int cantidad, LocalDate hoy) throws InventarioException {
        if (cantidad <= 0) {
            throw new InventarioException("La cantidad a ingresar debe ser mayor a cero.");
        }
        Producto p = buscarPorCodigo(codigo);

        if (p.estaVencido(hoy)) {
            throw new InventarioException("El producto está vencido: dé de baja el lote antes de reabastecer.");
        }
        if (p.usaRefrigeracion()
                && unidadesRefrigeradas() + cantidad > CAPACIDAD_REFRIGERACION) {
            throw new InventarioException("No hay espacio en refrigeración. Capacidad: "
                    + CAPACIDAD_REFRIGERACION + ", ocupado: " + unidadesRefrigeradas()
                    + ", se intentó agregar: " + cantidad + ".");
        }
        p.aumentarStock(cantidad);
    }

    // ------------------------------------------------------------------
    // RF6: alertas
    // ------------------------------------------------------------------

    /**
     * Arma la lista de alertas del día en tres grupos: vencidos, por vencer y
     * stock bajo. Devuelve textos; la interfaz es la que los imprime.
     */
    public ArrayList<String> generarAlertas(LocalDate hoy) {
        ArrayList<String> alertas = new ArrayList<>();

        for (Producto p : productos) {
            if (p.estaVencido(hoy)) {
                alertas.add("VENCIDO    | " + p + " - " + p.getCantidad()
                        + " und., pérdida de " + dinero(p.calcularValorInventario()));
            }
        }
        for (Producto p : productos) {
            if (p.requiereAtencion(hoy) && !p.estaVencido(hoy)) {
                alertas.add("POR VENCER | " + p + " - " + p.getDetalleEspecifico(hoy)
                        + " - " + p.getCantidad() + " und.");
            }
        }
        for (Producto p : productos) {
            if (p.tieneStockBajo()) {
                alertas.add("STOCK BAJO | " + p + " - quedan " + p.getCantidad()
                        + ", mínimo " + p.getStockMinimo());
            }
        }
        return alertas;
    }

    // ------------------------------------------------------------------
    // RF7: valor del inventario
    // ------------------------------------------------------------------

    /** Suma lo invertido en todos los productos (cantidad por costo). */
    public double calcularValorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.calcularValorInventario();
        }
        return total;
    }

    /** Dinero invertido en productos vencidos o por vencer. */
    public double calcularValorEnRiesgo(LocalDate hoy) {
        double total = 0;
        for (Producto p : productos) {
            if (p.requiereAtencion(hoy)) {
                total += p.calcularValorInventario();
            }
        }
        return total;
    }

    public int contarProductos() {
        return productos.size();
    }

    public int contarUnidades() {
        int total = 0;
        for (Producto p : productos) {
            total += p.getCantidad();
        }
        return total;
    }

    /** Unidades que hay ahora mismo dentro de las refrigeradoras. */
    public int unidadesRefrigeradas() {
        int total = 0;
        for (Producto p : productos) {
            total += p.unidadesRefrigeradas();
        }
        return total;
    }

    // ------------------------------------------------------------------
    // RF8: dar de baja
    // ------------------------------------------------------------------

    /**
     * Saca un producto del inventario (por vencimiento, daño, etc.) y devuelve
     * el dinero que se pierde, calculado a costo.
     */
    public double darDeBaja(String codigo) throws InventarioException {
        Producto p = buscarPorCodigo(codigo);
        double perdida = p.calcularValorInventario();
        productos.remove(p);
        return perdida;
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo
    // ------------------------------------------------------------------

    private boolean existeCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    private String dinero(double valor) {
        return String.format(java.util.Locale.US, "$%.2f", valor);
    }
}
