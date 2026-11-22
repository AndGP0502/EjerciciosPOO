package modelo;

import java.time.LocalDate;

/**
 * Producto que no tiene fecha de vencimiento corta (arroz, fideos, enlatados,
 * detergente). Se guarda en una percha o estante de la bodega, por eso su
 * dato propio es el estante.
 *
 * Su precio de venta siempre es el precio normal y nunca necesita revisión
 * por vencimiento.
 */
public class ProductoNoPerecedero extends Producto {

    private String estante;

    public ProductoNoPerecedero(String codigo, String nombre, int cantidad, double costo,
                                double precio, int stockMinimo, LocalDate fechaIngreso,
                                String estante) {
        super(codigo, nombre, cantidad, costo, precio, stockMinimo, fechaIngreso);
        setEstante(estante);
    }

    public String getEstante() {
        return estante;
    }

    public void setEstante(String estante) {
        if (estante == null || estante.isBlank()) {
            throw new IllegalArgumentException("El estante es obligatorio.");
        }
        this.estante = estante.trim().toUpperCase();
    }

    @Override
    public String getTipo() {
        return "No perecedero";
    }

    @Override
    public double calcularPrecioVenta(LocalDate hoy) {
        return getPrecio();
    }

    @Override
    public boolean requiereAtencion(LocalDate hoy) {
        return false;
    }

    @Override
    public String getEstado(LocalDate hoy) {
        return "SIN VENCIMIENTO";
    }

    @Override
    public String getDetalleEspecifico(LocalDate hoy) {
        return "Estante " + estante;
    }
}
