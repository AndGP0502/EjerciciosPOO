import modelo.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Sensor sensor = new Sensor("Sensor perimetral - DMZ");
        AnalistaSeguridad analista = new AnalistaSeguridad("Andrea Salazar");

        // Registrar eventos según su tipo (referencias de tipo EventoSeguridad)
        sensor.registrarEvento(new EventoRed("EVT-001", "Escaneo de puertos detectado", 3,
                "203.0.113.45", "10.0.0.12", 22, 64));
        sensor.registrarEvento(new EventoRed("EVT-002", "Trafico inusual hacia el servidor DNS", 2,
                "198.51.100.7", "10.0.0.53", 53, 4096));
        sensor.registrarEvento(new EventoAcceso("EVT-003", "Multiples intentos de inicio de sesion fallidos", 4,
                "admin", 12));
        sensor.registrarEvento(new EventoAcceso("EVT-004", "Inicio de sesion fuera de horario", 2,
                "mrojas", 0));
        sensor.registrarEvento(new EventoMalware("EVT-005", "Archivo con firma de ransomware", 5,
                "WannaLock", "contratos.xlsx"));
        sensor.registrarEvento(new EventoMalware("EVT-006", "Troyano bloqueado por el antivirus", 3,
                "TrojanBanker", "factura.pdf.exe"));

        System.out.println("===== PRUEBA 1: Detalles de los eventos registrados =====");
        System.out.println("Sensor: " + sensor.getUbicacion());
        System.out.println("Eventos registrados: " + sensor.contarEventos());
        System.out.print(sensor.mostrarEventos());

        System.out.println();
        System.out.println("===== PRUEBA 2: Filtrar por criticidad (4 o mas) =====");
        ArrayList<EventoSeguridad> criticos = sensor.filtrarPorCriticidad(4);
        for (EventoSeguridad e : criticos) {
            System.out.println(e.mostrarInformacion());
        }

        System.out.println();
        System.out.println("===== PRUEBA 3: El analista revisa y marca acciones sugeridas =====");
        System.out.print(analista.revisarEventos(sensor.getEventos()));
        System.out.println("Eventos revisados por el analista: " + analista.getEventosRevisados());

        System.out.println();
        System.out.println("===== PRUEBA 4: Eventos con la accion sugerida ya marcada =====");
        System.out.print(sensor.mostrarEventos());
    }
}
