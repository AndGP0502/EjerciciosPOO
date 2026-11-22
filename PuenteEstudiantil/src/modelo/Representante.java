package modelo;

/**
 * Representante legal del estudiante (padre, madre u otro familiar).
 * Es la persona a quien el DECE contacta durante el seguimiento.
 */
public class Representante extends Persona {

    private String parentesco;

    public Representante(String cedula, String nombres, String apellidos, String telefono, String parentesco) {
        super(cedula, nombres, apellidos, telefono);
        this.parentesco = parentesco;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + "\nParentesco: " + parentesco;
    }
}
