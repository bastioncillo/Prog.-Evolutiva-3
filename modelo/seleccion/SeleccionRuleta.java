package modelo.seleccion;

import java.util.Random;

import modelo.cromosoma.Cromosoma;

public class SeleccionRuleta implements Seleccion{
	
	public void selecciona(Cromosoma[] poblacion, double prob_cruce) {
		
		/*Cada elemento de la muestra se toma eligiendo el primer
		 * individuo de la poblaci�n cuya puntuaci�n acumulada sea
		 * superior a un valor aleatorio entre 0 y 1 */
		
		/*Esto se realiza por cada individuo de la pobalci�n*/
		
		int individuos = poblacion.length;
		
		Cromosoma[] nuevaPoblacion = new Cromosoma[individuos];
		int[] seleccionados = new int[individuos]; 
				
		Random r = new Random();
				
		int superviviente;
		double trial;
				
		for (int i = 0; i < individuos; i++) {
			/************** Modificado para revisi�n **************/
			superviviente = 0;
			trial = r.nextDouble();
		
			while (superviviente < individuos && trial > poblacion[superviviente].getPunt_acum())
				superviviente++;
			/******************************************************/
						
			seleccionados[i] = superviviente;
		}
				
		// Crear la poblacion auxiliar de supervivientes
		for (int i = 0; i < individuos; i++){
			nuevaPoblacion[i] = poblacion[seleccionados[i]].clone();
		}
				
		for (int i = 0; i < individuos; i++)
			poblacion[i] = nuevaPoblacion[i].clone();	// REEMPLAZO INMEDIATO
	}
}
