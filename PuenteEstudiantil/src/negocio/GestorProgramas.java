package negocio;

import modelo.AsignacionApoyo;
import modelo.Estudiante;
import modelo.ProgramaApoyo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Módulo 2 (Mateo Salgado): programas de apoyo y asignación a estudiantes.
 * Cubre RF-04 (registrar programa), RF-05 (consultar, actualizar y cerrar) y RF-06 (asignar apoyo).
 */
public class GestorProgramas {

    public static final String[] CRITERIOS_BUSQUEDA = {"Nombre", "Tipo", "Estado"};

    private ArrayList<ProgramaApoyo> programas;
    private int siguienteCodigo;

    public GestorProgramas() {
        programas = new ArrayList<>();
        siguienteCodigo = 1;
    }

    // ------------------------------------------------------------------
    // RF-04: registrar programa de apoyo
    // ------------------------------------------------------------------

    /**
     * Crea un programa en estado Vigente. Reglas: cupos mayores que cero, fecha de fin posterior
     * a la de inicio y ningún otro programa vigente con el mismo nombre en el mismo periodo.
     */
    public String registrarPrograma(ProgramaApoyo programa) throws PuenteException {
        validarPrograma(programa, null);
        programa.setCodigo(siguienteCodigo);
        siguienteCodigo++;
        programas.add(programa);
        return "Programa «" + programa.getNombre() + "» creado en estado Vigente con " + programa.getCupos()
                + " cupos.";
    }

    // ------------------------------------------------------------------
    // RF-05: consultar, actualizar y cerrar programas
    // ------------------------------------------------------------------

    /** Actualiza un programa vigente. No permite bajar los cupos por debajo de los ya asignados. */
    public String actualizarPrograma(int codigo, ProgramaApoyo datosNuevos) throws PuenteException {
        ProgramaApoyo programa = obtenerPrograma(codigo);
        if (!programa.estaVigente()) {
            throw new PuenteException("No se puede modificar un programa cerrado.");
        }
        validarPrograma(datosNuevos, programa);
        int asignados = programa.contarAsignacionesVigentes();
        if (datosNuevos.getCupos() < asignados) {
            throw new PuenteException("No se pueden reducir los cupos por debajo de los ya asignados ("
                    + asignados + ").");
        }
        programa.setNombre(datosNuevos.getNombre());
        programa.setTipo(datosNuevos.getTipo());
        programa.setDescripcion(datosNuevos.getDescripcion());
        programa.setCupos(datosNuevos.getCupos());
        programa.setFechaInicio(datosNuevos.getFechaInicio());
        programa.setFechaFin(datosNuevos.getFechaFin());
        programa.setNivelMinimo(datosNuevos.getNivelMinimo());
        return "Programa actualizado." + proponerPrimeroEnEspera(programa);
    }

    /** Cierra el programa: las asignaciones vigentes pasan a Finalizada y la lista de espera se cancela. */
    public String cerrarPrograma(int codigo) throws PuenteException {
        ProgramaApoyo programa = obtenerPrograma(codigo);
        if (!programa.estaVigente()) {
            throw new PuenteException("El programa ya está cerrado.");
        }
        int finalizadas = 0;
        int canceladas = 0;
        for (AsignacionApoyo a : programa.getAsignaciones()) {
            if (a.estaVigente()) {
                a.finalizar();
                finalizadas++;
            } else if (a.estaEnEspera()) {
                a.cancelar();
                canceladas++;
            }
        }
        programa.cerrar();
        return "Programa «" + programa.getNombre() + "» cerrado. Apoyos entregados (finalizados): " + finalizadas
                + ". Solicitudes en espera canceladas: " + canceladas + ".";
    }

