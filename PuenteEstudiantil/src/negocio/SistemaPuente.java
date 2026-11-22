package negocio;

import modelo.ProfesionalDece;

import java.util.ArrayList;

/**
 * Punto de acceso a la capa de negocio. Crea un gestor por módulo y los conecta entre sí,
 * para que la interfaz trabaje con una sola instancia compartida de los datos.
 */
public class SistemaPuente {

    private GestorEstudiantes gestorEstudiantes;
    private GestorProgramas gestorProgramas;
    private GestorCasos gestorCasos;
    private GestorAlertas gestorAlertas;
    private GestorRendimiento gestorRendimiento;
    private ArrayList<ProfesionalDece> profesionales;

    public SistemaPuente() {
        gestorEstudiantes = new GestorEstudiantes();
        gestorProgramas = new GestorProgramas();
        gestorCasos = new GestorCasos();
        gestorAlertas = new GestorAlertas(gestorEstudiantes, gestorProgramas, gestorCasos);
        gestorRendimiento = new GestorRendimiento(gestorCasos);
        profesionales = new ArrayList<>();
    }

    public void agregarProfesional(ProfesionalDece profesional) throws PuenteException {
        if (profesional == null || profesional.getNombres() == null || profesional.getNombres().trim().isEmpty()) {
            throw new PuenteException("Complete los datos del profesional.");
        }
        for (ProfesionalDece p : profesionales) {
            if (p.getCedula().equals(profesional.getCedula())) {
                throw new PuenteException("Ya existe un profesional con la cédula " + profesional.getCedula() + ".");
            }
        }
        profesionales.add(profesional);
    }

    public GestorEstudiantes getGestorEstudiantes() {
        return gestorEstudiantes;
    }

    public GestorProgramas getGestorProgramas() {
        return gestorProgramas;
    }

    public GestorCasos getGestorCasos() {
        return gestorCasos;
    }

    public GestorAlertas getGestorAlertas() {
        return gestorAlertas;
    }

    public GestorRendimiento getGestorRendimiento() {
        return gestorRendimiento;
    }

    public ArrayList<ProfesionalDece> getProfesionales() {
        return profesionales;
    }
}
