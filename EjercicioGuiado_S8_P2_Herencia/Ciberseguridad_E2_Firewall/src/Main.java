import modelo.*;

public class Main {
    public static void main(String[] args) {
        Firewall firewall = new Firewall("Firewall corporativo");
        Administrador admin = new Administrador("Roberto Cabrera");

        // Las reglas se crean fuera del firewall y se agregan después
        Regla r1 = new ReglaPuerto("Permitir HTTPS", "PERMITIR", 3, 443);
        Regla r2 = new ReglaIP("Bloquear IP maliciosa", "BLOQUEAR", 1, "203.0.113.50");
        Regla r3 = new ReglaProtocolo("Bloquear Telnet", "BLOQUEAR", 2, "TELNET");
        Regla r4 = new ReglaIP("Permitir IP de la oficina", "PERMITIR", 4, "192.168.1.10");
        Regla r5 = new ReglaPuerto("Bloquear puerto 443 temporal", "BLOQUEAR", 6, 443);

        firewall.agregarRegla(r1);
        firewall.agregarRegla(r2);
        firewall.agregarRegla(r3);
        firewall.agregarRegla(r4);
        firewall.agregarRegla(r5);

        System.out.println("===== PRUEBA 1: Reglas agregadas (orden de ingreso) =====");
        System.out.println("Reglas en " + firewall.getNombre() + ": " + firewall.contarReglas());
        System.out.print(firewall.listarReglas());

        System.out.println();
        System.out.println("===== PRUEBA 2: Reglas priorizadas =====");
        firewall.priorizarReglas();
        System.out.print(firewall.listarReglas());

        System.out.println();
        System.out.println("===== PRUEBA 3: Evaluar trafico simulado =====");
        System.out.println(firewall.evaluarTrafico("203.0.113.50", 443, "HTTPS"));
        System.out.println(firewall.evaluarTrafico("192.168.1.10", 80, "HTTP"));
        System.out.println(firewall.evaluarTrafico("10.0.0.8", 23, "TELNET"));
        System.out.println(firewall.evaluarTrafico("10.0.0.9", 443, "HTTPS"));
        System.out.println(firewall.evaluarTrafico("10.0.0.9", 8080, "TCP"));

        System.out.println();
        System.out.println("===== PRUEBA 4: Detectar conflictos =====");
        System.out.print(firewall.detectarConflictos());

        System.out.println();
        System.out.println("===== PRUEBA 5: El administrador corrige la regla en conflicto =====");
        System.out.println(admin.editarRegla(r5, "PERMITIR", 4));
        System.out.print(firewall.detectarConflictos());
        firewall.priorizarReglas();
        System.out.print(firewall.listarReglas());
        System.out.println("Cambios realizados por el administrador: " + admin.getCambiosRealizados());
    }
}
