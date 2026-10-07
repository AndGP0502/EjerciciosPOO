public class Main {

    public  static void main(String[] args) {

        Dispositivo dispositivo1 = new Dispositivo ();
        Dispositivo dispositivo2 = new Dispositivo ();

        dispositivo1.nombre = "MacMini";
        dispositivo1.tipo = "tecnología";
        dispositivo1.activo = true;

        dispositivo1.nombre = "Dell";

        dispositivo1.mostrarInformacion();
        dispositivo1.mostrarEstado();

    }
}
