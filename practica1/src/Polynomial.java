import java.util.Arrays;
import java.io.Serializable;

public class Polynomial implements Serializable{
    
    private int degree;
    private int[] coeff;
    private static final long serialVersionUID = 2L;

    /**
     * Construye un polinomio
     * @param n el grado del polinomio
     * @param coeff los coeficientes a0, a1, ..., an del polinomio
     */
    public Polynomial(int n, int[] coeff){
	degree = n;
	this.coeff = coeff;
    }

    /**
     * Construye un polinomio
     * @param coeff los coeficioentes a0, a1, ..., an del polinomio
     */
    public Polynomial(int[] coeff){
	this.coeff = coeff;
	rebalance();
    }

    /**
     * Cambia el grado del polinomio después de una modificación a sus 
     * coeficientes
     */
    private void rebalance(){
	for(int i = coeff.length - 1; i > 0; i--)
	    if(coeff[i] != 0){
		degree = i;
		return;
	    }
	degree = 0;
    }

    /**
     * Obtiene grado del polinomio
     * @return el grado del polinomio
     */
    public int getDegree(){
	return degree;
    }

    /**
     * Cambia el grado del polinomio
     * @param degree el nuevo grado del polinomio
     */
    public void setDegree(int degree){
	this.degree = degree;
    }

    /**
     * Obtiene los coeficientes del polinomio
     * @return los coeficientes del polinomio
     */
    public int[] getCoefficients(){
	int[] newArray = Arrays.copyOf(coeff, coeff.length);
	return newArray;
    }

    /**
     * Cambia los coeficientes del polinomio
     * @param coeff los nuevos coeficientes del polinomio
     */    
    public void setCoefficients(int[] coeff){
	this.coeff = coeff;
	rebalance();
    }

    /**
     * Obtiene un coeficiente del polinomio
     * @param xi la posición del coeficiente ai de xi
     * @return el coeficiente del polinomio
     */
    public int getCoefficient(int xi){
	return coeff[xi];
    }

    /**
     * Cambia un coeficiente del polinomio
     * @param coeff el nuevo coeficientes del polinomio
     * @param xi la posición del coeficiente ai (coeff) de xi
     */ 
    public void setCoefficient(int coeff, int xi){
	this.coeff[xi] = coeff;
	rebalance();
    }

    /**
     * Multiplicación de polinomios
     * @param polynomial el polinomio a multiplicar
     * @return la multiplicación de los polinomios
     */
    public Polynomial multiply(Polynomial polynomial) {
	
	int totalLength = this.degree + polynomial.degree + 1;
        int[] result = new int[totalLength];
	
        for (int i = 0; i < degree + 1; i++)
            for (int j = 0; j < polynomial.degree + 1; j++) 
                result[i + j] += coeff[i] * polynomial.coeff[j];           
	
        return new Polynomial(result);
    }
    
    /**
     * Suma de polinomios
     * @param polynomial el polinomio a sumar
     * @return la suma de los polinomios
     */
    public Polynomial add(Polynomial polynomial){

        int[] newCoeff = coeff.length > polynomial.coeff.length ?
	    getCoefficients() : polynomial.getCoefficients();
	int[] result = newCoeff;
	int minLength = Math.min(coeff.length, polynomial.coeff.length);

	for(int i = 0; i < minLength; i++)
	    result[i] = coeff[i] + polynomial.coeff[i];
		
	return new Polynomial(result);
    }
    
    @Override
    public String toString(){

	/*
	 * Representación del polinomio
	 */
	
	String polynomial = "";
	String alpha = "" + '\u03B1';
	for(int i = coeff.length - 1; i >= 0; i--){
	    
	    if(coeff[0] == 0 && degree == 0){
		polynomial = "0";
		break;
	    }
	    
	    if(coeff[i] == 1){
		if(i == 0)
		    polynomial += 1; 
		else if(i == 1)
		    polynomial += alpha + " + ";
		else
		    polynomial += alpha + "^" + i + " + ";
	    }else{
		if(i == 0 && coeff[0] != 0)
		    polynomial += coeff[i];	    
		else if(i == 1)
		    polynomial += coeff[i] + alpha + " + ";
		else
		    polynomial += coeff[i] + alpha + "^" + i + " + ";
	    }
	}
	
	char[] a = polynomial.toCharArray();
	polynomial = "";
	for(int i = 0; i < a.length; i++){	    
	    
	    if(a[i] == '0' && i + 1 < a.length){
		if(a[i + 1] == '\u03B1' && a[i +2] == ' ')
		    i += 4;
		else
		    i += 6;
		continue;
	    }
	    polynomial += a[i];
	}

	int length = polynomial.length();
	int end = length - 2;
	if(end >= 0)
	    if(polynomial.charAt(end) == '+')
		polynomial = polynomial.substring(0, end);
	
	return polynomial.trim();
    }

    @Override
    public boolean equals(Object o){
	if(o == null)
	    return false;
	if(o == this)
	    return true;
	if (!(o instanceof Polynomial))
	    return false;
	Polynomial p  = (Polynomial)o;
	if(this.coeff.length != p.coeff.length)
	    return false;
	if(this.degree != p.degree)
	    return false;
	for(int i = 0; i < this.coeff.length; i++)
	    if(this.coeff[i] != p.coeff[i])
		return false;

	return true;
    }
}
