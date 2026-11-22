package pruebas;

import modelo.AsignacionApoyo;
import modelo.Estudiante;
import modelo.ProgramaApoyo;
import negocio.GestorEstudiantes;
import negocio.GestorProgramas;

import java.time.LocalDate;

import static pruebas.Verificador.crearEstudiante;
import static pruebas.Verificador.esperarError;
import static pruebas.Verificador.titulo;
import static pruebas.Verificador.verificar;

/**
 * Pruebas del Módulo 2: programas de apoyo y asignación (RF-04, RF-05 y RF-06).
 */
public class PruebasModulo2 {

    public static void ejecutar() throws Exception {
        LocalDate hoy = LocalDate.now();
        GestorEstudiantes ge = new GestorEstudiantes();
        // Puntajes: critico = 10, alto = 6, medio = 3, bajo = 0
        Estudiante critico = crearEstudiante("1710000001", 16, 200, 5, true, true, true, true);
        Estudiante alto = crearEstudiante("1710000002", 14, 300, 5, false, true, true, false);
        Estudiante alto2 = crearEstudiante("1710000003", 14, 300, 5, true, false, true, false);
        Estudiante medio = crearEstudiante("1710000004", 14, 300, 5, false, false, false, false);
        Estudiante bajo = crearEstudiante("1710000005", 14, 900, 3, false, false, false, false);
        ge.registrarEstudiante(critico);
        ge.registrarEstudiante(alto);
        ge.registrarEstudiante(alto2);
        ge.registrarEstudiante(medio);
        ge.registrarEstudiante(bajo);

        titulo("MÓDULO 2 – RF-04 Registrar programa de apoyo");
        GestorProgramas g = new GestorProgramas();
        ProgramaApoyo refrigerio = new ProgramaApoyo("Refrigerio", "Refrigerio", "", 2, hoy.minusDays(10),
                hoy.plusMonths(6), Estudiante.NIVEL_MEDIO);
        g.registrarPrograma(refrigerio);
        verificar(refrigerio.estaVigente() && refrigerio.getCodigo() == 1 && refrigerio.getCuposDisponibles() == 2,
                "Programa creado Vigente, con código y cupos disponibles");
        esperarError(() -> g.registrarPrograma(new ProgramaApoyo("Útiles", "Útiles escolares", "", 0, hoy,
                hoy.plusDays(30), "Medio")), "Rechaza cupos iguales a cero");
        esperarError(() -> g.registrarPrograma(new ProgramaApoyo("Útiles", "Útiles escolares", "", 5, hoy,
                hoy.minusDays(1), "Medio")), "Rechaza fecha de fin anterior a la de inicio");
        esperarError(() -> g.registrarPrograma(new ProgramaApoyo("refrigerio", "Refrigerio", "", 5, hoy,
                hoy.plusDays(30), "Medio")), "Rechaza el mismo nombre en un periodo que se cruza");
        g.registrarPrograma(new ProgramaApoyo("Refrigerio", "Refrigerio", "", 5, hoy.plusMonths(7),
                hoy.plusMonths(12), "Medio"));
        verificar(g.getProgramas().size() == 2, "Acepta el mismo nombre en otro periodo");

        titulo("MÓDULO 2 – RF-06 Asignar apoyo a estudiante");
        String m1 = g.asignarApoyo(alto, refrigerio, hoy.minusDays(5));
        verificar(m1.contains("Cupos restantes: 1"), "Asigna y actualiza los cupos restantes");
        esperarError(() -> g.asignarApoyo(alto, refrigerio, hoy), "No asigna dos veces el mismo programa");
        esperarError(() -> g.asignarApoyo(bajo, refrigerio, hoy), "Rechaza nivel de riesgo menor al mínimo (Bajo < Medio)");
        g.asignarApoyo(medio, refrigerio, hoy.minusDays(4));
        verificar(refrigerio.getCuposDisponibles() == 0, "Con dos asignaciones el programa queda sin cupos");
        String espera1 = g.asignarApoyo(alto2, refrigerio, hoy.minusDays(3));
        String espera2 = g.asignarApoyo(critico, refrigerio, hoy.minusDays(1));
        verificar(espera1.contains("lista de espera") && g.obtenerPosicionEnEspera(alto2, refrigerio) == 2
                        && g.obtenerPosicionEnEspera(critico, refrigerio) == 1,
                "Sin cupos entra a lista de espera y el Crítico pasa primero aunque llegó después");
        verificar(espera2.contains("posición 1"), "El mensaje informa la posición en la lista");
        esperarError(() -> g.asignarApoyo(critico, refrigerio, hoy), "No repite a quien ya está en lista de espera");
        ge.darDeBaja("1710000005", "Traslado", hoy);
        esperarError(() -> g.asignarApoyo(bajo, g.getProgramas().get(1), hoy), "Rechaza estudiante inactivo");

        titulo("MÓDULO 2 – Liberación de cupos y lista de espera");
        AsignacionApoyo asignacionMedio = g.buscarAsignacionActiva(medio, refrigerio);
        String fin = g.finalizarAsignacion(asignacionMedio);
        verificar(!asignacionMedio.estaVigente() && fin.contains("se propone") && fin.contains(critico.getNombreCompleto()),
                "Al finalizar se libera un cupo y se propone al primero de la lista (Crítico)");
        g.asignarPrimeroEnEspera(refrigerio, hoy);
        verificar(g.buscarAsignacionActiva(critico, refrigerio).estaVigente(), "El primero de la lista recibe el cupo");
        esperarError(() -> g.asignarPrimeroEnEspera(refrigerio, hoy), "Sin cupos no se puede asignar desde la espera");
        verificar(g.tieneApoyoVigente(critico) && !g.tieneApoyoVigente(bajo), "tieneApoyoVigente (usado por el Módulo 4)");

        titulo("MÓDULO 2 – RF-05 Consultar, actualizar y cerrar programas");
        verificar(g.buscar("Tipo", "refri").size() == 2 && g.buscar("Nombre", "xyz").isEmpty(), "Busca por tipo y nombre");
        ProgramaApoyo menosCupos = new ProgramaApoyo("Refrigerio", "Refrigerio", "", 1, refrigerio.getFechaInicio(),
                refrigerio.getFechaFin(), "Medio");
        esperarError(() -> g.actualizarPrograma(refrigerio.getCodigo(), menosCupos),
                "No reduce los cupos por debajo de los asignados (2)");
        ProgramaApoyo masCupos = new ProgramaApoyo("Refrigerio", "Refrigerio", "", 3, refrigerio.getFechaInicio(),
                refrigerio.getFechaFin(), "Medio");
        String actualizado = g.actualizarPrograma(refrigerio.getCodigo(), masCupos);
        verificar(refrigerio.getCupos() == 3 && actualizado.contains(alto2.getNombreCompleto()),
                "Al aumentar cupos se propone al siguiente de la lista de espera");
        String cierre = g.cerrarPrograma(refrigerio.getCodigo());
        verificar(!refrigerio.estaVigente() && refrigerio.contarAsignacionesVigentes() == 0
                && refrigerio.getListaEspera().isEmpty() && cierre.contains("finalizados): 2")
                && cierre.contains("canceladas: 1"), "Al cerrar: vigentes -> Finalizada y espera -> Cancelada");
        esperarError(() -> g.asignarApoyo(medio, refrigerio, hoy), "No asigna en un programa cerrado");
        esperarError(() -> g.actualizarPrograma(refrigerio.getCodigo(), masCupos), "No edita un programa cerrado");
        esperarError(() -> g.cerrarPrograma(refrigerio.getCodigo()), "No cierra dos veces");
        ProgramaApoyo vencido = new ProgramaApoyo("Uniformes", "Uniforme", "", 3, hoy.minusMonths(6),
                hoy.minusMonths(1), "Medio");
        g.registrarPrograma(vencido);
        esperarError(() -> g.asignarApoyo(medio, vencido, hoy), "No asigna si la vigencia del programa ya terminó");
    }
}
