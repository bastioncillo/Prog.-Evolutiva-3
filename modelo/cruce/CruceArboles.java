package modelo.cruce;

import java.util.Random;

import modelo.arbol.Arbol;
import modelo.cromosoma.Cromosoma;

public class CruceArboles implements Cruce{

	public void cruce(Cromosoma padre1, Cromosoma padre2, Cromosoma hijo1, Cromosoma hijo2, int mediaPrf, double mejorAptGen){
		Random r = new Random();

		//Añadimos las referencias
		
		hijo1.addReferencia();
		hijo2.addReferencia();
		
		//Calculamos los puntos de corte
		
		int ptoCruce1 = 1;
		int ptoCruce2 = 1;
		
		if(hijo1.getNumFunciones() > 1){
			ptoCruce1 = 2 + r.nextInt(hijo1.getNumFunciones()-1);	
		}
		
		if(hijo2.getNumFunciones() > 1){
			ptoCruce2 = 2 + r.nextInt(hijo2.getNumFunciones()-1);	
		}
		
		/*
		System.out.println("pt1: " +  ptoCruce1);
		System.out.println("pt2: " +  ptoCruce2);
		
		
		hijo1.printAB();
		System.out.println();
		hijo2.printAB();
		System.out.println();
		*/
		
		//Sacamos los subArboles de los puntos de corte
		Arbol subAb1 = hijo1.calSubArbol(ptoCruce1);
		Arbol subAb2 = hijo2.calSubArbol(ptoCruce2);
		/*subAb1.preOrder();
		System.out.println();
		
		subAb2.preOrder();
		System.out.println();*/
		
		hijo1.addSubArbol(subAb2, ptoCruce1);
		hijo2.addSubArbol(subAb1, ptoCruce2);
		
		hijo1.addReferencia();
		hijo2.addReferencia();
		
		hijo1.actProfundidad();
		hijo2.actProfundidad();
		
		hijo1.calcularAptitud();
		hijo2.calcularAptitud();
		
		/*
		hijo1.printAB();
		System.out.println();
		hijo2.printAB();
		System.out.println();
		*/
		//Bloating
		/*
		System.out.println("Prof: "+mediaPrf);
		System.out.println("Aptitud: "+mejorAptGen);
		
		System.out.println("profun1: "+hijo1.getHMax());
		System.out.println("aptitud1: "+hijo1.getAptitud());
		
		System.out.println("profun2: "+hijo2.getHMax());
		
		System.out.println("aptitud2: "+hijo2.getAptitud());*/
		
		
		int ram =  r.nextInt();
		
		if(hijo1.getHMax() > mediaPrf && ram % 2 == 0){
			
			//if(hijo1.getAptitud() < mejorAptGen){
				hijo1.setAptitud(0);
			//}
		}
		if(hijo2.getHMax() > mediaPrf && ram % 2 == 0){
			//if(hijo2.getAptitud() < mejorAptGen){
				hijo2.setAptitud(0);
			//}
		}
	}	
}