import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

	boolean cont = false;
	FiniteField f = null;	
	Scanner scanner = new Scanner(System.in);
	System.out.println("\n¡IMPORTANTE! La forma de introducir polinomios " +
			   "en el programa es la siguiente:\n"+
			   "Ej: 2x^3 + 3x^2 + 2 = 2302\n"+
			   "Ej: 5x^4 + x + 1 = 50011\n");
	String calc = "\n*********************************\n";
	calc += "* Calculadora de campos finitos *\n";
	calc += "*********************************\n\n";
	
	String menu = calc + "1) Construir nuevo campo\n";
	menu += "2) Cargar campo\n";
	menu += "3) Salir\n\n";
	
	String menu2 = calc + "1) Mostrar elementos del campo\n";
	menu2 += "2) Suma de elementos\n";
	menu2 += "3) Multiplicación de elementos\n";
	menu2 += "4) Inverso aditivo\n";
	menu2 += "5) Inverso multiplicativo\n";
	menu2 += "6) Regresar al menú principal\n";
	menu2 += "7) Salir\n";
	do{
	    System.out.print(menu);
	    System.out.print("Seleccionar opción del menú: ");
	    int opt = scanner.nextInt();
	    System.out.println();
	    switch(opt){
	    case 1:
		// Leer datos
		System.out.print("\nIntroducir p: ");
		int p = scanner.nextInt();
		// Salto de línea
		scanner.nextLine();
		if(!isPrime(p)){
		    System.out.printf("\n%d no es un número primo\n", p);
		    cont = true;
		    break;
		}
		System.out.print("\nIntroducir polinomio: ");	
		String coeffs =
		    new StringBuilder(scanner.nextLine()).reverse().toString();
		
		// polinomio del estilo a0 + a1*x + a2x^2 + ... + an*x^n
		int[] polynomial = new int[coeffs.length()];
		for(int i = 0; i < coeffs.length(); i++)
		    polynomial[i] = Character.getNumericValue(coeffs.charAt(i));
		try{
		    f = new FiniteField(p, polynomial);
		}catch(Exception e){
		    System.out.println(e);
		    cont = true;
		    break;
		}
		boolean cont2 = false;
		// menu 2
	        do{
		    System.out.println(menu2);
		    opt = scanner.nextInt();
		    System.out.println();
		    scanner.nextLine();
		    switch(opt){
		    case 1:
			System.out.printf("%s",f);
			cont2 = true;
			break;
		    case 2:
		
			System.out.print("\nIntroducir el grado de alpha " +
					 "correspondiente al polinomio 1: ");	
			int p1 = scanner.nextInt();
			scanner.nextLine();
			System.out.print("\nIntroducir el grado de alpha " +
					 "correspondiente al polinomio 2: ");
			int p2 = scanner.nextInt();
			scanner.nextLine();
			try{
			    System.out.printf("\n (%s) + (%s) = %s\n",
					      f.getElement(p1),
					      f.getElement(p2), f.sum(p1, p2));
			}catch(NotElementOfFieldException e){
			    System.out.println(e);
			}
			cont2 = true;
			break;
		    case 3:
		    case 4:
		    case 5:
		    case 6:
			cont2 = false;
			cont = true;
			break;
		    case 7:
		    default:
			System.out.println("Opción inválida");
			cont2 = true;
		    }
		}while(cont2);
		break;
	    case 2:
		cont = true;
		break;
	    case 3:
		System.exit(1);
	    default:
		System.out.println("Opción inválida\n");
		cont = true;
	    }
	}while(cont);

	if(f == null){
	    System.out.println("Algo salió mal... Intenta de nuevo :)");
	    System.exit(1);
	}	
    }

    // Determina si un número es o no primo
    private static boolean isPrime(int p){

	if(p == 1)
	    return false;

	if (p != 2 && p % 2 == 0)
	    return false;

	for(int i = 3; i * i <= p; i += 2) {
	    if(p % i == 0)
		return false;
	}
	return true;
    }

}
