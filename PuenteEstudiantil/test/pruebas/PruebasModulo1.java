package pruebas;

import modelo.Estudiante;
import negocio.GestorEstudiantes;

import java.time.LocalDate;

import static pruebas.Verificador.crearEstudiante;
import static pruebas.Verificador.esperarError;
import static pruebas.Verificador.titulo;
import static pruebas.Verificador.verificar;

/**
 * Pruebas del Módulo 1: estudiantes y riesgo de abandono (RF-01, RF-02 y RF-03).
 */
public class PruebasModulo1 {

    public static void ejecutar() throws Exception {
        titulo("MÓDULO 1 – RF-01 Registrar estudiante");
        GestorEstudiantes g = new GestorEstudiantes();
        // Ingreso por persona 100 (> 92,40): no suma por ingreso
        Estudiante e1 = crearEstudiante("1700000001", 14, 400, 4, false, false, false, false);
        String mensaje = g.registrarEstudiante(e1);
        verificar(e1.estaActivo() && mensaje.contains("Activo"), "Registro válido queda en estado Activo");
        verificar(g.getEstudiantes().size() == 1, "El estudiante se guarda en la lista");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("1700000001", 14, 400, 4, false, false, false, false)),
                "Rechaza cédula repetida");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("170000002", 14, 400, 4, false, false, false, false)),
                "Rechaza cédula de 9 dígitos");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("1700000003", 10, 400, 4, false, false, false, false)),
                "Rechaza edad menor a 11 años");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("1700000004", 21, 400, 4, false, false, false, false)),
                "Rechaza edad mayor a 20 años");
        g.registrarEstudiante(crearEstudiante("1700000005", 11, 400, 4, false, false, false, false));
        g.registrarEstudiante(crearEstudiante("1700000006", 20, 400, 4, false, false, false, false));
        verificar(g.getEstudiantes().size() == 3, "Acepta los límites de edad 11 y 20 años");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("1700000007", 14, -5, 4, false, false, false, false)),
                "Rechaza ingreso negativo");
        esperarError(() -> g.registrarEstudiante(crearEstudiante("1700000008", 14, 400, 0, false, false, false, false)),
                "Rechaza hogar con 0 miembros");
        Estudiante sinNombre = crearEstudiante("1700000009", 14, 400, 4, false, false, false, false);
        sinNombre.setNombres("  ");
        esperarError(() -> g.registrarEstudiante(sinNombre), "Rechaza datos vacíos (nombres)");
        Estudiante telefonoMalo = crearEstudiante("1700000010", 14, 400, 4, false, false, false, false);
        telefonoMalo.getRepresentante().setTelefono("09abc");
        esperarError(() -> g.registrarEstudiante(telefonoMalo), "Rechaza teléfono del representante con letras");
        verificar(g.getEstudiantes().size() == 3, "Los registros rechazados no se guardan");

        titulo("MÓDULO 1 – RF-03 Calcular nivel de riesgo");
        Estudiante todos = crearEstudiante("1700000011", 16, 200, 5, true, true, true, true);
        g.registrarEstudiante(todos);
        verificar(todos.getPuntajeRiesgo() == 10 && Estudiante.NIVEL_CRITICO.equals(todos.getNivelRiesgo()),
                "Con los seis factores suma 10 puntos y queda Crítico (" + todos.getPuntajeRiesgo() + ")");
        verificar(todos.getDetalleRiesgo().contains("+3") && todos.getDetalleRiesgo().contains("16 años"),
                "Guarda el detalle de los factores que sumaron");
        verificar(e1.getPuntajeRiesgo() == 0 && Estudiante.NIVEL_BAJO.equals(e1.getNivelRiesgo()),
                "Sin factores queda en 0 puntos, nivel Bajo");
        Estudiante diecisiete = crearEstudiante("1700000012", 17, 400, 4, false, false, false, false);
        Estudiante dieciocho = crearEstudiante("1700000013", 18, 400, 4, false, false, false, false);
        g.registrarEstudiante(diecisiete);
        g.registrarEstudiante(dieciocho);
        verificar(diecisiete.getPuntajeRiesgo() == 1 && dieciocho.getPuntajeRiesgo() == 0,
                "La edad suma solo con 16 o 17 años (17 -> 1 punto, 18 -> 0)");
        verificar(g.obtenerNivel(2).equals("Bajo") && g.obtenerNivel(3).equals("Medio")
                        && g.obtenerNivel(5).equals("Medio") && g.obtenerNivel(6).equals("Alto")
                        && g.obtenerNivel(7).equals("Alto") && g.obtenerNivel(8).equals("Crítico"),
                "Rangos de nivel: 0–2 Bajo, 3–5 Medio, 6–7 Alto, 8–10 Crítico");
        Estudiante limite = crearEstudiante("1700000014", 14, 368, 4, false, false, false, false); // 92,00 por persona
        g.registrarEstudiante(limite);
        verificar(limite.getPuntajeRiesgo() == 3, "Ingreso por persona de 92,00 (< 92,40) suma 3 puntos");
        g.cambiarUmbral(80);
        verificar(limite.getPuntajeRiesgo() == 0, "Al bajar el umbral a 80 se recalcula y deja de sumar");
        esperarError(() -> g.cambiarUmbral(0), "Rechaza umbral igual a cero");
        g.cambiarUmbral(GestorEstudiantes.UMBRAL_INICIAL);

        titulo("MÓDULO 1 – RF-02 Consultar, actualizar y dar de baja");
        verificar(g.buscar("Cédula", "0000011").size() == 1, "Busca por cédula");
        verificar(g.buscar("Nombre", "PRUEBA 0011").size() == 1, "Busca por nombre (sin importar mayúsculas)");
        verificar(g.buscar("Curso", "1ro bgu a").size() == g.getEstudiantes().size(), "Busca por curso");
        verificar(g.buscar("Nivel de riesgo", "Crítico").size() == 1, "Busca por nivel de riesgo");
        verificar(g.buscar("Nombre", "").size() == g.getEstudiantes().size(), "Sin texto devuelve todos");

        Estudiante cambios = crearEstudiante("1700000001", 14, 400, 4, true, false, false, false);
        g.actualizarEstudiante("1700000001", cambios);
        verificar(e1.getFicha().isTrabaja() && e1.getPuntajeRiesgo() == 2,
                "Al actualizar (ahora trabaja) se recalcula el riesgo a 2 puntos");
        esperarError(() -> g.actualizarEstudiante("1799999999", cambios), "No actualiza una cédula inexistente");

        String baja = g.darDeBaja("1700000001", "Traslado", LocalDate.now());
        verificar(!e1.estaActivo() && "Traslado".equals(e1.getMotivoBaja()) && e1.getFechaBaja() != null
                && baja.contains("Inactivo"), "La baja es lógica: queda Inactivo con motivo y fecha");
        verificar(g.buscarPorCedula("1700000001") != null, "El estudiante dado de baja se conserva (historial)");
        esperarError(() -> g.darDeBaja("1700000001", "Otro", LocalDate.now()), "No da de baja dos veces");
        esperarError(() -> g.actualizarEstudiante("1700000001", cambios), "No permite editar un estudiante inactivo");
        esperarError(() -> g.darDeBaja("1700000005", "Traslado", LocalDate.now().plusDays(1)),
                "Rechaza fecha de baja futura");
        esperarError(() -> g.darDeBaja("1700000005", "", LocalDate.now()), "Exige el motivo de la baja");
        verificar(g.listarActivos().size() == g.getEstudiantes().size() - 1, "listarActivos excluye a los inactivos");
    }
}
