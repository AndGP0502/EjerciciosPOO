public class MainDocente {

    public static void main(String[] args) {


        // Clase de los Docentes con validaciones, getter y setter

        Docente do1 = new Docente("Ana Pérez", 1712345678L, "0991234567", "ana@udla.edu.ec");

        System.out.println("Nombre: " + do1.getNombre());
        System.out.println("Documento: " + do1.getDocumento());
        System.out.println("Telefono: " + do1.getTelefono());
        System.out.println("Email: " + do1.getEmail());


        // Cambiar datos con los setters (valores válidos)
        do1.setTelefono("0987654321");
        do1.setEmail("ana.perez@udla.edu.ec");

        System.out.println("\nDatos actualizados:");
        System.out.println("Teléfono: " + do1.getTelefono());
        System.out.println("Email: " + do1.getEmail());

        // Probar las validaciones (valores inválidos)
        System.out.println("\nPruebas con datos inválidos:");
        do1.setTelefono("123");        // muestra mensaje de error
        do1.setDocumento(-5);          // muestra mensaje de error
        do1.setEmail("correo-sin-arroba");

        // Los valores anteriores se mantienen porque no se asignaron los inválidos
        System.out.println("Teléfono: " + do1.getTelefono());
        System.out.println("Documento: " + do1.getDocumento());


        // Operaciones matemáticas y trigonométricas

        OpeMate num1 = new OpeMate();

        System.out.println("\nOperaciones matemáticas:");
        System.out.println("Logaritmo base 10 de 5: " + num1.logaritmoBase(5));
        System.out.println("Logaritmo natural de e: " + num1.logaritmoE(Math.E));
        System.out.println("Raíz cuadrada de 100: " + num1.raizCuadrada(100));

        Trigono num2 = new Trigono();

        System.out.println("\nTrigonometría (radianes):");
        System.out.println("Coseno de π: " + num2.coseno(Math.PI));
        System.out.println("Seno de π/2: " + num2.seno(Math.PI / 2));
        System.out.println("Tangente de 2π: " + num2.tan(2 * Math.PI));

    }
}
