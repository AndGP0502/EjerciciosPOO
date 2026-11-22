package modelo;

/**
 * Profesional del Departamento de Consejería Estudiantil (DECE).
 * Puede ser profesional de atención o coordinador/a del departamento.
 */
public class ProfesionalDece extends Persona {

    public static final String CARGO_PROFESIONAL = "Profesional DECE";
    public static final String CARGO_COORDINADOR = "Coordinador/a DECE";

    private String cargo;
    private String correo;

    public ProfesionalDece(String cedula, String nombres, String apellidos, String telefono,
                           String cargo, String correo) {
        super(cedula, nombres, apellidos, telefono);
        this.cargo = cargo;
        this.correo = correo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean esCoordinador() {
        return CARGO_COORDINADOR.equals(cargo);
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + "\nCargo: " + cargo + "\nCorreo: " + correo;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + cargo + ")";
    }
}
