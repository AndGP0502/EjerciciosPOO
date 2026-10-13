public class MainServicio {

    public static void main(String[] args) {

        Servicio servicio1 = new Servicio();

        servicio1.setNombre("Software personalizado");
        servicio1.setPuerto(1);

        servicio1.iniciar();
        System.out.println("Activo? " + servicio1.isActivo());

        servicio1.registrarPeticion();
        servicio1.registrarPeticion();
        System.out.println("Peticiones: " + servicio1.getCantidadPeticiones());

        servicio1.detener();
        servicio1.registrarPeticion();    // no cuenta porque ya está detenido
        System.out.println("Activo? " + servicio1.isActivo());
        System.out.println("Peticiones: " + servicio1.getCantidadPeticiones());
    }
}