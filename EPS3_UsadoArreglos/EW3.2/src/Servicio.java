public class Servicio {

    private String nombre;
    private int puerto;
    private boolean activo;
    private int cantidadPeticiones;

    public String getNombre(){

        return nombre;
    }

    public void setNombre(String nombre){

        if(nombre != null && !nombre.isBlank())
        {
            this.nombre = nombre;
        }
    }

    public int getPuerto(){

        return puerto;
    }

    public void setPuerto(int puerto){

        if(puerto > 0){
            this.puerto = puerto;
        }
    }

    public int getCantidadPeticiones(){

        return cantidadPeticiones;
    }

    public void setCantidadPeticiones(int cantidadPeticiones){

        if(cantidadPeticiones >= 0)
        {

            this.cantidadPeticiones = cantidadPeticiones;
        }

    }

    public void registrarPeticion(){

        if(activo)
        {
            cantidadPeticiones++;
        }
    }

    public boolean isActivo(){

        return activo;
    }

    public void iniciar(){

        if(nombre != null && puerto > 0)
        {
            activo = true;
        }
    }

    public void detener(){

        activo = false;
    }


}
