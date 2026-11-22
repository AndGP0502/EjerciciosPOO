package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Producto que tiene fecha de vencimiento (lácteos, pan, embutidos, jugos).
 *
 * Hereda los datos comunes de {@link Producto} y agrega la fecha de
 * vencimiento y si necesita refrigeración. Además implementa a su manera el
 * cálculo del precio: mientras más cerca está de vencer, más barato se vende,
 * porque es mejor recuperar parte de la inversión que botar el producto.
 */
public class ProductoPerecedero extends Producto {

    /** Desde estos días antes del vencimiento el producto entra en alerta. */
    public static final int DIAS_ALERTA = 7;

    /** Desde estos días antes del vencimiento el descuento es mayor. */
    public static final int DIAS_DESCUENTO_FUERTE = 3;

    public static final double DESCUENTO_MEDIO = 0.20;   // entre 4 y 7 días
    public static final double DESCUENTO_FUERTE = 0.40;  // 3 días o menos

    private LocalDate fechaVencimiento;
    private boolean requiereRefrigeracion;

    public ProductoPerecedero(String codigo, String nombre, int cantidad, double costo,
                              double precio, int stockMinimo, LocalDate fechaIngreso,
                              LocalDate fechaVencimiento, boolean requiereRefrigeracion) {
        super(codigo, nombre, cantidad, costo, precio, stockMinimo, fechaIngreso);
        setFechaVencimiento(fechaVencimiento);
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        }
        if (fechaVencimiento.isBefore(getFechaIngreso())) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento es anterior a la de ingreso: el producto ya está vencido.");
        }
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    /** Días que faltan para vencer. Si ya venció, el resultado es negativo. */
    public long diasParaVencer(LocalDate hoy) {
        return ChronoUnit.DAYS.between(hoy, fechaVencimiento);
    }

    @Override
    public boolean estaVencido(LocalDate hoy) {
        return diasParaVencer(hoy) < 0;
    }

    @Override
    public boolean usaRefrigeracion() {
        return requiereRefrigeracion;
    }

    @Override
    public String getTipo() {
        return "Perecedero";
    }

    /**
     * Precio con descuento según la cercanía al vencimiento:
     * vencido = 0 (no se vende), 3 días o menos = 40 % menos,
     * de 4 a 7 días = 20 % menos, más de 7 días = precio normal.
     */
    @Override
    public double calcularPrecioVenta(LocalDate hoy) {
        long dias = diasParaVencer(hoy);
        double precioFinal;

        if (dias < 0) {
            precioFinal = 0;
        } else if (dias <= DIAS_DESCUENTO_FUERTE) {
            precioFinal = getPrecio() * (1 - DESCUENTO_FUERTE);
        } else if (dias <= DIAS_ALERTA) {
            precioFinal = getPrecio() * (1 - DESCUENTO_MEDIO);
        } else {
            precioFinal = getPrecio();
        }
        return Math.round(precioFinal * 100.0) / 100.0;
    }

    @Override
    public boolean requiereAtencion(LocalDate hoy) {
        return diasParaVencer(hoy) <= DIAS_ALERTA;
    }

    @Override
    public String getEstado(LocalDate hoy) {
        if (estaVencido(hoy)) {
            return "VENCIDO";
        }
        if (requiereAtencion(hoy)) {
            return "POR VENCER";
        }
        return "VIGENTE";
    }

    @Override
    public String getDetalleEspecifico(LocalDate hoy) {
        long dias = diasParaVencer(hoy);
        String texto;

        if (dias < 0) {
            texto = "VENCIDO (" + fechaVencimiento.format(FORMATO_FECHA) + ")";
        } else if (dias == 0) {
            texto = "Vence hoy";
        } else {
            texto = "Vence " + fechaVencimiento.format(FORMATO_FECHA)
                    + " (" + dias + (dias == 1 ? " día)" : " días)");
        }
        return requiereRefrigeracion ? texto + ", frío" : texto;
    }
}
