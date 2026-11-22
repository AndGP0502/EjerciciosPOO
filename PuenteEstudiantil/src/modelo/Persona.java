package modelo;

/**
 * Clase base con los datos comunes de las personas del sistema:
 * estudiantes, representantes y profesionales del DECE.
 */
public class Persona {

    private String cedula;
    private String nombres;
    private String apellidos;
    private String telefono;

    public Persona(String cedula, String nombres, String apellidos, String telefono) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // Las clases hijas sobrescriben este método para agregar sus propios datos
    public String mostrarDatos() {
        return "Cédula: " + cedula + "\nNombre: " + getNombreCompleto() + "\nTeléfono: " + telefono;
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
