package modelo.mutacion;

import java.util.ArrayList;
import java.util.Random;

import modelo.arbol.Arbol;
import modelo.arbol.Arbol.Nodo;
import modelo.cromosoma.Cromosoma;

public class MutacionFuncionalSimple implements Mutacion{

	public void mutacion(int indv, Cromosoma[] pob, double prob_mut) {
		for(int i = 0; i < indv; i++){
			//Número aleatorio para la prob de mutación
			double numAle = Math.random();
			if(numAle < prob_mut){
				//Número aleatorio para escoger uno de los nodos terminales disponibles
				Random r = new Random();
				int nodo  = 1;
				//Solo mutamos si el número de funciones mutables es mayor que 
				if(pob[i].getArbol().numFuncsMutables() > 0){
					nodo = r.nextInt(pob[i].getArbol().numFuncsMutables());
					//Mutación del funcional
					toArray(pob[i].getArbol(), nodo);
				}
			}
		}
	}
	
	//Pasar los nodos terminal del árbol al ArrayList
	private void toArray(Arbol a, int n){
		ArrayList<Nodo> terms = new ArrayList<Nodo>();
		toArrayAux(a.getRaiz(), terms);
		//Una vez los tenemos en el ArrayList modificamos el indicado por n
		Nodo aux = terms.get(n);
		if(aux.getValor().equals("OR"))
			aux.setValor("AND");
		else
			aux.setValor("OR");
	}
	
	private void toArrayAux(Nodo n, ArrayList<Nodo> terms){
		if(n == null){
			return;
		}
		if(n.gethijos() != null){
			//Solo guardamos los nodos que sean función y que no sean "if" ni "not",
			//ya que estos no pueden mutar por otro que no sea ellos mismos o de lo
			//contrario se rompería el árbol
			if(!n.getValor().equals("IF") && !n.getValor().equals("NOT"))
				terms.add(n);
			for(int i = 0; i < n.gethijos().length; i++){
				toArrayAux(n.gethijos()[i].getRaiz(), terms);
			}
		}
	}
}
