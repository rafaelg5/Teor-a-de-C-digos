import java.util.*;

public class FiniteField{
    
    private int q;
    private int p;
    private int n;
    private Polynomial[] elements;
    private Polynomial primitivePolynomial;

    /**
     * Constructor para un campo finito. Tal que el número de elementos
     * del campo sea q = p^n y se toma como base un polinomio irreducible p(x) 
     * de grado n.
     * con coeficientes en Zp.
     * @param p un número primo
     * @param coeffs los coeficientes de un polinomio
     */
    public FiniteField(int p, int[] coeffs) throws Exception{

	if(coeffs.length == 1)
	    throw new PolynomialDegreeException("Polinomio inválido");
	
	Polynomial polynomial = new Polynomial(coeffs);
	
	n = polynomial.getDegree();
	q = (int)Math.pow(p, n);
	this.p = p;	
	elements = new Polynomial[q];
	elements = setElements();
	if(coeffs.length > 2)
	    findPrimitivePolynomial();

	if(!isIrreducible(polynomial)){
	    throw new Exception("El polinomio no es irreducible");
	}
    }

    /**
     * Verifica si un polinomio es irreducible en un campo
     * @param polynomial el polinomio
     * @return true si es irreducible, false e.o.c
     */
    public boolean isIrreducible(Polynomial polynomial){

	if(polynomial.getDegree() == 0)
	    return false;

	for(int i = 0; i < elements.length; i++){
	    if(elements[i].getDegree() == 0)
		continue;
	    for(int j = 0; j < elements.length; j++){
		if(elements[j].getDegree() == 0)
		    continue;
		Polynomial product = elements[i].multiply(elements[j]);
		moduloP(product);
		if(product.equals(polynomial))
		    return false;
	    }
	}
	return true;
    }

    /**
     * Construye los elementos del campo dado un polinomio primitivo
     * @param polynomial el polinomio.
     * @return los elementos del campo
     */
    private Polynomial[] setElements(Polynomial polynomial){

	Polynomial[] auxElements = new Polynomial[q];
	
	// El primer elemento siempre es el 0
	int[] zero = new int[n];
	auxElements[0] = new Polynomial(zero);

	// Todos los elementos primitivos a^k, tal que 0 < k < n
	for(int i = 1; i < n; i++){
	    int[] aux = new int[n];
	    Polynomial newP = new Polynomial(aux);
	    newP.setCoefficient(1, i);
	    newP.setDegree(i);
	    auxElements[i] = newP;
	}

	if(contains(auxElements, polynomial))
	    return null;

	// polinomio a^k con k = n
	int[] aux = new int[n];	
	
	for(int i = 0; i < n; i++){
	    int c = (-1 * polynomial.getCoefficient(i)) % p;
	    c = c < 0 ? c + p : c;
	    aux[i] = c;
	}
	
	//Polynomial primitiveRoot = new Polynomial(aux);	
	//auxElements[n] = new Polynomial(primitiveRoot.getCoefficients());
	auxElements[n] = new Polynomial(polynomial.getCoefficients());

	// a^i = a * a^(i - 1)
	for(int i = n + 1; i < q; i++){	    
	    int[] alphaC = new int[n];
	    Polynomial alpha = new Polynomial(alphaC);
	    alpha.setCoefficient(1, 1);
	    alpha.setDegree(1);
	    // a * a^(i - 1)
	    Polynomial newP = alpha.multiply(auxElements[i - 1]);
	    moduloP(newP);

	    // si el polinomio es de grado mayor a n, sustiuirlo correctamente
	    if(newP.getCoefficients().length > n &&
	       newP.getCoefficient(n) != 0){

		Polynomial auxP =
		    new Polynomial(auxElements[n].getCoefficients());
		for(int j = 0; j < n; j++){
		    int auxC = newP.getCoefficient(n) * auxP.getCoefficient(j);
		    auxP.setCoefficient(auxC, j);
		}
		
		newP = new Polynomial(Arrays.copyOfRange(newP.getCoefficients(),
							 0, n));
		newP = newP.add(auxP);	        
	    }

	    moduloP(newP);
	    if(contains(auxElements, newP))
		return null;
	    auxElements[i] =
		new Polynomial(Arrays.copyOfRange(newP.getCoefficients(),0, n));
	}
	primitivePolynomial = polynomial;
	return auxElements;
    }

    /**
     * Construye los elementos del campo, dado un polinomio irreducible p(x).
     * Los elementos del campo son todos los polinomios de grado &lt deg(p(x)).
     * @return los elementos del campo
     */
    private Polynomial[] setElements() {

	/* 
	 * Los elementos pueden ser vistos como permutaciones con repetición
	 */
	int[] inputSet = new int[p];
	for(int i = 0; i < p; i++)
	    inputSet[i] = i;

	int size = q;
	ArrayList<String> result = new ArrayList<>();
	
	for (int i = 0; i < q; i++) {
	    String permutation = "";
	    int k = i;
	    for (int j = 0; j < n; j++) {
		if (j > 0) {
		    k /= p;
		}
		permutation += inputSet[k % p];
	    }
	    result.add(permutation);
	}

	Polynomial[] auxElements = new Polynomial[q];
	Collections.sort(result);
	for(int i = 0; i < q; i++){
	    String element = result.get(i);
	    int j = 0;
	    int coeffs[] = new int[n];
	    for(char c : element.toCharArray()){
	        coeffs[j] = Character.getNumericValue(c);		
		j++;
	    }
	    auxElements[i] = new Polynomial(coeffs);
	}
	return auxElements;
    }

