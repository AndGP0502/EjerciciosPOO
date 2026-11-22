package negocio;

/**
 * Excepción que se lanza cuando una operación del inventario rompe una regla
 * del negocio (código repetido, stock insuficiente, falta de espacio, etc.).
 *
 * Es una excepción verificada (checked): quien llame a los métodos de
 * {@link Inventario} está obligado a manejarla con try/catch.
 */
public class InventarioException extends Exception {

    private static final long serialVersionUID = 1L;

    public InventarioException(String mensaje) {
        super(mensaje);
    }
}
