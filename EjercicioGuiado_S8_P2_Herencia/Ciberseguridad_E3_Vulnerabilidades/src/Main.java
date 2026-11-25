import modelo.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Auditor auditor = new Auditor("Patricia Molina");

        // Registrar equipos
        Equipo servidor = new Equipo("EQ-01", "Servidor Web");
        Equipo router = new Equipo("EQ-02", "Router Principal");
        Equipo laptop = new Equipo("EQ-03", "Laptop de Gerencia");

        ArrayList<Equipo> equipos = new ArrayList<>();
        equipos.add(servidor);
        equipos.add(router);
        equipos.add(laptop);

        // Registrar vulnerabilidades por equipo (referencias de tipo Vulnerabilidad)
        servidor.registrarVulnerabilidad(new VulnerabilidadSoftware("VUL-001",
                "Inyeccion SQL en el panel de administracion", 8, "Red", "Baja", "2.1.4", true));
        servidor.registrarVulnerabilidad(new VulnerabilidadSoftware("VUL-002",
                "Libreria de cifrado desactualizada", 5, "Red", "Media", "1.0.2", false));
        servidor.registrarVulnerabilidad(new VulnerabilidadSoftware("VUL-003",
                "Credenciales por defecto en el servicio de respaldo", 9, "Red", "Baja", "3.5.0", false));

        router.registrarVulnerabilidad(new VulnerabilidadHardware("VUL-004",
                "Puerto de depuracion expuesto en la placa", 6, "Fisico", "Alta", "Placa base"));

        laptop.registrarVulnerabilidad(new VulnerabilidadSoftware("VUL-005",
                "Escalamiento de privilegios en el sistema operativo", 4, "Local", "Media", "10.0.1", true));
        laptop.registrarVulnerabilidad(new VulnerabilidadHardware("VUL-006",
                "Firmware de la BIOS desactualizado", 7, "Local", "Media", "BIOS"));

        System.out.println("===== PRUEBA 1: Vulnerabilidades registradas por equipo =====");
        for (Equipo e : equipos) {
            System.out.println(e.getNombre() + " - Vulnerabilidades registradas: " + e.contarVulnerabilidades());
            System.out.print(e.listarVulnerabilidades());
            System.out.println();
        }

        System.out.println("===== PRUEBA 2: Riesgo acumulado por equipo =====");
        for (Equipo e : equipos) {
            System.out.println(auditor.evaluarRiesgoEquipo(e));
        }

        System.out.println();
        System.out.println("===== PRUEBA 3: Riesgo total evaluado por el auditor =====");
        System.out.println(auditor.evaluarRiesgoTotal(equipos));

        System.out.println();
        System.out.println("===== PRUEBA 4: Recomendaciones por equipo =====");
        for (Equipo e : equipos) {
            System.out.println(e.getNombre() + ":");
            System.out.print(e.generarRecomendaciones());
        }
    }
}
