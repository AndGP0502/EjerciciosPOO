public class CanalTelimetria {

    private double IdeN;

    private String nombre;

    private double valorI;

    private static int CanalesC = 0;


    public double getIdeN(){

        return IdeN;
    }

    public String getNombre(){

        return nombre;
    }


    public void setIdeN(double numID)
    {
        IdeN = numID;
    }

    public void setNombre(String nombre){

        this.nombre = nombre;
    }



    public CanalTelimetria(int IdeN, String nombre, double valorI, double UVM)

    {

        setIdeN(IdeN);
        setNombre(nombre);

        valorI = -1;
        UVM = valorI;

    }

    public void mostrarNombre(){

        System.out.println("Nombre: " + nombre);
    }

    public void mostrarIde(){

        System.out.println("Identidifacor: " + IdeN);
    }

    public boolean registrarMedicion(double valor){

        if(IdeN > 0 && nombre != null && !nombre.isBlank() && valor >= 0 && valor < 100)
        {

            CanalesC ++;


            return true;
        }
        else
        {
            System.out.println("Medicion invalida ");

            return false;
        }
    }

    public void obtenerEstadoMedicion(){

        System.out.println("Meedicoion: " + nombre);
        System.out.println("Total de canales: " + CanalesC);
        System.out.println("Valor de la cadena de mediocn: " + valorI);
    }

    public int getCanalesC(){

        return CanalesC;
    }



}
