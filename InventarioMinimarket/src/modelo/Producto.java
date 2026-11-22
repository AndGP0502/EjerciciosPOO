package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Clase base de todos los productos del minimarket.
 *
 * Es abstracta porque en la tienda nunca existe un "producto" a secas:
 * siempre es perecedero o no perecedero. Aquí van los datos y las reglas que
 * comparten los dos tipos; lo que cambia en cada tipo se declara como método
 * abstracto y lo implementa cada subclase (polimorfismo).
 *
 * Todos los atributos son privados (encapsulamiento) y solo se modifican por
 * medio de los setters, que validan el dato antes de guardarlo.
 */
public abstract class Producto {

    /** Formato en el que se piden y se muestran las fechas (dd/MM/aaaa). */
    public static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private String codigo;
    private String nombre;
    private int cantidad;
    private double costo;        // lo que le cuesta a la tienda cada unidad
    private double precio;       // precio de venta normal al público
    private int stockMinimo;     // por debajo (o igual) de este valor se genera alerta
    private LocalDate fechaIngreso;

    /**
     * Constructor para las subclases. Usa los setters para que las
     * validaciones se hagan en un solo lugar.
     */
    protected Producto(String codigo, String nombre, int cantidad, double costo,
                       double precio, int stockMinimo, LocalDate fechaIngreso) {
        setCodigo(codigo);
        setNombre(nombre);
        setCantidad(cantidad);
        setCosto(costo);
        setPrecio(precio);
        setStockMinimo(stockMinimo);
        setFechaIngreso(fechaIngreso);
    }

    // ------------------------------------------------------------------
    // Getters y setters (con validación)
    // ------------------------------------------------------------------

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        if (costo <= 0) {
            throw new IllegalArgumentException("El costo debe ser mayor a cero.");
        }
        // Si el precio ya existe, el costo nuevo no puede superarlo.
        if (precio > 0 && costo > precio) {
            throw new IllegalArgumentException("El costo no puede ser mayor al precio de venta.");
        }
        this.costo = costo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (precio < costo) {
            throw new IllegalArgumentException("El precio de venta no puede ser menor al costo.");
        }
        this.precio = precio;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo.");
        }
        this.stockMinimo = stockMinimo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria.");
        }
        this.fechaIngreso = fechaIngreso;
    }

    // ------------------------------------------------------------------
    // Comportamiento común a todos los productos
    // ------------------------------------------------------------------

    /** Suma unidades al stock (entrada de mercadería). */
    public void aumentarStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser mayores a cero.");
        }
        cantidad += unidades;
    }

    /** Resta unidades del stock (venta). No permite que el stock quede negativo. */
    public void reducirStock(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser mayores a cero.");
        }
        if (unidades > cantidad) {
            throw new IllegalArgumentException("No hay suficientes unidades en stock.");
        }
        cantidad -= unidades;
    }

    /** Dinero invertido en este producto: unidades en stock por costo unitario. */
    public double calcularValorInventario() {
        return cantidad * costo;
    }

    /** Indica si el stock llegó al mínimo definido para el producto. */
    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }

    /**
     * Por defecto un producto no vence. La subclase perecedera sobrescribe
     * este método.
     */
    public boolean estaVencido(LocalDate hoy) {
        return false;
    }

    /**
     * Por defecto un producto no ocupa espacio de refrigeración. La subclase
     * perecedera lo sobrescribe cuando el producto necesita frío.
     */
    public boolean usaRefrigeracion() {
        return false;
    }

    /** Unidades que este producto ocupa en las refrigeradoras. */
    public int unidadesRefrigeradas() {
        return usaRefrigeracion() ? cantidad : 0;
    }

    // ------------------------------------------------------------------
    // Métodos que cambian según el tipo de producto (polimorfismo)
    // ------------------------------------------------------------------

    /** Nombre del tipo de producto ("Perecedero" o "No perecedero"). */
    public abstract String getTipo();

    /** Precio al que se vende el producto en la fecha indicada. */
    public abstract double calcularPrecioVenta(LocalDate hoy);

    /** Devuelve true si el producto necesita revisión por su fecha de vencimiento. */
    public abstract boolean requiereAtencion(LocalDate hoy);

    /** Estado del producto en la fecha indicada (VIGENTE, POR VENCER, etc.). */
    public abstract String getEstado(LocalDate hoy);

    /** Dato propio de cada tipo (fecha de vencimiento o estante) en forma de texto. */
    public abstract String getDetalleEspecifico(LocalDate hoy);

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
