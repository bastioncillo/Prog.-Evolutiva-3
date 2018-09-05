package modelo.mutacion;

import java.util.Random;

import modelo.cromosoma.Cromosoma;

public class MutacionDeArbol implements Mutacion{

	public void mutacion(int ind, Cromosoma[] pob, double prob_mut) {
		for(int i = 0; i < ind; i++){
			Random r = new Random();
			if(prob_mut > r.nextDouble()){
				int ptoBorrado = 1;
				if(pob[i].getNumFunciones() > 1){
					ptoBorrado = 2 + r.nextInt(pob[i].getNumFunciones()-1);	
				}
				pob[i].mutarSubArbol( ptoBorrado);
				pob[i].addReferencia();
				pob[i].actProfundidad();
				pob[i].calcularAptitud();
			}
		}
	}

}
