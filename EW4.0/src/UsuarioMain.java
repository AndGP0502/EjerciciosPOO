public class UsuarioMain {

    public static void main(String[] args) {

        Usuario usu1 = new Usuario("Andre", "andregarzon0502@gmail.com");
        Usuario usu2 = new Usuario("Blessd", "Steven.blessd@udla.edu.ec");
        Usuario usu3 = new Usuario("Elon", "Elon@correo.com");
        Usuario usu4 = new Usuario("Musk", "Musk@correo.com");

        usu1.mostrarInformacion();
        usu2.mostrarInformacion();
        usu3.mostrarInformacion();
        usu4.mostrarInformacion();

        Usuario.mostrarTotal();
    }
}