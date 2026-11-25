package interfaz;

import modelo.AsignacionApoyo;
import modelo.Estudiante;
import modelo.ProgramaApoyo;
import negocio.GestorEstudiantes;
import negocio.GestorProgramas;
import negocio.PuenteException;
import negocio.SistemaPuente;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú del Módulo 2 (Mateo Salgado): programas de apoyo (RF-04, RF-05)
 * y asignación de programas a estudiantes con lista de espera (RF-06).
 */
public class MenuProgramas {

    private GestorProgramas gestor;
    private GestorEstudiantes gestorEstudiantes;

    public MenuProgramas(SistemaPuente sistema) {
        gestor = sistema.getGestorProgramas();
        gestorEstudiantes = sistema.getGestorEstudiantes();
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("2. PROGRAMAS DE APOYO");
            System.out.println("1. Mostrar todos los programas");
            System.out.println("2. Buscar programas");
            System.out.println("3. Registrar programa");
            System.out.println("4. Editar programa");
            System.out.println("5. Cerrar programa");
            System.out.println("6. Ver asignaciones de un programa");
            System.out.println("7. Asignar programa a un estudiante");
            System.out.println("8. Finalizar asignación");
            System.out.println("9. Dar el cupo al primero en espera");
            System.out.println("0. Volver al menú principal");
            opcion = Consola.leerEntero("Elija una opción");

            switch (opcion) {
                case 1:
                    mostrarLista(gestor.getProgramas());
                    break;
                case 2:
                    buscar();
                    break;
                case 3:
                    registrarPrograma();
                    break;
                case 4:
                    editarPrograma();
                    break;
                case 5:
                    cerrarPrograma();
                    break;
                case 6:
                    verAsignaciones();
                    break;
                case 7:
                    asignarEstudiante();
                    break;
                case 8:
                    finalizarAsignacion();
                    break;
                case 9:
                    asignarPrimeroEnEspera();
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

    private void mostrarLista(ArrayList<ProgramaApoyo> lista) {
        System.out.println();
        System.out.println("PROGRAMAS DE APOYO");
        System.out.println();
        for (ProgramaApoyo p : lista) {
            String disponibles = "–";   // un programa cerrado ya no tiene cupos disponibles
            if (p.estaVigente()) {
                disponibles = String.valueOf(p.getCuposDisponibles());
            }
            System.out.println("Código " + p.getCodigo() + " | " + p.getNombre() + " | " + p.getTipo()
                    + " | Cupos: " + p.getCupos() + " (asignados " + p.contarAsignacionesVigentes()
                    + ", disponibles " + disponibles + ") | En espera: " + p.getListaEspera().size()
                    + " | Vigencia: " + Consola.formatear(p.getFechaInicio()) + " – "
                    + Consola.formatear(p.getFechaFin()) + " | Nivel mínimo: " + p.getNivelMinimo()
                    + " | " + p.getEstado());
        }
        System.out.println();
        System.out.println("Total: " + lista.size() + " programas.");
    }

    private void buscar() {
        String criterio = Consola.elegirOpcion("Buscar por", GestorProgramas.CRITERIOS_BUSQUEDA);
        String texto = Consola.leerTexto("Texto a buscar");
        mostrarLista(gestor.buscar(criterio, texto));
    }

    /** Muestra las asignaciones vigentes, la lista de espera en orden y el historial del programa. */
    private void mostrarAsignaciones(ProgramaApoyo p) {
        System.out.println();
        System.out.println("ASIGNACIONES DEL PROGRAMA «" + p.getNombre() + "» (código " + p.getCodigo() + ")");
        System.out.println("Vigentes: " + p.contarAsignacionesVigentes() + " de " + p.getCupos()
                + " cupos | En lista de espera: " + p.getListaEspera().size());

        System.out.println();
        System.out.println("VIGENTES");
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (a.estaVigente()) {
                Estudiante e = a.getEstudiante();
                System.out.println("  " + e.getNombreCompleto() + " (" + e.getCedula() + ") – " + e.getCursoCompleto()
                        + " – riesgo " + e.getNivelRiesgo() + " – asignado el "
                        + Consola.formatear(a.getFechaAsignacion()));
            }
        }

        System.out.println();
        System.out.println("LISTA DE ESPERA (en orden)");
        int posicion = 1;
        for (AsignacionApoyo a : p.getListaEspera()) {
            Estudiante e = a.getEstudiante();
            System.out.println("  " + posicion + ". " + e.getNombreCompleto() + " (" + e.getCedula() + ") – riesgo "
                    + e.getNivelRiesgo() + " – solicitó el " + Consola.formatear(a.getFechaSolicitud()));
            posicion++;
        }

        System.out.println();
        System.out.println("HISTORIAL (finalizadas o canceladas)");
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (!a.estaVigente() && !a.estaEnEspera()) {
                System.out.println("  " + a.getEstudiante().getNombreCompleto() + " – " + a.getEstado());
            }
        }
    }

