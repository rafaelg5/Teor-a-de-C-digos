import java.util.Scanner;
import java.io.*;


public class MainQRC{
    public static void main(String[] args) {

	boolean cont = false;	
	Scanner scanner = new Scanner(System.in);
	String calc = "\n*********************************\n";
	calc += "* Códigos de Residuos Cuadráticos *\n";
	calc += "*********************************\n\n";

	File file;
	do{
	    cont = false;
	    // Leer datos
	    System.out.print("\nIntroducir el nombre del archivo: ");
	    String inputFile = scanner.nextLine();
	    file = new File(inputFile);
	    if(!file.exists()) {
		System.out.println("\nNombre de archivo incorrecto. "
				   + "Intente de nuevo.");
		cont = true;
		continue;
	    }
	}while(cont);

	int p = 0, l = 0, m = 0;
	do{
	    cont = false;
	    System.out.print("\nIntroducir p: ");
	    p = scanner.nextInt();
	    if(!isPrime(p)){
		System.out.println("\nEl número \"p\" no es primo. "
				   + "Intente de nuevo.");
		cont = true;
		continue;
	    }
	    // Salto de línea
	    scanner.nextLine();
	    System.out.print("\nIntroducir l: ");
	    l = scanner.nextInt();
	    if(!isPrime(l)){
		System.out.println("\nEl número \"l\" no es primo. "
				   + "Intente de nuevo.");
		cont = true;
		continue;
	    }
	    // Salto de línea
	    scanner.nextLine();
	    System.out.print("\nIntroducir m: ");
	    m = scanner.nextInt();
	    if(!isPrime(m)){
		System.out.println("\nEl número \"m\" no es primo. "
				   + "Intente de nuevo.");
		cont = true;
		continue;
	    }
	    // Salto de línea
	    scanner.nextLine();

	}while(cont);	
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