    /**
     * Encuentra el polinomio primitivo o generador y ordena los elementos del
     * campo de acuerdo a la notación normalizada.
     */
    private void findPrimitivePolynomial(){

	Polynomial[] aux = null;
	for(Polynomial p : elements){
	    if(p.getCoefficient(0) == 0 && p.getDegree() == 0)
		continue;
	    aux = setElements(p);
	    if(aux != null){
		elements = aux;
		break;
	    }
	}
    }

    public Polynomial getElement(int e) throws NotElementOfFieldException{
	
	if(e < 0 || e >= q)
	    throw new NotElementOfFieldException();
	
	return elements[e];
    }

    /**
     * Determina si un polinomio está en un arreglo de polinomios
     * @param array el arreglo
     * @param p1 el polinomio
     * @return si el polinomio está en el arreglo o no
     */
    private boolean contains(Polynomial[] array, Polynomial p1){

	for(Polynomial pp : array)
	    if(p1.equals(pp))
		return true;
	
	return false;
    }

    /**
     * Determina si un polinomio está en el campo
     * @param p1 el polinomio
     * @return si el polinomio está en el campo o no
     */
    public boolean contains(Polynomial p1){
	 
	for(Polynomial pp : elements)
	    if(p1.equals(pp))
		return true;
	return false;
    }

    /**
     * Aplica la operación módulo p a un polinomio
     * @param polynomial el polinomio
     */
    private void moduloP(Polynomial polynomial){
	
	for(int i = 0; i < polynomial.getDegree() + 1; i++){
	    int coeff = polynomial.getCoefficient(i);
	    int newCoeff = coeff < 0 ? coeff + p : coeff % p;
	    polynomial.setCoefficient(newCoeff, i);
	}
    }

    /**
     * Suma (+) dos elementos del campo
     * @param e1 la posición de un elemento del campo
     * @param e2 la posición de un elemento del campo
     * @return la suma de dos elementos del campo, la cual es un elemento 
     * del campo
     */
    public Polynomial sum(int e1, int e2)
	throws NotElementOfFieldException{
	
	if(e1 < 0 || e1 >= q || e2 < 0 || e2 >= q)
	    throw new NotElementOfFieldException();	

	if(e1 == 0)
	    return elements[e2];

	if(e2 == 0)
	    return elements[e1];

	Polynomial p1 = new Polynomial(elements[e1].getCoefficients());
	Polynomial p2 = new Polynomial(elements[e2].getCoefficients());
	Polynomial sum = p1.add(p2);
	moduloP(sum);
	return sum;
    }

    /**
     * Resta (-) dos elementos del campo
     * @param e1 la posición de un elemento del campo
     * @param e2 la posición de un elemento del campo
     * @return la resta de dos elementos del campo, la cual es un elemento 
     * del campo
     */
    public Polynomial difference(int e1, int e2)
	throws NotElementOfFieldException{

	Polynomial p1 = new Polynomial(elements[e1].getCoefficients());
	Polynomial p2 = new Polynomial(elements[e2].getCoefficients());
	
	for(int i = 0; i < n; i++)
	    p2.setCoefficient(p2.getCoefficient(i) * - 1, i);
	
	moduloP(p2);
	for(int i = 0; i < elements.length; i++)
	    if(p2.equals(elements[i]))
		return sum(e1, i);
	
	// No debería llegar aquí
	return null;
    }

    /**
     * Producto (*) de dos elementos del campo
     * @param e1 la posición de un elemento del campo
     * @param e2 la posición de un elemento del campo
     * @return el producto de dos elementos del campo, el cual es un elemento
     * del campo
     */
    public Polynomial multiply(int e1, int e2)
	throws NotElementOfFieldException{
	
	if(e1 < 0 || e1 >= q || e2 < 0 || e2 >= q)
	    throw new NotElementOfFieldException();	

	if(e1 == 0)
	    return elements[0];	

	if(e2 == 0)
	    return elements[0];

	if(e1 == q - 1)
	    return elements[e2];

	if(e2 == q - 1)
	    return elements[e1];
	
	int res = e1 + e2;
	return res >= q ? elements[(res % q) + 1] : elements[res];
    }

    /**
     * Obtiene el inverso aditivo -p(x) de un polinomio p(x) tal que 
     * p(x) + (-p(x)) = 0
     * @param e la posición de un elemento del campo
     * @return el inverso aditivo
     */
    public Polynomial additiveInverse(int e)
	throws NotElementOfFieldException{
	
	if(e < 0 || e >= q)
	    throw new NotElementOfFieldException();

	for(int i = 0; i < elements.length; i++)
	    if(difference(e, i).equals(elements[0]))
		return elements[i];

	// no debería llegar aquí
	return null;
    }

    /**
     * Obtiene el inverso multiplicativo p(x)^-1 de un polinomio p(x) tal que 
     * p(x) * p(x)^-1 = 1
     * @param e la posición de un elemento del campo
     * @return el inverso multiplicativo
     */
    public Polynomial multiplicativeInverse(int e)
	throws NotElementOfFieldException{
	
	if(e < 0 || e >= q)
	    throw new NotElementOfFieldException();

	for(int i = 0; i < elements.length; i++)
	    if(multiply(e, i).equals(elements[q - 1]))
		return elements[i];

	// El polinomio es cero
	return null;
    }

    @Override
    public String toString(){

	/*
	 * Representación del campo
	 */
	
	String field = "Campo F(" + q + "):\n";
	int i = 0;
	String alpha = "" + '\u03B1';

	for(Polynomial pl : elements){
	    
	    field += alpha + "^" + i + " = " + elements[i] + "\n";
	    i++;
	}
	
	return field;
    }
}
