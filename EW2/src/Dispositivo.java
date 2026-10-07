public class Dispositivo {

    public String nombre;
    String tipo;
    public boolean activo;

    public void mostrarInformacion(){

        System.out.println("Nombre: " + nombre);
        System.out.println("Precio: " + tipo);
    }

    void mostrarEstado(){
        System.out.println("Estado: " + activo);
    }

}
