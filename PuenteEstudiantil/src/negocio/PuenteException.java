package negocio;

/**
 * Excepción que se lanza cuando una operación rompe una regla del negocio
 * (cédula repetida, programa sin vigencia, caso ya abierto, etc.).
 *
 * Es una excepción verificada (checked): la interfaz está obligada a manejarla
 * con try/catch y mostrarle el mensaje al usuario.
 */
public class PuenteException extends Exception {

    private static final long serialVersionUID = 1L;

    public PuenteException(String mensaje) {
        super(mensaje);
    }
}
