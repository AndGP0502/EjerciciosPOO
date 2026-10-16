public class Producto {

    private String nombre;
    private String tipo;
    private boolean activo;

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre != null && !nombre.isBlank()){
            this.nombre = nombre;
        }
        else{
            System.out.println("Nombre inválido");
        }
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public boolean isActivo(){
        return activo;
    }

    public void setActivo(boolean activo){
        this.activo = activo;
    }
}