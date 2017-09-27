/**
 * Clase para excepciones de elementos inválidos de un campo.
 */
public class NotElementOfFieldException  extends Exception {

    /**
     * Constructor vacío.
     */
    public NotElementOfFieldException() {
        super("No es elemento del campo");
    }

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public NotElementOfFieldException(String msg) {
        super(msg);
    }
}
