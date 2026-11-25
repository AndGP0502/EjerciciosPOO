
package modelo;

public class VersionSoftware {

    private String numero;
    private String fecha;
    private boolean estable;
    private int cantidadCambios;

    public VersionSoftware(String numero, String fecha,
                           boolean estable, int cantidadCambios) {

        this.numero = numero;
        this.fecha = fecha;
        this.estable = estable;

        if (cantidadCambios >= 0) {
            this.cantidadCambios = cantidadCambios;
        } else {
            this.cantidadCambios = 0;
        }
    }

    public String getNumero() {
        return numero;
    }

    public String getFecha() {
        return fecha;
    }

    public boolean isEstable() {
        return estable;
    }

    public int getCantidadCambios() {
        return cantidadCambios;
    }

    public String mostrarInformacion() {

        return "Version: " + numero +
                " | Fecha: " + fecha +
                " | Estable: " + estable +
                " | Cambios: " + cantidadCambios;
    }
}

