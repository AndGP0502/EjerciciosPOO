package modelo;

// Clase base de las reglas del firewall
public class Regla {
    private String nombre;
    private String accion;     // "PERMITIR" o "BLOQUEAR"
    private int prioridad;     // 1 es la prioridad más alta
    private CondicionInterna condicion;

    public Regla(String nombre, String accion, int prioridad, String campo, String valor) {
        this.nombre = nombre;
        this.accion = accion;
        this.prioridad = prioridad;
        // La condición se crea aquí adentro: no existe fuera de la regla (composición)
        this.condicion = new CondicionInterna(campo, valor);
    }

    public String getNombre() {
        return nombre;
    }

    public String getAccion() {
        return accion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public String describirCondicion() {
        return condicion.describir();
    }

    // Dos reglas tienen la misma condición si revisan el mismo campo y valor
    public boolean tieneMismaCondicion(Regla otra) {
        return condicion.esIgualA(otra.condicion);
    }

    // La regla base no coincide con nada; cada hija define su propio criterio
    public boolean coincide(String ipOrigen, int puerto, String protocolo) {
        return false;
    }

    public String mostrarInformacion() {
        return "[Prioridad " + prioridad + "] " + nombre
                + " | Accion: " + accion
                + " | Condicion: " + condicion.describir();
    }
}