    /** Busca programas por nombre, tipo o estado. Sin texto devuelve todos. */
    public ArrayList<ProgramaApoyo> buscar(String criterio, String texto) {
        ArrayList<ProgramaApoyo> resultado = new ArrayList<>();
        String buscado = texto == null ? "" : texto.trim().toLowerCase();
        for (ProgramaApoyo p : programas) {
            boolean coincide;
            if (buscado.isEmpty()) {
                coincide = true;
            } else if ("Tipo".equals(criterio)) {
                coincide = p.getTipo().toLowerCase().contains(buscado);
            } else if ("Estado".equals(criterio)) {
                coincide = p.getEstado().equalsIgnoreCase(buscado);
            } else {
                coincide = p.getNombre().toLowerCase().contains(buscado);
            }
            if (coincide) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // ------------------------------------------------------------------
    // RF-06: asignar apoyo a estudiante [Regla de negocio]
    // ------------------------------------------------------------------

    /**
     * Asigna el programa solo si: el estudiante está Activo, su riesgo es igual o mayor al mínimo,
     * el programa está Vigente y no tiene ya una asignación vigente o en espera del mismo programa.
     * Si no quedan cupos, el estudiante entra a la lista de espera.
     */
    public String asignarApoyo(Estudiante estudiante, ProgramaApoyo programa, LocalDate fecha)
            throws PuenteException {
        if (estudiante == null || programa == null || fecha == null) {
            throw new PuenteException("Seleccione el estudiante, el programa y una fecha válida.");
        }
        if (!estudiante.estaActivo()) {
            throw new PuenteException("El estudiante no está activo.");
        }
        if (!programa.estaVigente()) {
            throw new PuenteException("El programa no está vigente.");
        }
        if (fecha.isAfter(programa.getFechaFin())) {
            throw new PuenteException("El programa ya terminó su vigencia.");
        }
        if (Estudiante.valorNivel(estudiante.getNivelRiesgo()) < Estudiante.valorNivel(programa.getNivelMinimo())) {
            throw new PuenteException("El nivel de riesgo del estudiante (" + estudiante.getNivelRiesgo()
                    + ") es menor que el mínimo del programa (" + programa.getNivelMinimo() + ").");
        }
        AsignacionApoyo existente = buscarAsignacionActiva(estudiante, programa);
        if (existente != null && existente.estaVigente()) {
            throw new PuenteException("El estudiante ya tiene una asignación vigente de este programa.");
        }
        if (existente != null) {
            throw new PuenteException("El estudiante ya está en la lista de espera de este programa (posición "
                    + obtenerPosicionEnEspera(estudiante, programa) + ").");
        }

        if (programa.getCuposDisponibles() > 0) {
            programa.agregarAsignacion(new AsignacionApoyo(estudiante, programa, fecha, AsignacionApoyo.ESTADO_VIGENTE));
            return "Apoyo asignado a " + estudiante.getNombreCompleto() + ". Cupos restantes: "
                    + programa.getCuposDisponibles() + ".";
        }
        programa.agregarAsignacion(new AsignacionApoyo(estudiante, programa, fecha, AsignacionApoyo.ESTADO_EN_ESPERA));
        return "No quedan cupos. " + estudiante.getNombreCompleto() + " entró a la lista de espera en la posición "
                + obtenerPosicionEnEspera(estudiante, programa) + ".";
    }

    /** Finaliza una asignación vigente y propone el cupo liberado al primero de la lista de espera. */
    public String finalizarAsignacion(AsignacionApoyo asignacion) throws PuenteException {
        if (asignacion == null || !asignacion.estaVigente()) {
            throw new PuenteException("Solo se pueden finalizar asignaciones vigentes.");
        }
        asignacion.finalizar();
        return "Asignación finalizada." + proponerPrimeroEnEspera(asignacion.getPrograma());
    }

    /**
     * Entrega un cupo libre al primero de la lista de espera. Si alguien de la lista
     * ya no está activo, su solicitud se cancela y se pasa al siguiente.
     */
    public String asignarPrimeroEnEspera(ProgramaApoyo programa, LocalDate fecha) throws PuenteException {
        if (programa == null || !programa.estaVigente()) {
            throw new PuenteException("El programa no está vigente.");
        }
        if (programa.getCuposDisponibles() <= 0) {
            throw new PuenteException("El programa no tiene cupos disponibles.");
        }
        for (AsignacionApoyo a : programa.getListaEspera()) {
            if (a.getEstudiante().estaActivo()) {
                a.activar(fecha);
                return "Se asignó el cupo a " + a.getEstudiante().getNombreCompleto() + " (nivel "
                        + a.getEstudiante().getNivelRiesgo() + ").";
            }
            a.cancelar();
        }
        throw new PuenteException("No hay estudiantes activos en la lista de espera.");
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo
    // ------------------------------------------------------------------

    public int obtenerPosicionEnEspera(Estudiante estudiante, ProgramaApoyo programa) {
        ArrayList<AsignacionApoyo> espera = programa.getListaEspera();
        for (int i = 0; i < espera.size(); i++) {
            if (espera.get(i).getEstudiante() == estudiante) {
                return i + 1;
            }
        }
        return 0;
    }

    /** Busca una asignación vigente o en espera del estudiante en ese programa (o null). */
    public AsignacionApoyo buscarAsignacionActiva(Estudiante estudiante, ProgramaApoyo programa) {
        for (AsignacionApoyo a : programa.getAsignaciones()) {
            if (a.getEstudiante() == estudiante && (a.estaVigente() || a.estaEnEspera())) {
                return a;
            }
        }
        return null;
    }

    /** Lo usa el Módulo 4 para saber si un estudiante ya recibe algún apoyo. */
    public boolean tieneApoyoVigente(Estudiante estudiante) {
        for (ProgramaApoyo p : programas) {
            for (AsignacionApoyo a : p.getAsignaciones()) {
                if (a.getEstudiante() == estudiante && a.estaVigente()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ProgramaApoyo obtenerPrograma(int codigo) throws PuenteException {
        for (ProgramaApoyo p : programas) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        throw new PuenteException("No existe el programa con código " + codigo + ".");
    }

    private String proponerPrimeroEnEspera(ProgramaApoyo programa) {
        ArrayList<AsignacionApoyo> espera = programa.getListaEspera();
        if (programa.getCuposDisponibles() > 0 && !espera.isEmpty()) {
            Estudiante e = espera.get(0).getEstudiante();
            return " Hay un cupo libre: se propone asignarlo a " + e.getNombreCompleto() + " (nivel "
                    + e.getNivelRiesgo() + "), primero de la lista de espera.";
        }
        return "";
    }

    private void validarPrograma(ProgramaApoyo p, ProgramaApoyo actual) throws PuenteException {
        if (p == null || estaVacio(p.getNombre()) || estaVacio(p.getTipo()) || estaVacio(p.getNivelMinimo())) {
            throw new PuenteException("Complete el nombre, el tipo y el nivel mínimo de riesgo.");
        }
        if (p.getFechaInicio() == null || p.getFechaFin() == null) {
            throw new PuenteException("Ingrese fechas de inicio y fin válidas.");
        }
        if (p.getCupos() <= 0) {
            throw new PuenteException("Los cupos deben ser mayores que cero.");
        }
        if (!p.getFechaFin().isAfter(p.getFechaInicio())) {
            throw new PuenteException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }
        for (ProgramaApoyo otro : programas) {
            if (otro != actual && otro.estaVigente() && otro.getNombre().equalsIgnoreCase(p.getNombre().trim())
                    && otro.seCruzaCon(p.getFechaInicio(), p.getFechaFin())) {
                throw new PuenteException("Ya existe un programa vigente llamado «" + otro.getNombre()
                        + "» en ese periodo.");
            }
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public ArrayList<ProgramaApoyo> getProgramas() {
        return programas;
    }
}
