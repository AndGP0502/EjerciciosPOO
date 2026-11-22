package negocio;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Módulo 1 (André Garzón): registro de estudiantes y cálculo del riesgo de abandono.
 * Cubre RF-01 (registrar), RF-02 (consultar, actualizar y dar de baja) y RF-03 (calcular riesgo).
 *
 * Aquí no se muestra nada al usuario: los métodos devuelven un mensaje de confirmación
 * o lanzan {@link PuenteException} cuando algo no cumple las reglas.
 */
public class GestorEstudiantes {

    public static final int EDAD_MINIMA = 11;
    public static final int EDAD_MAXIMA = 20;
    /** Valor inicial del umbral: línea de pobreza por ingresos del INEC a diciembre de 2025 (USD 92,40). */
    public static final double UMBRAL_INICIAL = 92.40;
    public static final String[] MOTIVOS_BAJA = {"Traslado", "Graduación", "Abandono confirmado", "Otro"};
    public static final String[] CRITERIOS_BUSQUEDA = {"Nombre", "Cédula", "Curso", "Nivel de riesgo"};

    private ArrayList<Estudiante> estudiantes;
    private double umbralIngresoPorPersona;

    public GestorEstudiantes() {
        estudiantes = new ArrayList<>();
        umbralIngresoPorPersona = UMBRAL_INICIAL;
    }

    // ------------------------------------------------------------------
    // RF-01: registrar estudiante y ficha socioeconómica
    // ------------------------------------------------------------------

    public String registrarEstudiante(Estudiante estudiante) throws PuenteException {
        validarDatos(estudiante, true);
        calcularRiesgo(estudiante);
        estudiantes.add(estudiante);
        return "Estudiante registrado en estado Activo. Nivel de riesgo: " + estudiante.getNivelRiesgo()
                + " (" + estudiante.getPuntajeRiesgo() + " puntos).";
    }

    // ------------------------------------------------------------------
    // RF-02: consultar, actualizar y dar de baja
    // ------------------------------------------------------------------

    /** Actualiza los datos de un estudiante activo (la cédula no cambia) y recalcula su riesgo. */
    public String actualizarEstudiante(String cedula, Estudiante datosNuevos) throws PuenteException {
        Estudiante estudiante = obtenerEstudiante(cedula);
        if (!estudiante.estaActivo()) {
            throw new PuenteException("No se puede modificar un estudiante inactivo.");
        }
        validarDatos(datosNuevos, false);
        estudiante.setNombres(datosNuevos.getNombres());
        estudiante.setApellidos(datosNuevos.getApellidos());
        estudiante.setTelefono(datosNuevos.getTelefono());
        estudiante.setFechaNacimiento(datosNuevos.getFechaNacimiento());
        estudiante.setCurso(datosNuevos.getCurso());
        estudiante.setParalelo(datosNuevos.getParalelo());
        estudiante.setDireccion(datosNuevos.getDireccion());
        estudiante.setSector(datosNuevos.getSector());
        estudiante.setObservaciones(datosNuevos.getObservaciones());
        estudiante.setRepresentante(datosNuevos.getRepresentante());
        estudiante.setFicha(datosNuevos.getFicha());
        calcularRiesgo(estudiante);
        return "Datos actualizados. Nivel de riesgo recalculado: " + estudiante.getNivelRiesgo()
                + " (" + estudiante.getPuntajeRiesgo() + " puntos).";
    }

    /** Baja lógica: el estudiante pasa a Inactivo y se conserva todo su historial. */
    public String darDeBaja(String cedula, String motivo, LocalDate fecha) throws PuenteException {
        Estudiante estudiante = obtenerEstudiante(cedula);
        if (!estudiante.estaActivo()) {
            throw new PuenteException("El estudiante ya se encuentra inactivo.");
        }
        if (estaVacio(motivo)) {
            throw new PuenteException("Seleccione el motivo de la baja.");
        }
        if (fecha == null || fecha.isAfter(LocalDate.now())) {
            throw new PuenteException("La fecha de baja no es válida (no puede ser futura).");
        }
        estudiante.darDeBaja(motivo, fecha);
        return estudiante.getNombreCompleto() + " pasó a estado Inactivo. Motivo: " + motivo + ".";
    }

