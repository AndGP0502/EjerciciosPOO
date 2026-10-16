

public class Docente {

    private String nombre;
    private long documento;
    private String telefono;
    private String email;

    public String getNombre(){

        return nombre;
    }
    public String getTelefono(){
        return telefono;
    }
    public String getEmail(){
        return email;
    }
    public long getDocumento(){
        return documento;
    }

    public void setNombre(String nombre){
        if(nombre != null && !nombre.isBlank())
        {
            this.nombre = nombre;
        }
        else{
            System.out.println("Es un dato invalido");
        }
    }
    public void setDocumento(long documento){
        if(documento > 0)
        {
            this.documento = documento;
        }
        else {
            System.out.println("Los documentos deben ser más de 0");
        }
    }
    public void setTelefono(String telefono){
        if(telefono != null && !telefono.isBlank() && telefono.length() == 10)
        {
            this.telefono = telefono;
        }
        else {
            System.out.println("Tú número de teléfono es inválido");
        }
    }

    public void setEmail(String email){
        if(email != null && !email.isBlank() && email.contains("@"))
        {
            this.email = email;
        }
        else{
            System.out.println("Es algo invalido");
        }
    }

    public Docente(String nombre, long documento, String telefono, String email){

        setNombre(nombre);
        setDocumento(documento);
        setTelefono(telefono);
        setEmail(email);

    }


}
