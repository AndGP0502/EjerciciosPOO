package utilitario;

import java.util.Scanner;

public class Valida {

    private static Scanner sc = new Scanner(System.in);

    public static String leerString(String mensaje){

        String valor;

        do{

            System.out.println(mensaje + ": ");
            valor = sc.nextLine();
            System.out.println("El dato no puede ser nulo ni vacío");
        }while(valor ==null||valor.trim().isEmpty());
        return valor;



    }


}
