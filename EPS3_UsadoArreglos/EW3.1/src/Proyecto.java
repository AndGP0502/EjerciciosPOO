public class Proyecto {

    private String nombre;
    private int progreso;
    private String responsable;
    private boolean terminado;

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        if(nombre != null && !nombre.isBlank())
        {
            this.nombre = nombre;

            System.out.println("El nombre es: " + nombre);
        } else {
            System.out.println("Nombre invalido");
        }
    }

    public int getProgreso() {

        return progreso;
    }


    public String getResponsable() {

        return responsable;
    }

    public void setResponsable(String responsable) {

        if(responsable != null && !responsable.isBlank())
        {
            this.responsable = responsable;

            System.out.println("Responsable: " + responsable);
        } else {
            System.out.println("El responsable es inválido");
        }
    }


    public void avanzar(int porcentaje){

        if(porcentaje > 0 ){

            progreso += porcentaje;
            if(progreso >= 100) {

                progreso = 100;

                System.out.println("A llegado a su " + progreso + "%");
            }

            System.out.println("El avance del progreso es de: " + progreso);
            actualizarTerminado();
        } else {
            System.out.println("El avance debe ser mayor a cero para que haya progreso");
        }
    }

    public boolean isTerminado() {

        return terminado;
    }


    public void actualizarTerminado() {

        if(progreso == 100)
        {
            terminado = true;
        } else {

            terminado = false;
        }


    }





}
