package pruebas;

import modelo.CasoSeguimiento;
import modelo.Estudiante;
import modelo.Intervencion;
import modelo.ProfesionalDece;
import negocio.GestorCasos;
import negocio.GestorEstudiantes;

import java.time.LocalDate;
import java.util.ArrayList;

import static pruebas.Verificador.crearEstudiante;
import static pruebas.Verificador.esperarError;
import static pruebas.Verificador.titulo;
import static pruebas.Verificador.verificar;

/**
 * Pruebas del Módulo 3: casos de seguimiento (RF-07, RF-08 y RF-09).
 */
public class PruebasModulo3 {

    public static void ejecutar() throws Exception {
        LocalDate hoy = LocalDate.now();
        GestorEstudiantes ge = new GestorEstudiantes();
        Estudiante alto = crearEstudiante("1720000101", 16, 300, 5, true, false, true, false);    // 7 -> Alto
        Estudiante medio = crearEstudiante("1720000102", 14, 300, 5, false, false, false, false); // 3 -> Medio
        Estudiante critico = crearEstudiante("1720000103", 16, 200, 5, true, true, true, true);   // 10 -> Crítico
        Estudiante inactivo = crearEstudiante("1720000104", 15, 300, 5, true, true, false, false);
        ge.registrarEstudiante(alto);
        ge.registrarEstudiante(medio);
        ge.registrarEstudiante(critico);
        ge.registrarEstudiante(inactivo);
        ge.darDeBaja("1720000104", "Traslado", hoy);
        ProfesionalDece ana = new ProfesionalDece("1700000201", "Ana", "Ruiz", "0990000001",
                ProfesionalDece.CARGO_PROFESIONAL, "ana@colegio.edu.ec");
        ProfesionalDece luis = new ProfesionalDece("1700000202", "Luis", "Mora", "0990000002",
                ProfesionalDece.CARGO_PROFESIONAL, "luis@colegio.edu.ec");

        titulo("MÓDULO 3 – RF-07 Abrir caso de seguimiento");
        GestorCasos g = new GestorCasos();
        CasoSeguimiento c1 = g.abrirCaso(alto, "Faltas frecuentes", ana, hoy.minusDays(20), "", "");
        verificar(CasoSeguimiento.ESTADO_ABIERTO.equals(c1.getEstado()) && c1.getNumero() == 1,
                "Riesgo Alto: el caso se abre directamente en estado Abierto");
        esperarError(() -> g.abrirCaso(alto, "Otro motivo", ana, hoy, "", ""), "Solo un caso abierto por estudiante");
        esperarError(() -> g.abrirCaso(medio, "Llega tarde", ana, hoy, "", ""), "Riesgo Medio sin justificación: rechazado");
        CasoSeguimiento c2 = g.abrirCaso(medio, "Llega tarde", luis, hoy.minusDays(5), "", "La tutora lo solicita.");
        verificar(c2 != null && c2.getJustificacion().length() > 0, "Riesgo Medio con justificación: se abre");
        esperarError(() -> g.abrirCaso(critico, "Motivo", null, hoy, "", ""), "Exige profesional responsable");
        esperarError(() -> g.abrirCaso(critico, " ", ana, hoy, "", ""), "Exige motivo de la detección");
        esperarError(() -> g.abrirCaso(critico, "Motivo", ana, hoy.plusDays(2), "", ""), "Rechaza fecha futura");
        esperarError(() -> g.abrirCaso(inactivo, "Motivo", ana, hoy, "", ""), "Rechaza estudiante inactivo");

        titulo("MÓDULO 3 – RF-08 Registrar intervención y cambiar estado");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(19), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "Conversación inicial"), null, null);
        verificar(CasoSeguimiento.ESTADO_EN_SEGUIMIENTO.equals(c1.getEstado()) && c1.getIntervenciones().size() == 1,
                "La primera intervención pasa el caso a En seguimiento");
        esperarError(() -> g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(30), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_EFECTIVA, ""), null, null), "Rechaza intervención anterior a la apertura");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(15), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, ""), null, null);
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(14), Intervencion.TIPO_VISITA_HOGAR,
                Intervencion.RESULTADO_SIN_RESPUESTA, ""), null, null);
        verificar(c1.contarIntentosSinRespuesta() == 2 && CasoSeguimiento.ESTADO_EN_SEGUIMIENTO.equals(c1.getEstado()),
                "Con 2 intentos sin respuesta sigue En seguimiento");
        String tercero = g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(13), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, ""), null, null);
        verificar(CasoSeguimiento.ESTADO_REQUIERE_DERIVACION.equals(c1.getEstado())
                        && g.getAvisosCoordinador().size() == 1 && tercero.contains("coordinador"),
                "Al tercer intento sin respuesta pasa a Requiere derivación y se avisa al coordinador");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(12), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, ""), null, null);
        verificar(g.getAvisosCoordinador().size() == 1, "Un cuarto intento no duplica el aviso");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(10), Intervencion.TIPO_DERIVACION,
                Intervencion.RESULTADO_EFECTIVA, "Derivado a la UDAI"), CasoSeguimiento.ESTADO_DERIVADO, null);
        verificar(CasoSeguimiento.ESTADO_DERIVADO.equals(c1.getEstado()), "Se puede cambiar el estado a Derivado");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(9), Intervencion.TIPO_ENTREVISTA_REPRESENTANTE,
                Intervencion.RESULTADO_EFECTIVA, ""), null, null);
        verificar(c1.contarIntentosSinRespuesta() == 0, "Un contacto efectivo con el representante reinicia el contador");

        esperarError(() -> g.registrarIntervencion(c2, new Intervencion(hoy.minusDays(4), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, ""), CasoSeguimiento.ESTADO_RESUELTO, "Final"),
                "No resuelve un caso sin intervenciones efectivas");
        esperarError(() -> g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(2),
                Intervencion.TIPO_ENTREVISTA_ESTUDIANTE, Intervencion.RESULTADO_EFECTIVA, ""),
                CasoSeguimiento.ESTADO_RESUELTO, "  "), "No resuelve un caso sin observación final");
        g.registrarIntervencion(c1, new Intervencion(hoy.minusDays(2), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "Asiste normalmente"), CasoSeguimiento.ESTADO_RESUELTO,
                "Asiste con regularidad desde hace dos semanas.");
        verificar(!c1.estaAbierto() && c1.getFechaCierre() != null && !c1.getObservacionFinal().isEmpty(),
                "Con intervención efectiva y observación final el caso queda Resuelto (cerrado)");
        esperarError(() -> g.registrarIntervencion(c1, new Intervencion(hoy, Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_EFECTIVA, ""), null, null), "Un caso cerrado no admite intervenciones");
        CasoSeguimiento c3 = g.abrirCaso(alto, "Nueva situación", ana, hoy.minusDays(1), "", "");
        verificar(c3.estaAbierto(), "Cerrado el caso anterior, se puede abrir uno nuevo para el mismo estudiante");

        CasoSeguimiento c4 = g.abrirCaso(critico, "No asiste", luis, hoy.minusDays(8), "", "");
        g.registrarIntervencion(c4, new Intervencion(hoy.minusDays(1), Intervencion.TIPO_VISITA_HOGAR,
                Intervencion.RESULTADO_EFECTIVA, "Se confirma que se fue a trabajar"), CasoSeguimiento.ESTADO_ABANDONO,
                "El estudiante dejó el colegio.");
        verificar(CasoSeguimiento.ESTADO_ABANDONO.equals(c4.getEstado()) && !c4.estaAbierto(),
                "Abandono confirmado también cierra el caso");

        titulo("MÓDULO 3 – RF-09 Consultar historial");
        verificar(g.consultarHistorial("1720000101", null, null, null, "Todos").size() == 2,
                "Filtra por estudiante (cédula): 2 casos");
        verificar(g.consultarHistorial("", luis, null, null, null).size() == 2, "Filtra por profesional");
        verificar(g.consultarHistorial("", null, hoy.minusDays(6), hoy, null).size() == 2,
                "Filtra por rango de fechas de apertura");
        verificar(g.consultarHistorial("", null, null, null, CasoSeguimiento.ESTADO_RESUELTO).size() == 1,
                "Filtra por estado");
        ArrayList<CasoSeguimiento> todos = g.consultarHistorial("", null, null, null, "Todos");
        verificar(todos.size() == 4 && g.contarAbiertos(todos) == 2 && g.contarCerrados(todos) == 2,
                "Totales: 4 casos, 2 abiertos y 2 cerrados");
        verificar(!todos.get(0).getFechaApertura().isAfter(todos.get(1).getFechaApertura()),
                "El historial sale ordenado por fecha de apertura");
        CasoSeguimiento desordenado = c3;
        g.registrarIntervencion(desordenado, new Intervencion(hoy, Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_EFECTIVA, "B"), null, null);
        g.registrarIntervencion(desordenado, new Intervencion(hoy.minusDays(1), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "A"), null, null);
        verificar(desordenado.getIntervencionesOrdenadas().get(0).getObservaciones().equals("A"),
                "Las intervenciones se muestran en orden cronológico aunque se ingresen desordenadas");
    }
}
