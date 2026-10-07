public class Aplicacion {

    public String nombre;
    String categoria;
    public int descargas;


    public void mostrarInformacion(){
        System.out.println("Nombre: " + nombre);
        System.out.println("Categoria: " + categoria);
        System.out.println("Descargas: " + descargas);
    }

    void mostrarCategoria(){

        System.out.println("Categoria: " + categoria);
    }
}

