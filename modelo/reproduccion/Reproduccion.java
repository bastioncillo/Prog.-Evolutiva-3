package modelo.reproduccion;

import modelo.cromosoma.Cromosoma;
import modelo.cruce.Cruce;

public interface Reproduccion {
	public void reproduccion(int individuos, double prob_cruce, Cruce metodoCruce, Cromosoma[] poblacion, int mediaPrf, double m);
}
