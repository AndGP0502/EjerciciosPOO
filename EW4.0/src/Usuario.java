public class Usuario {

    private int id;
    private String username;
    private String correo;
    private boolean activo;

    private static int contadorUsuarios = 0;

    public Usuario(String username, String correo){

        this.username = username;
        this.correo = correo;

        contadorUsuarios++;
        this.id = contadorUsuarios;

        if(username != null && !username.isBlank() && correo != null && !correo.isBlank())
        {
            activo = true;
        }
    }

    public int getId(){

        return id;
    }

    public String getUsername(){

        return username;
    }

    public String getCorreo(){

        return correo;
    }

    public boolean getActivo(){

        return activo;
    }

    public static int getContadorUsuarios()
    {
        return contadorUsuarios;
    }

    public void mostrarInformacion(){

        System.out.println("ID: " + getId());
        System.out.println("Username: " + getUsername());
        System.out.println("Correo: " + getCorreo());
        System.out.println("Activo: " + getActivo());
        System.out.println();
    }

    public static void mostrarTotal(){

        System.out.println("Total de usuarios: " + getContadorUsuarios());
    }
}