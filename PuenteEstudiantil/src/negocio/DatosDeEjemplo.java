package negocio;

import modelo.CasoSeguimiento;
import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Intervencion;
import modelo.ProfesionalDece;
import modelo.ProgramaApoyo;
import modelo.Representante;

import java.time.LocalDate;

/**
 * Carga datos ficticios para poder probar el sistema sin escribir todo a mano.
 * Las fechas se calculan a partir de hoy, así las edades y las vigencias siempre son válidas.
 * Todos los nombres, cédulas y teléfonos son inventados.
 */
public class DatosDeEjemplo {

    public static final String ANIO_LECTIVO = "2026-2027";

    public static void cargar(SistemaPuente sistema) throws PuenteException {
        LocalDate hoy = LocalDate.now();

        // ---------------- Profesionales del DECE ----------------
        ProfesionalDece gabriela = new ProfesionalDece("1720000011", "Gabriela", "Torres Benítez", "0991112233",
                ProfesionalDece.CARGO_PROFESIONAL, "gabriela.torres@colegio.edu.ec");
        ProfesionalDece diego = new ProfesionalDece("1720000029", "Diego", "Andrade Vaca", "0992223344",
                ProfesionalDece.CARGO_PROFESIONAL, "diego.andrade@colegio.edu.ec");
        ProfesionalDece patricia = new ProfesionalDece("1720000037", "Patricia", "Salazar Mena", "0993334455",
                ProfesionalDece.CARGO_COORDINADOR, "patricia.salazar@colegio.edu.ec");
        sistema.agregarProfesional(gabriela);
        sistema.agregarProfesional(diego);
        sistema.agregarProfesional(patricia);

        // ---------------- Módulo 1: estudiantes ----------------
        GestorEstudiantes ge = sistema.getGestorEstudiantes();
        // (cédula, nombres, apellidos, años, meses extra, curso, paralelo, sector, zona,
        //  miembros, ingreso, trabaja, hijos/cuida, repitió, representante, teléfono, parentesco)
        Estudiante kevin = crear(ge, hoy, "1750000011", "Kevin Andrés", "Quishpe Toapanta", 16, 3, "2do BGU", "A",
                "Guamaní", FichaSocioeconomica.ZONA_URBANA, 6, 380, true, false, true,
                "Rosa Elena", "Toapanta Chicaiza", "0987001001", "Madre");
        Estudiante mariaJose = crear(ge, hoy, "1750000029", "María José", "Chicaiza Llumiquinga", 17, 2, "3ro BGU", "B",
                "Amaguaña", FichaSocioeconomica.ZONA_RURAL, 5, 300, true, true, false,
                "Luis Alberto", "Chicaiza Pillajo", "0987001002", "Padre");
        Estudiante bryan = crear(ge, hoy, "1750000037", "Bryan Steven", "Tipán Guamán", 15, 6, "1ro BGU", "A",
                "Pifo", FichaSocioeconomica.ZONA_RURAL, 4, 420, true, false, true,
                "Carmen Lucía", "Guamán Ortiz", "0987001003", "Madre");
        Estudiante dayana = crear(ge, hoy, "1750000045", "Dayana Lizbeth", "Pilataxi Caiza", 13, 4, "9no EGB", "A",
                "Quitumbe", FichaSocioeconomica.ZONA_URBANA, 5, 500, false, false, false,
                "Jorge Iván", "Pilataxi Lema", "0987001004", "Padre");
        Estudiante jhon = crear(ge, hoy, "1750000052", "Jhon Alexander", "Morocho Simbaña", 16, 8, "2do BGU", "A",
                "Lloa", FichaSocioeconomica.ZONA_RURAL, 5, 250, true, false, true,
                "Blanca Inés", "Simbaña Quinatoa", "0987001005", "Madre");
        Estudiante anahi = crear(ge, hoy, "1750000060", "Anahí Valentina", "Cuichán Farinango", 14, 1, "10mo EGB", "B",
                "Calderón", FichaSocioeconomica.ZONA_URBANA, 6, 350, false, true, false,
                "Martha Cecilia", "Farinango Tupiza", "0987001006", "Abuela");
        Estudiante josue = crear(ge, hoy, "1750000078", "Josué David", "Ushiña Pillajo", 17, 5, "3ro BGU", "A",
                "Conocoto", FichaSocioeconomica.ZONA_RURAL, 4, 600, true, false, false,
                "Segundo Manuel", "Ushiña Caiza", "0987001007", "Padre");
        Estudiante erika = crear(ge, hoy, "1750000086", "Erika Fernanda", "Sangucho Lema", 16, 10, "2do BGU", "B",
                "Chillogallo", FichaSocioeconomica.ZONA_URBANA, 4, 280, false, true, false,
                "Gloria Patricia", "Lema Guachamín", "0987001008", "Madre");
        Estudiante luis = crear(ge, hoy, "1750000094", "Luis Ángel", "Caiza Iza", 12, 7, "8vo EGB", "A",
                "La Roldós", FichaSocioeconomica.ZONA_URBANA, 5, 460, false, false, false,
                "Rosa María", "Iza Lincango", "0987001009", "Madre");
        crear(ge, hoy, "1750000102", "Camila Estefanía", "Guachamín Sánchez", 15, 2, "1ro BGU", "B",
                "Carapungo", FichaSocioeconomica.ZONA_URBANA, 4, 800, false, false, false,
                "Paúl Fernando", "Guachamín Ruiz", "0987001010", "Padre");
        Estudiante mateo = crear(ge, hoy, "1750000110", "Mateo Sebastián", "Lincango Tupiza", 18, 4, "3ro BGU", "A",
                "Nanegalito", FichaSocioeconomica.ZONA_RURAL, 5, 320, true, false, true,
                "Ana Lucía", "Tupiza Morales", "0987001011", "Madre");
        crear(ge, hoy, "1750000128", "Carlos Andrés", "Topón Quinatoa", 17, 0, "3ro BGU", "B",
                "Tumbaco", FichaSocioeconomica.ZONA_RURAL, 3, 450, false, false, false,
                "Edgar Patricio", "Topón Vega", "0987001012", "Padre");
        ge.darDeBaja("1750000128", "Traslado", hoy.minusDays(30));

        // ---------------- Módulo 2: programas de apoyo ----------------
        GestorProgramas gp = sistema.getGestorProgramas();
        LocalDate inicioAnio = hoy.minusMonths(2);
        LocalDate finAnio = hoy.plusMonths(7);
        ProgramaApoyo utiles = new ProgramaApoyo("Kit de útiles escolares", ProgramaApoyo.TIPOS[0],
                "Cuadernos, esferos y materiales básicos para el año lectivo.", 6, inicioAnio, finAnio,
                Estudiante.NIVEL_MEDIO);
        ProgramaApoyo refrigerio = new ProgramaApoyo("Refrigerio diario", ProgramaApoyo.TIPOS[2],
                "Refrigerio en la jornada matutina donado por una fundación.", 3, inicioAnio, finAnio,
                Estudiante.NIVEL_ALTO);
        ProgramaApoyo transporte = new ProgramaApoyo("Ayuda de transporte rural", ProgramaApoyo.TIPOS[3],
                "Pasajes mensuales para estudiantes que viven en parroquias rurales.", 4, inicioAnio, finAnio,
                Estudiante.NIVEL_MEDIO);
        ProgramaApoyo tutorias = new ProgramaApoyo("Tutorías de refuerzo", ProgramaApoyo.TIPOS[4],
                "Refuerzo pedagógico los martes y jueves después de clases.", 8, inicioAnio, finAnio,
                Estudiante.NIVEL_BAJO);
        ProgramaApoyo uniformes = new ProgramaApoyo("Uniformes donados", ProgramaApoyo.TIPOS[1],
                "Entrega de uniformes del año lectivo anterior.", 2, hoy.minusMonths(9), hoy.minusMonths(4),
                Estudiante.NIVEL_MEDIO);
        gp.registrarPrograma(utiles);
        gp.registrarPrograma(refrigerio);
        gp.registrarPrograma(transporte);
        gp.registrarPrograma(tutorias);
        gp.registrarPrograma(uniformes);

        gp.asignarApoyo(kevin, utiles, hoy.minusDays(50));
        gp.asignarApoyo(mariaJose, utiles, hoy.minusDays(50));
        gp.asignarApoyo(jhon, utiles, hoy.minusDays(49));
        gp.asignarApoyo(erika, utiles, hoy.minusDays(48));
        gp.asignarApoyo(anahi, utiles, hoy.minusDays(40));

        gp.asignarApoyo(mariaJose, refrigerio, hoy.minusDays(45));
        gp.asignarApoyo(jhon, refrigerio, hoy.minusDays(45));
        gp.asignarApoyo(kevin, refrigerio, hoy.minusDays(44));
        gp.asignarApoyo(mateo, refrigerio, hoy.minusDays(30));   // sin cupos: lista de espera
        gp.asignarApoyo(erika, refrigerio, hoy.minusDays(20));   // sin cupos: lista de espera

        gp.asignarApoyo(jhon, transporte, hoy.minusDays(40));
        gp.asignarApoyo(mariaJose, transporte, hoy.minusDays(40));
        gp.asignarApoyo(bryan, transporte, hoy.minusDays(35));

        gp.asignarApoyo(bryan, tutorias, hoy.minusDays(15));
        gp.asignarApoyo(luis, tutorias, hoy.minusDays(15));

        gp.asignarApoyo(josue, uniformes, hoy.minusMonths(8));
        gp.cerrarPrograma(uniformes.getCodigo());

        // ---------------- Módulo 3: casos de seguimiento ----------------
        GestorCasos gc = sistema.getGestorCasos();
        CasoSeguimiento caso1 = gc.abrirCaso(mariaJose, "Inasistencias frecuentes y trabajo informal en las tardes",
                gabriela, hoy.minusDays(40), "Reportado por la docente tutora.", "");
        gc.registrarIntervencion(caso1, new Intervencion(hoy.minusDays(38), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "Indica que trabaja en un mercado para ayudar en casa."), null, null);
        gc.registrarIntervencion(caso1, new Intervencion(hoy.minusDays(30), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, "El representante no contestó."), null, null);
        gc.registrarIntervencion(caso1, new Intervencion(hoy.minusDays(20), Intervencion.TIPO_VISITA_HOGAR,
                Intervencion.RESULTADO_EFECTIVA, "Se acordó un horario de trabajo que no cruce con clases."), null, null);

        CasoSeguimiento caso2 = gc.abrirCaso(jhon, "Reporte del docente tutor: faltas y notas bajas", diego,
                hoy.minusDays(25), "", "");
        gc.registrarIntervencion(caso2, new Intervencion(hoy.minusDays(24), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, "Teléfono apagado."), null, null);
        gc.registrarIntervencion(caso2, new Intervencion(hoy.minusDays(20), Intervencion.TIPO_LLAMADA,
                Intervencion.RESULTADO_SIN_RESPUESTA, "No contesta."), null, null);

        CasoSeguimiento caso3 = gc.abrirCaso(erika, "Responsabilidades de cuidado de su hijo y riesgo de retiro",
                gabriela, hoy.minusDays(60), "", "");
        gc.registrarIntervencion(caso3, new Intervencion(hoy.minusDays(58), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "Se identificó la necesidad de apoyo con el cuidado del niño."), null, null);
        gc.registrarIntervencion(caso3, new Intervencion(hoy.minusDays(50), Intervencion.TIPO_ENTREVISTA_REPRESENTANTE,
                Intervencion.RESULTADO_EFECTIVA, "La madre se compromete a cuidar al niño en la mañana."), null, null);
        gc.registrarIntervencion(caso3, new Intervencion(hoy.minusDays(35), Intervencion.TIPO_ENTREVISTA_ESTUDIANTE,
                Intervencion.RESULTADO_EFECTIVA, "Asiste con regularidad."), CasoSeguimiento.ESTADO_RESUELTO,
                "La estudiante asiste con regularidad desde hace un mes; se cierra el caso.");

        gc.abrirCaso(anahi, "Llega tarde con frecuencia y se ve cansada", diego, hoy.minusDays(10), "",
                "La tutora reporta que cuida a sus hermanos menores por las mañanas.");

        // ---------------- Módulo 5: calificaciones del primer periodo ----------------
        GestorRendimiento gr = sistema.getGestorRendimiento();
        LocalDate fechaNotas = hoy.minusDays(5);
        registrarNotas(gr, jhon, fechaNotas, new String[]{"Matemática", "Lengua y Literatura", "Física", "Química",
                "Inglés", "Historia"}, new double[]{5.20, 6.40, 4.80, 6.90, 7.50, 7.10});
        registrarNotas(gr, mariaJose, fechaNotas, new String[]{"Matemática", "Lengua y Literatura", "Biología",
                "Inglés", "Historia"}, new double[]{6.50, 7.80, 7.20, 8.00, 7.40});
        registrarNotas(gr, kevin, fechaNotas, new String[]{"Matemática", "Física", "Lengua y Literatura", "Química",
                "Inglés"}, new double[]{6.80, 6.20, 7.00, 7.30, 6.90});
        registrarNotas(gr, dayana, fechaNotas, new String[]{"Matemática", "Lengua y Literatura", "Ciencias Naturales",
                "Estudios Sociales", "Inglés"}, new double[]{9.10, 8.70, 9.00, 8.80, 9.30});
        registrarNotas(gr, erika, fechaNotas, new String[]{"Matemática", "Lengua y Literatura", "Física"},
                new double[]{7.40, 8.10, 7.20});
        registrarNotas(gr, luis, fechaNotas, new String[]{"Matemática", "Lengua y Literatura", "Ciencias Naturales",
                "Estudios Sociales"}, new double[]{6.00, 7.50, 7.80, 8.00});
    }

