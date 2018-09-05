package modelo.mutacion;

import modelo.cromosoma.Cromosoma;

public interface Mutacion {
	public void mutacion(int ind, Cromosoma[] pob, double prob_mut);
}
