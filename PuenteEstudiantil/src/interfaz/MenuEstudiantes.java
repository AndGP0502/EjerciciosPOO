package interfaz;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;
import negocio.GestorEstudiantes;
import negocio.PuenteException;
import negocio.SistemaPuente;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú del Módulo 1: búsqueda de estudiantes (RF-02), registro y edición (RF-01 y RF-02),
 * baja lógica (RF-02) y cálculo del riesgo con umbral configurable (RF-03).
 */
public class MenuEstudiantes {

    private static final String[] ZONAS = {FichaSocioeconomica.ZONA_URBANA, FichaSocioeconomica.ZONA_RURAL};
    private static final String[] PARENTESCOS = {"Madre", "Padre", "Abuelo/a", "Tío/a", "Hermano/a", "Otro"};

    private GestorEstudiantes gestor;

    public MenuEstudiantes(SistemaPuente sistema) {
        gestor = sistema.getGestorEstudiantes();
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("1. ESTUDIANTES Y RIESGO");
            System.out.println("1. Mostrar todos los estudiantes");
            System.out.println("2. Buscar estudiantes");
            System.out.println("3. Registrar estudiante");
            System.out.println("4. Editar estudiante");
            System.out.println("5. Dar de baja a un estudiante");
            System.out.println("6. Ver ficha y riesgo de un estudiante");
            System.out.println("7. Recalcular el riesgo de todos");
            System.out.println("8. Cambiar el umbral de ingreso");
            System.out.println("0. Volver al menú principal");
            opcion = Consola.leerEntero("Elija una opción");

            switch (opcion) {
                case 1:
                    mostrarLista(gestor.getEstudiantes());
                    break;
                case 2:
                    buscar();
                    break;
                case 3:
                    registrarEstudiante();
                    break;
                case 4:
                    editarEstudiante();
                    break;
                case 5:
                    darDeBaja();
                    break;
                case 6:
                    verFicha();
                    break;
                case 7:
                    System.out.println(gestor.recalcularTodos());
                    break;
                case 8:
                    cambiarUmbral();
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

    private void mostrarLista(ArrayList<Estudiante> lista) {
        System.out.println();
        System.out.println("Umbral de ingreso por persona: USD "
                + String.format("%.2f", gestor.getUmbralIngresoPorPersona()));
        System.out.println();
        int activos = 0;
        for (Estudiante e : lista) {
            System.out.println(e.getCedula() + " | " + e.getNombreCompleto() + " | " + e.calcularEdad() + " años | "
                    + e.getCursoCompleto() + " | " + e.getFicha().getZona() + " | Ingreso por persona: $ "
                    + String.format("%.2f", e.getFicha().calcularIngresoPorPersona()) + " | Riesgo: "
                    + e.getNivelRiesgo() + " (" + e.getPuntajeRiesgo() + " puntos) | " + e.getEstado());
            if (e.estaActivo()) {
                activos++;
            }
        }
        System.out.println();
        System.out.println("Total: " + lista.size() + " estudiantes (" + activos + " activos).");
    }

    private void buscar() {
        String criterio = Consola.elegirOpcion("Buscar por", GestorEstudiantes.CRITERIOS_BUSQUEDA);
        String texto = Consola.leerTexto("Texto a buscar");
        mostrarLista(gestor.buscar(criterio, texto));
    }

    private void registrarEstudiante() {
        System.out.println("REGISTRAR ESTUDIANTE (RF-01)");
        try {
            Estudiante datos = construirEstudiante(null);
            System.out.println(gestor.registrarEstudiante(datos));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void editarEstudiante() {
        try {
            Estudiante e = gestor.obtenerEstudiante(Consola.leerTexto("Cédula del estudiante"));
            if (!e.estaActivo()) {
                Consola.mostrarError("No se puede modificar un estudiante inactivo.");
                return;
            }
            System.out.println("EDITAR ESTUDIANTE (RF-02). Presione Enter para conservar el valor entre corchetes.");
            Estudiante datos = construirEstudiante(e);
            System.out.println(gestor.actualizarEstudiante(e.getCedula(), datos));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    // Arma el objeto con lo que escribe el usuario; las reglas las revisa el gestor.
    // Al registrar, existente es null; al editar, se muestran sus datos actuales.
    private Estudiante construirEstudiante(Estudiante existente) {
        String cedula = null;
        String nombres = null;
        String apellidos = null;
        String nacimiento = null;
        String telefono = null;
        String curso = null;
        String paralelo = null;
        String direccion = null;
        String sector = null;
        String zona = null;
        String repNombres = null;
        String repApellidos = null;
        String repTelefono = null;
        String parentesco = null;
        String miembros = null;
        String ingreso = null;
        String trabaja = null;
        String hijos = null;
        String repitio = null;
        String observaciones = null;
        String cedulaRep = "";

        if (existente != null) {
            cedula = existente.getCedula();
            nombres = existente.getNombres();
            apellidos = existente.getApellidos();
            nacimiento = Consola.formatear(existente.getFechaNacimiento());
            telefono = existente.getTelefono();
            curso = existente.getCurso();
            paralelo = existente.getParalelo();
            direccion = existente.getDireccion();
            sector = existente.getSector();
            zona = existente.getFicha().getZona();
            repNombres = existente.getRepresentante().getNombres();
            repApellidos = existente.getRepresentante().getApellidos();
            repTelefono = existente.getRepresentante().getTelefono();
            parentesco = existente.getRepresentante().getParentesco();
            miembros = String.valueOf(existente.getFicha().getMiembrosHogar());
            ingreso = String.format("%.2f", existente.getFicha().getIngresoMensualHogar());
            trabaja = Consola.siNo(existente.getFicha().isTrabaja());
            hijos = Consola.siNo(existente.getFicha().isTieneHijosOCuidaFamiliares());
            repitio = Consola.siNo(existente.getFicha().isRepitioAnio());
            observaciones = existente.getObservaciones();
            cedulaRep = existente.getRepresentante().getCedula();
        }

        if (existente == null) {
            cedula = Consola.leerTexto("Cédula");
        } else {
            System.out.println("Cédula: " + cedula + " (no se puede cambiar)");
        }
        nombres = Consola.leerTexto("Nombres", nombres);
        apellidos = Consola.leerTexto("Apellidos", apellidos);
        LocalDate fechaNacimiento = Consola.leerFecha("Fecha de nacimiento", nacimiento);
        telefono = Consola.leerTexto("Teléfono (opcional)", telefono);
        curso = Consola.elegirOpcion("Curso", Consola.CURSOS, curso);
        paralelo = Consola.elegirOpcion("Paralelo", Consola.PARALELOS, paralelo);
        direccion = Consola.leerTexto("Dirección", direccion);
        sector = Consola.leerTexto("Sector", sector);
        zona = Consola.elegirOpcion("Zona", ZONAS, zona);

        System.out.println("-- Representante --");
        repNombres = Consola.leerTexto("Nombres del representante", repNombres);
        repApellidos = Consola.leerTexto("Apellidos del representante", repApellidos);
        repTelefono = Consola.leerTexto("Teléfono del representante", repTelefono);
        parentesco = Consola.elegirOpcion("Parentesco", PARENTESCOS, parentesco);

        System.out.println("-- Ficha socioeconómica --");
        int miembrosHogar = Consola.leerEntero("Miembros del hogar", miembros);
        double ingresoHogar = Consola.leerDecimal("Ingreso mensual del hogar (USD)", ingreso);
        boolean siTrabaja = Consola.leerSiNo("¿Trabaja?", trabaja);
        boolean tieneHijos = Consola.leerSiNo("¿Tiene hijos o cuida familiares?", hijos);
        boolean repitioAnio = Consola.leerSiNo("¿Ha repetido año?", repitio);
        observaciones = Consola.leerTexto("Observaciones (opcional)", observaciones);

        Representante representante = new Representante(cedulaRep, repNombres, repApellidos, repTelefono, parentesco);
        FichaSocioeconomica ficha = new FichaSocioeconomica(zona, miembrosHogar, ingresoHogar,
                siTrabaja, tieneHijos, repitioAnio);
        return new Estudiante(cedula, nombres, apellidos, telefono, fechaNacimiento, curso, paralelo,
                direccion, sector, observaciones, representante, ficha);
    }

    private void darDeBaja() {
        try {
            Estudiante e = gestor.obtenerEstudiante(Consola.leerTexto("Cédula del estudiante"));
            String motivo = Consola.elegirOpcion("Motivo de baja", GestorEstudiantes.MOTIVOS_BAJA);
            if (!Consola.confirmar("¿Dar de baja a " + e.getNombreCompleto() + "? Motivo: " + motivo + ".")) {
                System.out.println("Operación cancelada.");
                return;
            }
            System.out.println(gestor.darDeBaja(e.getCedula(), motivo, LocalDate.now()));
            System.out.println("Su historial se conserva en el sistema.");
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void verFicha() {
        try {
            Estudiante e = gestor.obtenerEstudiante(Consola.leerTexto("Cédula del estudiante"));
            FichaSocioeconomica f = e.getFicha();
            Representante r = e.getRepresentante();
            System.out.println();
            System.out.println("DATOS DEL ESTUDIANTE");
            System.out.println(e.mostrarDatos());
            System.out.println("Fecha de nacimiento: " + Consola.formatear(e.getFechaNacimiento())
                    + " (" + e.calcularEdad() + " años)");
            System.out.println("Dirección: " + e.getDireccion() + " – Sector: " + e.getSector());
            System.out.println();
            System.out.println("REPRESENTANTE");
            System.out.println(r.mostrarDatos());
            System.out.println();
            System.out.println("FICHA SOCIOECONÓMICA");
            System.out.println("Zona: " + f.getZona());
            System.out.println("Miembros del hogar: " + f.getMiembrosHogar());
            System.out.println("Ingreso mensual del hogar: USD " + String.format("%.2f", f.getIngresoMensualHogar()));
            System.out.println("Ingreso por persona: USD " + String.format("%.2f", f.calcularIngresoPorPersona()));
            System.out.println("Trabaja: " + (f.isTrabaja() ? "Sí" : "No"));
            System.out.println("Tiene hijos o cuida familiares: " + (f.isTieneHijosOCuidaFamiliares() ? "Sí" : "No"));
            System.out.println("Ha repetido año: " + (f.isRepitioAnio() ? "Sí" : "No"));
            System.out.println();
            System.out.println("CÁLCULO DEL RIESGO (RF-03)");
            System.out.println("Puntaje: " + e.getPuntajeRiesgo() + " de 10 – Nivel: " + e.getNivelRiesgo());
            System.out.println("Factores que sumaron: " + e.getDetalleRiesgo());
            if (!e.estaActivo()) {
                System.out.println();
                System.out.println("BAJA");
                System.out.println("Fecha: " + Consola.formatear(e.getFechaBaja()) + " – Motivo: " + e.getMotivoBaja());
            }
            if (e.getObservaciones() != null && !e.getObservaciones().isEmpty()) {
                System.out.println();
                System.out.println("Observaciones: " + e.getObservaciones());
            }
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void cambiarUmbral() {
        System.out.println("Al cambiar el umbral se recalcula el riesgo de todos los estudiantes.");
        double umbral = Consola.leerDecimal("Nuevo umbral de ingreso mensual por persona (USD)",
                String.format("%.2f", gestor.getUmbralIngresoPorPersona()));
        try {
            System.out.println(gestor.cambiarUmbral(umbral));
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }
}
