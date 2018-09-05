package modelo.mutacion;

import java.util.ArrayList;
import java.util.Random;

import modelo.arbol.Arbol;
import modelo.arbol.Arbol.Nodo;
import modelo.cromosoma.Cromosoma;

public class MutacionTerminalSimple implements Mutacion{

	public void mutacion(int indv, Cromosoma[] pob, double prob_mut) {
		String[] terminales;
		/*= new String[6];
		terminales[0] = "A0"; terminales[1] = "A1"; terminales[2] = "D0";
		terminales[3] = "D1"; terminales[4] = "D2"; terminales[5] = "D3";
		*/
		for(int i = 0; i < indv; i++){
			//Número aleatorio para la prob de mutación
			double numAle = Math.random();
			if(numAle < prob_mut){
				
				terminales = pob[i].getCjtoTerms();
				//Número aleatorio para eligir uno de los 6 terminales
				int numAle2 = (int) (Math.random()*terminales.length);
				//Número aleatorio para escoger uno de los nodos terminales disponibles
				Random r = new Random();
				int nodo = r.nextInt(pob[i].getArbol().numTerminales());
				String s = terminales[numAle2];
				
				/*System.out.println("nodo: " +  nodo);
				System.out.println("s: " +  s);	
				
				pob[i].printAB();
				System.out.println();
				*/
				//Mutación del terminal
				toArray(pob[i].getArbol(), nodo, s);
				/*
				pob[i].printAB();
				System.out.println();
				*/
			}
		}
	}
	
	//Pasar los nodos terminal del árbol al ArrayList
	private void toArray(Arbol a, int n, String s){
		ArrayList<Nodo> terms = new ArrayList<Nodo>();
		toArrayAux(a.getRaiz(), terms);
		//Una vez los tenemos en el ArrayList modificamos el indicado por n
		Nodo aux = terms.get(n);
		aux.setValor(s);
	}
	
	private void toArrayAux(Nodo n, ArrayList<Nodo> terms){
		if(n == null){
			return;
		}
		if(n.gethijos() != null){
			for(int i = 0; i < n.gethijos().length; i++){
				toArrayAux(n.gethijos()[i].getRaiz(), terms);
			}
		}
		//Solo añadimos los nodos que son terminales(sin hijos)
		else{
			terms.add(n);
		}
	}
	
}

