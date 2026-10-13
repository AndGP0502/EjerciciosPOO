public class Motor {

    private String nombre;
    private double potencia;
    private boolean encendido;
    private int velocidad;

    public String getNombre(){

        return nombre;
    }

    public void setNombre(String nombre){

        this.nombre = nombre;

        System.out.println("El nombre es: " + nombre);
    }

    public double getPotencia(){

        return potencia;
    }

    public void setPotencia(double potencia){

        if(potencia >=0)
        {
            this.potencia = potencia;

            System.out.println("La potencia es de: " + potencia);
        }
        else {
            System.out.println("La potencia debe ser 0 o mayor que 0");
        }
    }

    public int getVelocidad(){

        return velocidad;
    }

    public void setVelocidad(int velocidad){

        if(velocidad > 0 && velocidad < 100) {

            this.velocidad = velocidad;

            System.out.println("La velocidad es: " + velocidad);
        }
        else{
            System.out.println("La velocidad máxima es de 100 y mínima es de 0"); }

    }

    public boolean getEncendido(){

        return encendido;
    }


    public void encender(){

        if(nombre != null && !nombre.isBlank() && potencia > 0)
        {
            encendido = true;

            System.out.println("El motor está encendido");
        }
        else
        {
            System.out.println("El sistema está apagado porque el motor no enciende");
        }
    }


    public void apagar(){

        encendido = false;

        System.out.println("El sistema está apagado");
    }




}
