public class Main {

    public static void main(String[] args) {

        Sensor.mostrarTotal();

        Sensor sensor1 = new Sensor("DHT22", "Temperatura");
        Sensor.mostrarTotal();

        Sensor sensor2 = new Sensor("MQ-135", "Gas");
        Sensor sensor3 = new Sensor("HC-SR04", "Distancia");
        Sensor.mostrarTotal();

        sensor1.setLectura(24.5);
        sensor2.setLectura(310);
        sensor3.setLectura(15.8);

        System.out.println();
        System.out.println(" sensor 1 ");
        sensor1.mostrarInformacion();

        System.out.println();
        System.out.println(" sensor 2 ");
        sensor2.mostrarInformacion();

        System.out.println();
        System.out.println(" sensor 3 ");
        sensor3.mostrarInformacion();

        System.out.println();
        // Prueba de validación: este valor se rechaza
        sensor1.setLectura(-10);
        System.out.println("Lectura del sensor 1: " + sensor1.getLectura());
    }
}