package negocio;

public class Carro {
    private int potencia;
    private double velocidad;

    /*
    métodos para establecer valor
    set()
    "siempre" es void
    siempre recibe párametro
    parámetro generalmente es del mismo tipo del atributo
     */
    public void setPotencia(int potencia){
        //asignar solo cuando sea correcto
        if (potencia > 0)
            this.potencia = potencia;
    }

    public void setVelocidad(double velocidad){
        //asignar si es correcto sino poner un valor defecto
        if(velocidad < 0)
            velocidad = 0;
        this.velocidad = velocidad;
    }

    /*
    métodos para obtener el valor
    get()
    siempre retornan valor
    el tipo de retorno generalmente es del mismo tipo del atributo
     */

    public int getPotencia(){
        return potencia;
    }

    public double getVelocidad(){
        return velocidad;
    }
    public void acelerar(){
        velocidad += potencia;
    }

    void frenar(){
        velocidad /= 2;
    }
}
