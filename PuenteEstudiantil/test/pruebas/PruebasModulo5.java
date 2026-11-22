package pruebas;

import modelo.Calificacion;
import modelo.Estudiante;
import modelo.ProfesionalDece;
import negocio.GestorCasos;
import negocio.GestorEstudiantes;
import negocio.GestorRendimiento;

import java.time.LocalDate;
import java.util.ArrayList;

import static pruebas.Verificador.crearEstudiante;
import static pruebas.Verificador.esperarError;
import static pruebas.Verificador.titulo;
import static pruebas.Verificador.verificar;

/**
 * Pruebas del Módulo 5: rendimiento académico (RF-13, RF-14 y RF-15).
 */
public class PruebasModulo5 {

    private static final String ANIO = "2026-2027";

    public static void ejecutar() throws Exception {
        LocalDate hoy = LocalDate.now();
        GestorEstudiantes ge = new GestorEstudiantes();
        Estudiante ana = crearEstudiante("1730000001", 15, 300, 5, true, false, false, false);   // Medio
        Estudiante beto = crearEstudiante("1730000002", 15, 300, 5, true, true, true, false);   // Alto
        Estudiante caro = crearEstudiante("1730000003", 15, 900, 3, false, false, false, false); // Bajo
        Estudiante dani = crearEstudiante("1730000004", 15, 900, 3, false, false, false, false);
        ge.registrarEstudiante(ana);
        ge.registrarEstudiante(beto);
        ge.registrarEstudiante(caro);
        ge.registrarEstudiante(dani);
        GestorCasos gc = new GestorCasos();
        GestorRendimiento g = new GestorRendimiento(gc);

        titulo("MÓDULO 5 – RF-13 Registrar calificaciones");
        String m = g.registrarCalificacion(caro, ANIO, 1, "Matemática", 8.5, hoy);
        verificar(m.contains("8,50") || m.contains("8.50"), "Registra la nota e informa el promedio del periodo");
        String alerta = g.registrarCalificacion(caro, ANIO, 1, "Física", 6.5, hoy);
        verificar(alerta.contains("alerta temprana"), "Una nota menor a 7,00 activa una alerta temprana");
        esperarError(() -> g.registrarCalificacion(caro, ANIO, 1, "física", 9, hoy), "No repite asignatura en el periodo");
        esperarError(() -> g.registrarCalificacion(caro, ANIO, 1, "Química", 10.5, hoy), "Rechaza nota mayor a 10");
        esperarError(() -> g.registrarCalificacion(caro, ANIO, 1, "Química", -1, hoy), "Rechaza nota negativa");
        esperarError(() -> g.registrarCalificacion(caro, ANIO, 4, "Química", 8, hoy), "Rechaza periodo 4");
        esperarError(() -> g.registrarCalificacion(caro, "2026", 1, "Química", 8, hoy), "Rechaza año con formato inválido");
        esperarError(() -> g.registrarCalificacion(caro, ANIO, 1, " ", 8, hoy), "Exige la asignatura");
        g.registrarCalificacion(caro, ANIO, 1, "Química", 6.999, hoy);
        Calificacion quimica = g.buscarCalificaciones(caro, ANIO, 1).get(2);
        verificar(quimica.getNota() == 6.99, "La nota se guarda con dos decimales truncados (6,999 -> 6,99)");
        ge.darDeBaja("1730000004", "Traslado", hoy);
        esperarError(() -> g.registrarCalificacion(dani, ANIO, 1, "Inglés", 8, hoy), "Rechaza estudiante inactivo");

        titulo("MÓDULO 5 – RF-14 Consultar, actualizar y eliminar");
        verificar(g.buscarCalificaciones(caro, ANIO, 1).size() == 3 && g.buscarCalificaciones(caro, ANIO, 2).isEmpty(),
                "Consulta las notas por estudiante y periodo");
        verificar(g.calcularPromedio(caro, ANIO, 1) == 7.33, "Promedio (8,50 + 6,50 + 6,99) / 3 = 7,33");
        g.actualizarNota(quimica, 7.5);
        verificar(quimica.getNota() == 7.5 && g.calcularPromedio(caro, ANIO, 1) == 7.5, "Corrige la nota y recalcula");
        esperarError(() -> g.actualizarNota(quimica, 12), "No corrige con una nota fuera de rango");
        g.eliminarCalificacion(quimica);
        verificar(g.buscarCalificaciones(caro, ANIO, 1).size() == 2, "Elimina una nota mal ingresada");
        esperarError(() -> g.eliminarCalificacion(quimica), "No elimina dos veces la misma nota");

        titulo("MÓDULO 5 – RF-15 Detectar bajo rendimiento");
        // caro: 8,50 y 6,50 -> promedio 7,50 con 1 asignatura bajo 7 -> En observación
        verificar(GestorRendimiento.RENDIMIENTO_EN_OBSERVACION.equals(g.clasificarRendimiento(caro, ANIO, 1)),
                "1 asignatura bajo 7 -> En observación");
        // beto: tres asignaturas bajo 7 aunque el promedio pase de 7
        g.registrarCalificacion(beto, ANIO, 1, "Matemática", 6.9, hoy);
        g.registrarCalificacion(beto, ANIO, 1, "Física", 6.8, hoy);
        g.registrarCalificacion(beto, ANIO, 1, "Química", 6.5, hoy);
        g.registrarCalificacion(beto, ANIO, 1, "Inglés", 10, hoy);
        g.registrarCalificacion(beto, ANIO, 1, "Historia", 10, hoy);
        verificar(g.calcularPromedio(beto, ANIO, 1) >= 7 && g.contarAsignaturasBajoMinimo(beto, ANIO, 1) == 3
                        && GestorRendimiento.RENDIMIENTO_BAJO.equals(g.clasificarRendimiento(beto, ANIO, 1)),
                "3 asignaturas bajo 7 -> Bajo rendimiento (aunque el promedio sea mayor a 7)");
        // ana: promedio menor a 7
        g.registrarCalificacion(ana, ANIO, 1, "Matemática", 9, hoy);
        g.registrarCalificacion(ana, ANIO, 1, "Lengua y Literatura", 4, hoy);
        verificar(GestorRendimiento.RENDIMIENTO_BAJO.equals(g.clasificarRendimiento(ana, ANIO, 1)),
                "Promedio menor a 7 (6,50) -> Bajo rendimiento");
        // Caída pequeña: caro pasa de 7,50 (periodo 1) a 7,25 (periodo 2), todas sus notas sobre 7
        g.registrarCalificacion(caro, ANIO, 2, "Matemática", 7.0, hoy);
        g.registrarCalificacion(caro, ANIO, 2, "Física", 7.5, hoy);
        verificar(g.calcularCaida(caro, ANIO, 2) == 0.25
                        && GestorRendimiento.RENDIMIENTO_ADECUADO.equals(g.clasificarRendimiento(caro, ANIO, 2)),
                "Caída de 0,25 (7,50 -> 7,25) con notas sobre 7 -> Adecuado");
        // Caída grande: beto pasa de 8,80 (periodo 2) a 7,55 (periodo 3), todas sus notas sobre 7
        g.registrarCalificacion(beto, ANIO, 2, "Matemática", 9.0, hoy);
        g.registrarCalificacion(beto, ANIO, 2, "Física", 8.6, hoy);
        g.registrarCalificacion(beto, ANIO, 3, "Matemática", 7.5, hoy);
        g.registrarCalificacion(beto, ANIO, 3, "Física", 7.6, hoy);
        verificar(g.calcularCaida(beto, ANIO, 3) == 1.25
                        && GestorRendimiento.RENDIMIENTO_EN_OBSERVACION.equals(g.clasificarRendimiento(beto, ANIO, 3)),
                "Caída de 1,25 puntos (8,80 -> 7,55) -> En observación");
        verificar(GestorRendimiento.SIN_CALIFICACIONES.equals(g.clasificarRendimiento(ana, ANIO, 2)),
                "Sin notas en el periodo -> Sin calificaciones");

        ArrayList<Estudiante> detectados = g.detectarBajoRendimiento(ge.getEstudiantes(), "", ANIO, 1);
        verificar(detectados.size() == 3 && detectados.get(0) == ana && detectados.get(2) == caro,
                "Detecta a 3 estudiantes: primero Bajo rendimiento (menor promedio) y luego En observación");
        verificar(g.detectarBajoRendimiento(ge.getEstudiantes(), "2do BGU", ANIO, 1).isEmpty(), "Filtra por curso");

        String sinCaso = g.generarRecomendacion(beto, ANIO, 1);
        verificar(sinCaso.contains("Matemática") && sinCaso.contains("abrir un caso"),
                "Sin caso abierto: sugiere refuerzo en las asignaturas bajo 7 y abrir un caso");
        gc.abrirCaso(beto, "Bajo rendimiento académico", new ProfesionalDece("1700000301", "Eva", "Paz", "0990000003",
                ProfesionalDece.CARGO_PROFESIONAL, "eva@colegio.edu.ec"), hoy, "", "");
        verificar(g.generarRecomendacion(beto, ANIO, 1).contains("caso de seguimiento abierto"),
                "Con caso abierto: sugiere registrarlo en ese caso");
        verificar(g.generarRecomendacion(caro, ANIO, 2).equals("No se requieren acciones."),
                "Rendimiento adecuado: no sugiere acciones");
    }
}
