public class MainMotor {

    static void main(String[] args) {

        Motor motor1 = new Motor();
        Motor motor2 = new Motor();
        Motor motor3 = new Motor();

        motor1.potencia = 4.0;
        motor1.nombre = "4.0-litre biturbo V8 engine";

        motor1.mostrarNombre();
        motor1.encender();
        motor1.mostrarEstado();
        motor1.mostrarInformacion();
        motor1.apagar();
    }
}