    /** Busca estudiantes por nombre, cédula, curso o nivel de riesgo. Sin texto devuelve todos. */
    public ArrayList<Estudiante> buscar(String criterio, String texto) {
        ArrayList<Estudiante> resultado = new ArrayList<>();
        String buscado = texto == null ? "" : texto.trim().toLowerCase();
        for (Estudiante e : estudiantes) {
            boolean coincide;
            if (buscado.isEmpty()) {
                coincide = true;
            } else if ("Cédula".equals(criterio)) {
                coincide = e.getCedula().contains(buscado);
            } else if ("Curso".equals(criterio)) {
                coincide = e.getCursoCompleto().toLowerCase().contains(buscado);
            } else if ("Nivel de riesgo".equals(criterio)) {
                coincide = e.getNivelRiesgo().equalsIgnoreCase(buscado);
            } else {
                coincide = e.getNombreCompleto().toLowerCase().contains(buscado);
            }
            if (coincide) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    // ------------------------------------------------------------------
    // RF-03: calcular nivel de riesgo de abandono [Regla de negocio]
    // ------------------------------------------------------------------

    /**
     * Suma los puntos de seis factores: ingreso por persona menor al umbral (3), trabaja (2),
     * tiene hijos o cuida familiares (2), zona rural (1), tiene 16 o 17 años (1) y repitió año (1).
     * Con el total (0 a 10) asigna el nivel y guarda el detalle de los factores que sumaron.
     */
    public void calcularRiesgo(Estudiante estudiante) {
        FichaSocioeconomica ficha = estudiante.getFicha();
        int puntaje = 0;
        String detalle = "";

        if (ficha.calcularIngresoPorPersona() < umbralIngresoPorPersona) {
            puntaje += 3;
            detalle += "Ingreso por persona menor al umbral (+3). ";
        }
        if (ficha.isTrabaja()) {
            puntaje += 2;
            detalle += "Trabaja (+2). ";
        }
        if (ficha.isTieneHijosOCuidaFamiliares()) {
            puntaje += 2;
            detalle += "Tiene hijos o cuida familiares (+2). ";
        }
        if (ficha.esRural()) {
            puntaje += 1;
            detalle += "Vive en zona rural (+1). ";
        }
        int edad = estudiante.calcularEdad();
        if (edad == 16 || edad == 17) {
            puntaje += 1;
            detalle += "Tiene " + edad + " años (+1). ";
        }
        if (ficha.isRepitioAnio()) {
            puntaje += 1;
            detalle += "Ha repetido año (+1). ";
        }
        if (detalle.isEmpty()) {
            detalle = "No presenta factores de riesgo.";
        }
        estudiante.actualizarRiesgo(puntaje, obtenerNivel(puntaje), detalle.trim());
    }

    /** Bajo (0–2), Medio (3–5), Alto (6–7) o Crítico (8–10). */
    public String obtenerNivel(int puntaje) {
        if (puntaje <= 2) {
            return Estudiante.NIVEL_BAJO;
        } else if (puntaje <= 5) {
            return Estudiante.NIVEL_MEDIO;
        } else if (puntaje <= 7) {
            return Estudiante.NIVEL_ALTO;
        }
        return Estudiante.NIVEL_CRITICO;
    }

    /** Recalcula manualmente el riesgo de todos los estudiantes. */
    public String recalcularTodos() {
        for (Estudiante e : estudiantes) {
            calcularRiesgo(e);
        }
        return "Se recalculó el nivel de riesgo de " + estudiantes.size() + " estudiantes.";
    }

    /** El DECE puede cambiar el umbral de ingreso; al hacerlo se recalcula el riesgo de todos. */
    public String cambiarUmbral(double nuevoUmbral) throws PuenteException {
        if (nuevoUmbral <= 0) {
            throw new PuenteException("El umbral debe ser mayor que cero.");
        }
        umbralIngresoPorPersona = nuevoUmbral;
        recalcularTodos();
        return "Umbral actualizado a USD " + String.format("%.2f", nuevoUmbral) + ". Se recalculó el riesgo de "
                + estudiantes.size() + " estudiantes.";
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo
    // ------------------------------------------------------------------

    /** Devuelve el estudiante con esa cédula o null si no existe. */
    public Estudiante buscarPorCedula(String cedula) {
        for (Estudiante e : estudiantes) {
            if (e.getCedula().equals(cedula)) {
                return e;
            }
        }
        return null;
    }

    /** Igual que buscarPorCedula, pero lanza una excepción si no existe. */
    public Estudiante obtenerEstudiante(String cedula) throws PuenteException {
        Estudiante e = buscarPorCedula(cedula);
        if (e == null) {
            throw new PuenteException("No existe un estudiante con la cédula " + cedula + ".");
        }
        return e;
    }

    public ArrayList<Estudiante> listarActivos() {
        ArrayList<Estudiante> activos = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.estaActivo()) {
                activos.add(e);
            }
        }
        return activos;
    }

    private void validarDatos(Estudiante e, boolean esNuevo) throws PuenteException {
        if (e == null) {
            throw new PuenteException("No se recibieron datos del estudiante.");
        }
        if (estaVacio(e.getCedula()) || estaVacio(e.getNombres()) || estaVacio(e.getApellidos())
                || estaVacio(e.getCurso()) || estaVacio(e.getParalelo()) || estaVacio(e.getDireccion())
                || estaVacio(e.getSector()) || e.getFechaNacimiento() == null) {
            throw new PuenteException("Complete todos los datos obligatorios del estudiante.");
        }
        if (!e.getCedula().matches("\\d{10}")) {
            throw new PuenteException("La cédula debe tener 10 dígitos.");
        }
        if (esNuevo && buscarPorCedula(e.getCedula()) != null) {
            throw new PuenteException("Ya existe un estudiante registrado con la cédula " + e.getCedula() + ".");
        }
        int edad = e.calcularEdad();
        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            throw new PuenteException("La edad del estudiante (" + edad + " años) debe estar entre "
                    + EDAD_MINIMA + " y " + EDAD_MAXIMA + " años.");
        }
        Representante r = e.getRepresentante();
        if (r == null || estaVacio(r.getNombres()) || estaVacio(r.getApellidos()) || estaVacio(r.getTelefono())) {
            throw new PuenteException("Complete el nombre y el teléfono del representante.");
        }
        if (!r.getTelefono().matches("\\d{7,10}")) {
            throw new PuenteException("El teléfono del representante debe tener entre 7 y 10 dígitos.");
        }
        FichaSocioeconomica f = e.getFicha();
        if (f == null || estaVacio(f.getZona())) {
            throw new PuenteException("Complete la ficha socioeconómica (zona urbana o rural).");
        }
        if (f.getMiembrosHogar() < 1) {
            throw new PuenteException("El número de miembros del hogar debe ser al menos 1.");
        }
        if (f.getIngresoMensualHogar() < 0) {
            throw new PuenteException("El ingreso mensual del hogar no puede ser negativo.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public double getUmbralIngresoPorPersona() {
        return umbralIngresoPorPersona;
    }
}