    // Pide el código del programa y lo busca (lanza un error si no existe)
    private ProgramaApoyo leerPrograma() throws PuenteException {
        int codigo = Consola.leerEntero("Código del programa");
        return gestor.obtenerPrograma(codigo);
    }

    // Pide la cédula del estudiante y lo busca (lanza un error si no existe)
    private Estudiante leerEstudiante() throws PuenteException {
        return gestorEstudiantes.obtenerEstudiante(Consola.leerTexto("Cédula del estudiante"));
    }

    private void registrarPrograma() {
        System.out.println("REGISTRAR PROGRAMA DE APOYO (RF-04)");
        try {
            ProgramaApoyo datos = construirPrograma(null);
            System.out.println(gestor.registrarPrograma(datos));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void editarPrograma() {
        try {
            ProgramaApoyo p = leerPrograma();
            if (!p.estaVigente()) {
                Consola.mostrarError("No se puede modificar un programa cerrado.");
                return;
            }
            System.out.println("EDITAR PROGRAMA (RF-05). Presione Enter para conservar el valor entre corchetes.");
            ProgramaApoyo datos = construirPrograma(p);
            System.out.println(gestor.actualizarPrograma(p.getCodigo(), datos));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    // Arma el programa con lo que escribe el usuario; las reglas las revisa el gestor.
    // Al registrar, existente es null; al editar, se muestran sus datos actuales.
    private ProgramaApoyo construirPrograma(ProgramaApoyo existente) {
        String nombre = null;
        String tipo = null;
        String descripcion = null;
        String cupos = null;
        String inicio = Consola.formatear(LocalDate.now());   // al registrar, por defecto empieza hoy
        String fin = null;
        String nivel = Estudiante.NIVEL_MEDIO;                // valor por defecto según RF-04

        if (existente != null) {
            nombre = existente.getNombre();
            tipo = existente.getTipo();
            descripcion = existente.getDescripcion();
            cupos = String.valueOf(existente.getCupos());
            inicio = Consola.formatear(existente.getFechaInicio());
            fin = Consola.formatear(existente.getFechaFin());
            nivel = existente.getNivelMinimo();
        }

        nombre = Consola.leerTexto("Nombre", nombre);
        tipo = Consola.elegirOpcion("Tipo de apoyo", ProgramaApoyo.TIPOS, tipo);
        descripcion = Consola.leerTexto("Descripción", descripcion);
        int numeroCupos = Consola.leerEntero("Cupos", cupos);
        LocalDate fechaInicio = Consola.leerFecha("Fecha de inicio", inicio);
        LocalDate fechaFin = Consola.leerFecha("Fecha de fin", fin);
        nivel = Consola.elegirOpcion("Nivel mínimo de riesgo", Estudiante.NIVELES, nivel);

        return new ProgramaApoyo(nombre, tipo, descripcion, numeroCupos, fechaInicio, fechaFin, nivel);
    }

    private void cerrarPrograma() {
        try {
            ProgramaApoyo p = leerPrograma();
            if (!Consola.confirmar("¿Cerrar el programa «" + p.getNombre()
                    + "»? Las asignaciones vigentes pasarán a Finalizada y la lista de espera se cancelará.")) {
                System.out.println("Operación cancelada.");
                return;
            }
            System.out.println(gestor.cerrarPrograma(p.getCodigo()));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void verAsignaciones() {
        try {
            mostrarAsignaciones(leerPrograma());
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void asignarEstudiante() {
        try {
            ProgramaApoyo p = leerPrograma();
            Estudiante e = leerEstudiante();
            System.out.println(gestor.asignarApoyo(e, p, LocalDate.now()));
            mostrarAsignaciones(p);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void finalizarAsignacion() {
        try {
            ProgramaApoyo p = leerPrograma();
            Estudiante e = leerEstudiante();
            AsignacionApoyo asignacion = gestor.buscarAsignacionActiva(e, p);
            if (asignacion == null) {
                Consola.mostrarError(e.getNombreCompleto() + " no tiene una asignación vigente en este programa.");
                return;
            }
            System.out.println(gestor.finalizarAsignacion(asignacion));
            mostrarAsignaciones(p);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void asignarPrimeroEnEspera() {
        try {
            ProgramaApoyo p = leerPrograma();
            System.out.println(gestor.asignarPrimeroEnEspera(p, LocalDate.now()));
            mostrarAsignaciones(p);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }
}
