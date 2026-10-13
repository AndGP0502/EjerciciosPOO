public class MainMotor {

    public static void main(String[] args) {

        Motor motor1 = new Motor();

        motor1.setNombre("Porsche Cayenne biturbo");

        motor1.setPotencia(4.0);

        motor1.setVelocidad(99);


        motor1.encender();
        motor1.apagar();

    }
}
