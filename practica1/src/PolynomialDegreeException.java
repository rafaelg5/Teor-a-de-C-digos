/**
 * Clase para excepciones de grados de polinomio inválidos.
 */
public class PolynomialDegreeException extends Exception{

    /**
     * Constructor vacío.
     */
    public PolynomialDegreeException() {
        super();
    }

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param msg un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public PolynomialDegreeException(String msg) {
        super(msg);
    }
}