    private static Estudiante crear(GestorEstudiantes ge, LocalDate hoy, String cedula, String nombres,
                                    String apellidos, int anios, int mesesExtra, String curso, String paralelo,
                                    String sector, String zona, int miembros, double ingreso, boolean trabaja,
                                    boolean hijosOCuida, boolean repitio, String nombresRep, String apellidosRep,
                                    String telefonoRep, String parentesco) throws PuenteException {
        Representante rep = new Representante("", nombresRep, apellidosRep, telefonoRep, parentesco);
        FichaSocioeconomica ficha = new FichaSocioeconomica(zona, miembros, ingreso, trabaja, hijosOCuida, repitio);
        LocalDate nacimiento = hoy.minusYears(anios).minusMonths(mesesExtra).minusDays(3);
        Estudiante e = new Estudiante(cedula, nombres, apellidos, "", nacimiento, curso, paralelo,
                "Barrio " + sector, sector, "", rep, ficha);
        ge.registrarEstudiante(e);
        return e;
    }

    private static void registrarNotas(GestorRendimiento gr, Estudiante e, LocalDate fecha, String[] asignaturas,
                                       double[] notas) throws PuenteException {
        for (int i = 0; i < asignaturas.length; i++) {
            gr.registrarCalificacion(e, ANIO_LECTIVO, 1, asignaturas[i], notas[i], fecha);
        }
    }
}
