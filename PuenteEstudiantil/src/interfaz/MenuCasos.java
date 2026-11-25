package interfaz;

import modelo.CasoSeguimiento;
import modelo.Estudiante;
import modelo.Intervencion;
import modelo.ProfesionalDece;
import negocio.GestorCasos;
import negocio.PuenteException;
import negocio.SistemaPuente;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú del Módulo 3 (José Ayora): apertura de casos (RF-07), registro de intervenciones
 * (RF-08) y consulta del historial con filtros (RF-09).
 */
public class MenuCasos {

    private SistemaPuente sistema;
    private GestorCasos gestor;

    public MenuCasos(SistemaPuente sistema) {
        this.sistema = sistema;
        gestor = sistema.getGestorCasos();
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("3. CASOS DE SEGUIMIENTO");
            System.out.println("1. Ver todos los casos");
            System.out.println("2. Consultar historial con filtros");
            System.out.println("3. Abrir caso");
            System.out.println("4. Registrar intervención");
            System.out.println("5. Ver detalle de un caso");
            System.out.println("6. Avisos al coordinador (" + gestor.getAvisosCoordinador().size() + ")");
            System.out.println("0. Volver al menú principal");
            opcion = Consola.leerEntero("Elija una opción");

            switch (opcion) {
                case 1:
                    mostrarHistorial(gestor.consultarHistorial("", null, null, null, "Todos"));
                    break;
                case 2:
                    consultarHistorial();
                    break;
                case 3:
                    abrirCaso();
                    break;
                case 4:
                    registrarIntervencion();
                    break;
                case 5:
                    verDetalle();
                    break;
                case 6:
                    verAvisos();
                    break;
                case 0:
                    break;
                default:
                    Consola.mostrarError("Opción no válida.");
            }
            if (opcion != 0) {
                Consola.pausar();
            }
        } while (opcion != 0);
    }

    /** Nombres de los profesionales en el mismo orden de la lista del sistema. */
    private String[] nombresProfesionales() {
        ArrayList<ProfesionalDece> profesionales = sistema.getProfesionales();
        String[] nombres = new String[profesionales.size()];
        for (int i = 0; i < profesionales.size(); i++) {
            nombres[i] = profesionales.get(i).toString();
        }
        return nombres;
    }

    /** RF-09: pide los filtros (Enter o "Todos" para no filtrar) y muestra los casos que los cumplen. */
    private void consultarHistorial() {
        String textoEstudiante = Consola.leerTexto("Estudiante (nombre o cédula, Enter para todos)");

        // La opción 1 es "Todos"; las demás siguen el orden de la lista de profesionales
        String[] nombres = nombresProfesionales();
        String[] opcionesProfesional = new String[nombres.length + 1];
        opcionesProfesional[0] = "Todos";
        for (int i = 0; i < nombres.length; i++) {
            opcionesProfesional[i + 1] = nombres[i];
        }
        int posicion = Consola.elegirPosicion("Profesional", opcionesProfesional, null);
        ProfesionalDece profesional = null;
        if (posicion > 0) {
            profesional = sistema.getProfesionales().get(posicion - 1);
        }

        String[] opcionesEstado = new String[CasoSeguimiento.ESTADOS.length + 1];
        opcionesEstado[0] = "Todos";
        for (int i = 0; i < CasoSeguimiento.ESTADOS.length; i++) {
            opcionesEstado[i + 1] = CasoSeguimiento.ESTADOS[i];
        }
        String estado = Consola.elegirOpcion("Estado", opcionesEstado);

        LocalDate desde = Consola.leerFechaOpcional("Apertura desde");
        LocalDate hasta = Consola.leerFechaOpcional("Apertura hasta");

        mostrarHistorial(gestor.consultarHistorial(textoEstudiante, profesional, desde, hasta, estado));
    }

    /** Muestra cada caso con sus intervenciones en orden de fecha. */
    private void mostrarHistorial(ArrayList<CasoSeguimiento> lista) {
        System.out.println();
        System.out.println("HISTORIAL DE CASOS (RF-09)");
        System.out.println();
        for (CasoSeguimiento c : lista) {
            System.out.println("CASO N.° " + c.getNumero() + " – " + c.getEstudiante().getNombreCompleto() + " – "
                    + c.getEstudiante().getCursoCompleto() + " – Riesgo " + c.getEstudiante().getNivelRiesgo()
                    + " – Estado: " + c.getEstado() + " – Profesional: " + c.getProfesional().getNombreCompleto());
            System.out.println("  " + Consola.formatear(c.getFechaApertura()) + "  Apertura del caso: "
                    + c.getMotivoDeteccion());
            for (Intervencion i : c.getIntervencionesOrdenadas()) {
                System.out.println("  " + Consola.formatear(i.getFecha()) + "  " + i.getTipo() + " – "
                        + i.getResultado() + ": " + i.getObservaciones());
            }
            if (!c.estaAbierto()) {
                System.out.println("  " + Consola.formatear(c.getFechaCierre()) + "  Cierre (" + c.getEstado() + "): "
                        + c.getObservacionFinal());
            }
            System.out.println();
        }
        System.out.println("Total de casos: " + lista.size() + " | Abiertos: " + gestor.contarAbiertos(lista)
                + " | Cerrados: " + gestor.contarCerrados(lista));
    }

    /** Pide el número de caso y lo busca. Devuelve null y avisa si no existe. */
    private CasoSeguimiento leerCaso() {
        int numero = Consola.leerEntero("Número de caso");
        for (CasoSeguimiento c : gestor.getCasos()) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        Consola.mostrarError("No existe el caso N.° " + numero + ".");
        return null;
    }

    private void abrirCaso() {
        System.out.println("ABRIR CASO DE SEGUIMIENTO (RF-07)");
        try {
            Estudiante estudiante = sistema.getGestorEstudiantes().obtenerEstudiante(
                    Consola.leerTexto("Cédula del estudiante"));
            System.out.println("Estudiante: " + estudiante.getNombreCompleto() + " – riesgo " + estudiante.getNivelRiesgo());
            String motivo = Consola.leerTexto("Motivo de la detección");
            int posicion = Consola.elegirPosicion("Profesional responsable", nombresProfesionales(), null);
            ProfesionalDece profesional = sistema.getProfesionales().get(posicion);
            LocalDate fecha = Consola.leerFecha("Fecha de apertura", Consola.formatear(LocalDate.now()));
            String observaciones = Consola.leerTexto("Observaciones (opcional)");
            // Si el riesgo es Bajo o Medio, el gestor exige una justificación
            String justificacion = "";
            if (Estudiante.NIVEL_BAJO.equals(estudiante.getNivelRiesgo())
                    || Estudiante.NIVEL_MEDIO.equals(estudiante.getNivelRiesgo())) {
                justificacion = Consola.leerTexto("Justificación (el riesgo es " + estudiante.getNivelRiesgo() + ")");
            }
            CasoSeguimiento caso = gestor.abrirCaso(estudiante, motivo, profesional, fecha, observaciones, justificacion);
            System.out.println("Caso N.° " + caso.getNumero() + " abierto para " + estudiante.getNombreCompleto()
                    + " (riesgo " + estudiante.getNivelRiesgo() + ") en estado " + caso.getEstado() + ".");
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void registrarIntervencion() {
        CasoSeguimiento c = leerCaso();
        if (c == null) {
            return;
        }
        if (!c.estaAbierto()) {
            Consola.mostrarError("El caso está cerrado (" + c.getEstado() + ") y no admite nuevas intervenciones.");
            return;
        }
        System.out.println("REGISTRAR INTERVENCIÓN (RF-08)");
        System.out.println("Caso N.° " + c.getNumero() + " – " + c.getEstudiante().getNombreCompleto()
                + " – Estado actual: " + c.getEstado()
                + " (intentos sin respuesta: " + c.contarIntentosSinRespuesta() + ")");

        String tipo = Consola.elegirOpcion("Tipo de intervención", Intervencion.TIPOS);
        LocalDate fecha = Consola.leerFecha("Fecha", Consola.formatear(LocalDate.now()));
        String resultado = Consola.elegirOpcion("Resultado", Intervencion.RESULTADOS);
        String observaciones = Consola.leerTexto("Observaciones");

        // Si el caso recién se abrió, lo normal es que pase a "En seguimiento"
        String estadoSugerido = c.getEstado();
        if (CasoSeguimiento.ESTADO_ABIERTO.equals(c.getEstado())) {
            estadoSugerido = CasoSeguimiento.ESTADO_EN_SEGUIMIENTO;
        }
        String nuevoEstado = Consola.elegirOpcion("Nuevo estado del caso", CasoSeguimiento.ESTADOS, estadoSugerido);
        String observacionFinal = Consola.leerTexto("Observación final (si se cierra el caso; Enter para omitir)");

        try {
            Intervencion intervencion = new Intervencion(fecha, tipo, resultado, observaciones);
            System.out.println(gestor.registrarIntervencion(c, intervencion, nuevoEstado, observacionFinal));
            mostrarDetalle(c);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void verDetalle() {
        CasoSeguimiento c = leerCaso();
        if (c != null) {
            mostrarDetalle(c);
        }
    }

    private void mostrarDetalle(CasoSeguimiento c) {
        System.out.println();
        System.out.println("DETALLE DEL CASO N.° " + c.getNumero());
        System.out.println("Estudiante: " + c.getEstudiante().getNombreCompleto() + " (" + c.getEstudiante().getCedula() + ")");
        System.out.println("Curso: " + c.getEstudiante().getCursoCompleto() + " – Riesgo: " + c.getEstudiante().getNivelRiesgo());
        System.out.println("Profesional responsable: " + c.getProfesional());
        System.out.println("Fecha de apertura: " + Consola.formatear(c.getFechaApertura()));
        System.out.println("Motivo de la detección: " + c.getMotivoDeteccion());
        System.out.println("Estado: " + c.getEstado());
        System.out.println("Intentos seguidos sin respuesta del representante: " + c.contarIntentosSinRespuesta());
        if (c.getJustificacion() != null && !c.getJustificacion().isEmpty()) {
            System.out.println("Justificación (riesgo Bajo o Medio): " + c.getJustificacion());
        }
        if (c.getObservaciones() != null && !c.getObservaciones().isEmpty()) {
            System.out.println("Observaciones: " + c.getObservaciones());
        }
        System.out.println();
        System.out.println("INTERVENCIONES (de la más antigua a la más reciente)");
        for (Intervencion i : c.getIntervencionesOrdenadas()) {
            System.out.println("  " + Consola.formatear(i.getFecha()) + "  " + i.getTipo() + " – "
                    + i.getResultado() + ": " + i.getObservaciones());
        }
        if (!c.estaAbierto()) {
            System.out.println();
            System.out.println("Fecha de cierre: " + Consola.formatear(c.getFechaCierre()));
            System.out.println("Observación final: " + c.getObservacionFinal());
        }
    }

    private void verAvisos() {
        ArrayList<String> avisos = gestor.getAvisosCoordinador();
        System.out.println("AVISOS AL COORDINADOR");
        System.out.println();
        if (avisos.isEmpty()) {
            System.out.println("No hay avisos para el coordinador.");
        }
        for (String aviso : avisos) {
            System.out.println("- " + aviso);
        }
    }
}
