package modelo;

// Asociación: el administrador edita reglas que recibe como parámetro,
// pero el firewall y sus reglas no le pertenecen
public class Administrador {
    private String nombre;
    private int cambiosRealizados;

    public Administrador(String nombre) {
        this.nombre = nombre;
        this.cambiosRealizados = 0;
    }

    public int getCambiosRealizados() {
        return cambiosRealizados;
    }

    public String editarRegla(Regla regla, String nuevaAccion, int nuevaPrioridad) {
        regla.setAccion(nuevaAccion);
        regla.setPrioridad(nuevaPrioridad);
        cambiosRealizados++;
        return nombre + " edito la regla '" + regla.getNombre() + "': accion = "
                + nuevaAccion + ", prioridad = " + nuevaPrioridad;
    }
}
