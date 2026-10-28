public class AppStore {

    private String nombre;
    private String categoria;
    private double memoria;
    private double precio;
    private int cantLic;
    private int cantLicVen;

    private static String nombres[] = new String[100];
    private static int totalNombres = 0;

    private static int totalRompecabezas = 0;
    private static int totalAccion = 0;
    private static int totalDeporte = 0;



    public String getNombre(){
        return nombre;
    }
    public String getCategoria(){
        return categoria;
    }
    public double getMemoria(){
        return memoria;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantLic(){
        return cantLic;
    }
    public int getCantLicVen(){
        return cantLicVen;
    }

    public AppStore(String ponerNombre, int ponerCate, double tMemoria, double ponerP, int licJuego){

        setNombre(ponerNombre);
        asignarCategoria(ponerCate);
        setMemoria(tMemoria);
        setPrecio(ponerP);
        cantLic = licJuego;
        cantLicVen = 0;
    }

    public static boolean existeNombre(String nuevoNombre){

        for(int i = 0; i < totalNombres; i++){

            if(nombres[i].equalsIgnoreCase(nuevoNombre))
            {
                System.out.println("Ese nombre ya existe, registra uno nuevo");

                return true;

            }
        }

        return false;
    }



    public void setNombre(String nuevoNombre){

        if(existeNombre(nuevoNombre))
        {
            System.out.println("Ese nombre ya existe, prueba uno nuevo");
        } else if (nuevoNombre == null || nuevoNombre.isBlank()) {

            System.out.println("Nombre invalido, escribe algo");

        }
        else{

            nombre = nuevoNombre;
            nombres[totalNombres] = nuevoNombre;
            totalNombres++;
        }
    }

    public void asignarCategoria(int numCat){

        switch(numCat){

            case 1:
                categoria = "rompecabezas";
                System.out.println("Categoria rompecabezas");
                totalRompecabezas++;
                break;
            case 2:
                categoria = "accion";
                System.out.println("Categoria accion");
                totalAccion++;
                break;
            case 3:
                categoria = "deporte";
                System.out.println("Categoria deporte");
                totalDeporte++;
                break;
            default:
                System.out.println("Esa categoria no existe, digita del 1 al 3");
        }
    }

    public void setMemoria(double cantMemo){

        if(cantMemo > 0){
            memoria = cantMemo;

            System.out.println("Memoria: " + cantMemo + "KB");
        }
        else{
            System.out.println("Cantidad de memoria invalida");;
        }
    }

    public void setPrecio(double precio){
        if(precio >= 0)
        {
            this.precio = precio;
        }
        else{
            System.out.println("El precio debe ser mayor o igual a 0");
        }
    }

    public void venderLic(int cantVen){

        double total = 0;

        if(cantVen > 0 && cantVen <= cantLic)
        {

            cantLic -= cantVen;
            cantLicVen += cantVen;

            total = DST(cantVen);



        }
        else {
            System.out.println(" Debes poner más que cero y menos o igual que la cantidad actual");
        }

    }

    public void comprarLic(int cantComp){

        if(cantComp>0){

            cantLic += cantComp;
        }
        else{
            System.out.println("La cantidad debe ser mayor a 0");
        }
    }

     public void mostrarInformacion(){

         System.out.println("Nombre: " + getNombre());
         System.out.println("Categoria: " + getCategoria());
         System.out.println("Memoria: " + getMemoria() + "KB");
         System.out.println("Precio: " + getPrecio());
         System.out.println("Licencias dispo: " + getCantLic());
         System.out.println("Licencias vendidas: " + getCantLicVen());
     }

     public double DST(int cantidad){

        double total = precio * cantidad;

        if(cantidad > 25){

            total = total - (total * 20/100);
        }
        else{
            System.out.println("Descuento no aplicable");
        }

        return total;
     }

    public static double venderCombo(AppStore juegoD, int cantD, AppStore juegoA, int cantA){

        if(!juegoD.getCategoria().equals("deporte") || !juegoA.getCategoria().equals("accion"))
        {
            System.out.println("El primer juego debe ser de deporte y el segundo de accion");
            return 0;
        }

        if(cantD > juegoD.getCantLic() || cantA > juegoA.getCantLic())
        {
            System.out.println("No hay suficientes licencias para la compra");
            return 0;
        }

        double total = juegoD.venderLic(cantD) + juegoA.venderLic(cantA);

        if(cantD >= 20 && cantA >= 15)
        {
            total = total - (total * 15 / 100);
            System.out.println("Descuento combo del 15% aplicado");
        }

        return total;
    }





}
