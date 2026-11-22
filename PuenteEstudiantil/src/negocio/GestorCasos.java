package negocio;

import modelo.CasoSeguimiento;
import modelo.Estudiante;
import modelo.Intervencion;
import modelo.ProfesionalDece;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Módulo 3 (José Ayora): casos de seguimiento e intervenciones.
 * Cubre RF-07 (abrir caso), RF-08 (registrar intervención y cambiar estado) y RF-09 (consultar historial).
 */
public class GestorCasos {

    public static final int INTENTOS_PARA_DERIVAR = 3;

    private ArrayList<CasoSeguimiento> casos;
    private ArrayList<String> avisosCoordinador;
    private int siguienteNumero;

    public GestorCasos() {
        casos = new ArrayList<>();
        avisosCoordinador = new ArrayList<>();
        siguienteNumero = 1;
    }

    // ------------------------------------------------------------------
    // RF-07: abrir caso de seguimiento [Regla de negocio]
    // ------------------------------------------------------------------

    /**
     * Abre un caso de forma directa si el riesgo es Alto o Crítico; si es Bajo o Medio
     * se necesita una justificación. Solo puede haber un caso abierto por estudiante.
     */
    public CasoSeguimiento abrirCaso(Estudiante estudiante, String motivo, ProfesionalDece profesional,
                                     LocalDate fecha, String observaciones, String justificacion)
            throws PuenteException {
        if (estudiante == null) {
            throw new PuenteException("Seleccione un estudiante.");
        }
        if (!estudiante.estaActivo()) {
            throw new PuenteException("El estudiante no está activo.");
        }
        if (estaVacio(motivo)) {
            throw new PuenteException("Ingrese el motivo de la detección.");
        }
        if (profesional == null) {
            throw new PuenteException("Seleccione el profesional del DECE responsable.");
        }
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new PuenteException("La fecha de apertura no es válida (no puede ser futura).");
        }
        CasoSeguimiento abierto = buscarCasoAbierto(estudiante);
        if (abierto != null) {
            throw new PuenteException("El estudiante ya tiene un caso abierto (caso N.° " + abierto.getNumero() + ").");
        }
        if (!estudiante.tieneRiesgoAltoOCritico() && estaVacio(justificacion)) {
            throw new PuenteException("El riesgo del estudiante es " + estudiante.getNivelRiesgo()
                    + ": se requiere una justificación del profesional para abrir el caso.");
        }
        CasoSeguimiento caso = new CasoSeguimiento(siguienteNumero, estudiante, profesional, motivo.trim(), fecha,
                observaciones, justificacion);
        siguienteNumero++;
        casos.add(caso);
        return caso;
    }

    // ------------------------------------------------------------------
    // RF-08: registrar intervención y cambiar estado del caso [Regla de negocio]
    // ------------------------------------------------------------------

    /**
     * Guarda la intervención en el historial y actualiza el estado. Reglas:
     * - Con 3 intentos seguidos de contacto con el representante sin respuesta, el caso pasa a
     *   "Requiere derivación" y se avisa al coordinador.
     * - Solo pasa a "Resuelto" si tiene al menos una intervención efectiva y una observación final.
     */
    public String registrarIntervencion(CasoSeguimiento caso, Intervencion intervencion, String nuevoEstado,
                                        String observacionFinal) throws PuenteException {
        if (caso == null || intervencion == null) {
            throw new PuenteException("Seleccione un caso y complete la intervención.");
        }
        if (!caso.estaAbierto()) {
            throw new PuenteException("El caso está cerrado (" + caso.getEstado()
                    + ") y no admite nuevas intervenciones.");
        }
        if (estaVacio(intervencion.getTipo()) || estaVacio(intervencion.getResultado())
                || intervencion.getFecha() == null) {
            throw new PuenteException("Complete el tipo, la fecha y el resultado de la intervención.");
        }
        if (intervencion.getFecha().isBefore(caso.getFechaApertura())) {
            throw new PuenteException("La fecha de la intervención no puede ser anterior a la apertura del caso.");
        }
        if (intervencion.getFecha().isAfter(LocalDate.now())) {
            throw new PuenteException("La fecha de la intervención no puede ser futura.");
        }
        String estadoFinal = estaVacio(nuevoEstado) ? caso.getEstado() : nuevoEstado;
        if (CasoSeguimiento.ESTADO_RESUELTO.equals(estadoFinal)) {
            if (!caso.tieneIntervencionEfectiva() && !intervencion.esEfectiva()) {
                throw new PuenteException("Para resolver el caso se necesita al menos una intervención efectiva.");
            }
            if (estaVacio(observacionFinal)) {
                throw new PuenteException("Para resolver el caso se necesita una observación final.");
            }
        }

        caso.agregarIntervencion(intervencion);
        String texto = "Intervención registrada en el caso N.° " + caso.getNumero() + ".";

        // Un caso que ya tiene intervenciones deja de estar solo "Abierto"
        if (CasoSeguimiento.ESTADO_ABIERTO.equals(estadoFinal)) {
            estadoFinal = CasoSeguimiento.ESTADO_EN_SEGUIMIENTO;
        }
        // Regla de los 3 intentos sin respuesta (no se aplica si el caso se deriva o se cierra)
        boolean puedeDerivarse = CasoSeguimiento.ESTADO_EN_SEGUIMIENTO.equals(estadoFinal)
                || CasoSeguimiento.ESTADO_REQUIERE_DERIVACION.equals(estadoFinal);
        if (puedeDerivarse && caso.contarIntentosSinRespuesta() >= INTENTOS_PARA_DERIVAR) {
            if (!CasoSeguimiento.ESTADO_REQUIERE_DERIVACION.equals(caso.getEstado())) {
                avisosCoordinador.add(intervencion.getFecha() + " | Caso N.° " + caso.getNumero() + " ("
                        + caso.getEstudiante().getNombreCompleto() + "): " + INTENTOS_PARA_DERIVAR
                        + " intentos de contacto con el representante sin respuesta. Requiere derivación.");
                texto += " Hay " + INTENTOS_PARA_DERIVAR
                        + " intentos seguidos sin respuesta del representante: se avisó al coordinador.";
            }
            estadoFinal = CasoSeguimiento.ESTADO_REQUIERE_DERIVACION;
        }

        if (CasoSeguimiento.ESTADO_RESUELTO.equals(estadoFinal) || CasoSeguimiento.ESTADO_ABANDONO.equals(estadoFinal)) {
            caso.cerrar(estadoFinal, observacionFinal == null ? "" : observacionFinal.trim(), intervencion.getFecha());
            if (CasoSeguimiento.ESTADO_ABANDONO.equals(estadoFinal)) {
                texto += " Recuerde dar de baja al estudiante con el motivo «Abandono confirmado».";
            }
        } else {
            caso.setEstado(estadoFinal);
        }
        return texto + " Estado del caso: " + caso.getEstado() + ".";
    }

    // ------------------------------------------------------------------
    // RF-09: consultar historial de casos e intervenciones
    // ------------------------------------------------------------------

    /**
     * Filtra los casos por estudiante (cédula o nombre), profesional, rango de fechas de apertura
     * y estado. Los filtros vacíos (null o "Todos") no se aplican. El resultado sale ordenado por fecha.
     */
    public ArrayList<CasoSeguimiento> consultarHistorial(String textoEstudiante, ProfesionalDece profesional,
                                                         LocalDate desde, LocalDate hasta, String estado) {
        ArrayList<CasoSeguimiento> resultado = new ArrayList<>();
        String buscado = textoEstudiante == null ? "" : textoEstudiante.trim().toLowerCase();
        for (CasoSeguimiento c : casos) {
            Estudiante e = c.getEstudiante();
            boolean coincideEstudiante = buscado.isEmpty() || e.getCedula().contains(buscado)
                    || e.getNombreCompleto().toLowerCase().contains(buscado);
            boolean coincideProfesional = profesional == null || c.getProfesional() == profesional;
            boolean coincideDesde = desde == null || !c.getFechaApertura().isBefore(desde);
            boolean coincideHasta = hasta == null || !c.getFechaApertura().isAfter(hasta);
            boolean coincideEstado = estaVacio(estado) || "Todos".equals(estado) || c.getEstado().equals(estado);
            if (coincideEstudiante && coincideProfesional && coincideDesde && coincideHasta && coincideEstado) {
                int posicion = 0;
                while (posicion < resultado.size()
                        && !c.getFechaApertura().isBefore(resultado.get(posicion).getFechaApertura())) {
                    posicion++;
                }
                resultado.add(posicion, c);
            }
        }
        return resultado;
    }

    public int contarAbiertos(ArrayList<CasoSeguimiento> lista) {
        int total = 0;
        for (CasoSeguimiento c : lista) {
            if (c.estaAbierto()) {
                total++;
            }
        }
        return total;
    }

    public int contarCerrados(ArrayList<CasoSeguimiento> lista) {
        return lista.size() - contarAbiertos(lista);
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo
    // ------------------------------------------------------------------

    /** Devuelve el caso abierto del estudiante o null si no tiene. También lo usa el Módulo 4. */
    public CasoSeguimiento buscarCasoAbierto(Estudiante estudiante) {
        for (CasoSeguimiento c : casos) {
            if (c.getEstudiante() == estudiante && c.estaAbierto()) {
                return c;
            }
        }
        return null;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public ArrayList<CasoSeguimiento> getCasos() {
        return casos;
    }

    public ArrayList<String> getAvisosCoordinador() {
        return avisosCoordinador;
    }
}
