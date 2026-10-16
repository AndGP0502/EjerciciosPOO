public class Motor {

    public String nombre;
    public double potencia;
    public boolean encendido;
    public String estado;
    public boolean apagado;

    public void mostrarNombre(){

        System.out.println("El nombre de este motor, es: " + nombre);
    }

    public void encender(){

        encendido = true;

        System.out.println("¿Está encendido?: " + encendido);
    }

    public void mostrarInformacion(){

        System.out.println("Potencia de motor: " + potencia + "w");

    }

    void apagar(){

        encendido = false;
        apagado = true;

        System.out.println("El motor está apagado: " + apagado);
    }

    void mostrarEstado(){

        if(encendido) {

            System.out.println("Estado: Encendido y funcionando");
        }
        else {

            System.out.println("Estado: El motor está apagado");
        }
    }

}
