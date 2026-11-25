package modelo;

public class Tarea {

    private String codigo;
    private String titulo;
    private double horas;
    private boolean completada;

    public Tarea(String codigo, String titulo, double horas) {

        this.codigo = codigo;
        this.titulo = titulo;

        if (horas >= 0) {
            this.horas = horas;
        } else {
            this.horas = 0;
        }

        this.completada = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getHoras() {
        return horas;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void completar() {
        completada = true;
    }

    public String mostrarInformacion() {

        return "Codigo: " + codigo +
                " | Titulo: " + titulo +
                " | Horas: " + horas +
                " | Completada: " + completada;
    }
}
