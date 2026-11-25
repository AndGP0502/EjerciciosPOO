package modelo;

public class Proyecto {

    private String codigo;
    private String nombre;
    private double presupuesto;

    public Proyecto(String codigo, String nombre, double presupuesto) {

        this.codigo = codigo;
        this.nombre = nombre;

        if (presupuesto >= 0) {
            this.presupuesto = presupuesto;
        } else {
            this.presupuesto = 0;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public String mostrarInformacion() {

        return "Codigo: " + codigo +
                " | Nombre: " + nombre +
                " | Presupuesto: $" + presupuesto;
    }
}
