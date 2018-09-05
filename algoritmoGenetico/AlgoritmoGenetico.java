package algoritmoGenetico;
import java.util.HashMap;
import java.util.Map;


import modelo.cromosoma.Cromosoma;
import modelo.cruce.Cruce;
import modelo.mutacion.Mutacion;
import modelo.reproduccion.Reproduccion;
import modelo.seleccion.Seleccion;

public class AlgoritmoGenetico {
	Map<Integer, Integer> pobAux = new HashMap<Integer, Integer>();
	protected Cromosoma[] poblacion;
	protected Cromosoma mejor;
	
	// Parametros
	private int profundidad;
	protected int individuos;
	private int generaciones;
	private double prob_cruce;
	private double prob_mutacion;
	private Seleccion metodoSeleccion;
	private Mutacion metodoMutacion;
	private Cruce metodoCruce;
	private Reproduccion metodoReproduccion;
	private boolean elitismo;
	private boolean condicion;
	private int tamElite;
	private String funcion;
	private int entradas;
	private int mediaPrf;
	
	/************ Modificado para revisión ***************/
	private Cromosoma[] elite;
	/*****************************************************/
	
	// Estadisticas
	private double[] mejorGeneracion;
	private double[] mejorGlobal;
	private double[] mediaAptitud;
	
	//Constructor
	public AlgoritmoGenetico (int p, int entr, int t, int g, double pc, double pm, Seleccion s, boolean e, boolean cond, Mutacion m, Cruce c, Reproduccion r, String ini) {
		profundidad = p-1;
		individuos = t;
		generaciones = g;
		prob_cruce = pc;
		prob_mutacion = pm;
		metodoSeleccion = s;
		metodoMutacion = m;
		metodoCruce = c;
		metodoReproduccion = r;
		funcion = ini;
		entradas = entr;
		
		poblacion = new Cromosoma[individuos];
		
		elitismo = e;
		condicion = cond;
		Double d = (individuos * 0.2) + 1;
		tamElite = d.intValue(); 
		
		/************ Modificado para revisión ***************/
		if(elitismo){
			elite = new Cromosoma[tamElite];
		}
		
		/*****************************************************/
		
		mejorGeneracion = new double[generaciones];
		mejorGlobal = new double[generaciones];
		mediaAptitud = new double[generaciones];
	}
	
	//Ejecucuión del algoritmo
	public Cromosoma ejecuta() {
		inicializaPoblacion();
		evaluarPoblacion(0);	// Evaluamos la población puntuandoa sus individuos
		
		int t=1;
		
		while (!terminacion(t)) {
			for (int i = 0; i < individuos; i++) {
				System.out.println(poblacion[i].getAptitud());
			}
			if(elitismo){
				seleccionarElite();
			}
			System.out.println("sig");
			for (int i = 0; i < individuos; i++) {
				System.out.println(poblacion[i].getAptitud());
			}
			
			seleccion();	   // Modifica la poblacion (la sobreescribe)
			reproduccion(t-1);	   // Modifica a los progenitores
			
			mutacion();	   // Modifica la poblacion a nivel de genes
			
			actMediaPrf();
			System.out.println("sig");
			for (int i = 0; i < individuos; i++) {
				System.out.println(poblacion[i].getAptitud());
			}
			

			if(elitismo){
				reintegrar(); //Si ha habido elitismo, reintegramos la élite en la población
			}
			
			evaluarPoblacion(t); //Reevaluamos
			t++;
		}
		//Devolvemos el mejor cromosoma
		return mejor;
	}
	
	//Proceso de mutación de la población
	private void mutacion() {
		metodoMutacion.mutacion(individuos, poblacion, prob_mutacion);
	}
	
	//Proceso de reproducción de la población
	private void reproduccion(int t) {
		metodoReproduccion.reproduccion(individuos, prob_cruce, metodoCruce, poblacion, mediaPrf, mejorGlobal[t]);
	}
	
	//Seleccionamos los indivíduos de esta generación que
	//participarán en el proceso de cruce y de mutación
	private void seleccion() {
		metodoSeleccion.selecciona(poblacion, prob_cruce);
	}
	
	/****************************** Modificado para revisión **********************/
	//Selección de los indivíduos de la población que formarán parte de la élite
	public void seleccionarElite() {
		/*for (int i = 0; i < individuos; i++) {
			System.out.print(poblacion[i].getAptitud()+ ", ");
		}
		System.out.println();*/
		ordenaMayorAMenor();
/*		for (int i = 0; i < individuos; i++) {
			System.out.print(poblacion[i].getAptitud()+ ", ");
		}
		System.out.println();*/
		for(int i = 0; i < tamElite; i++){
			this.elite[i] = poblacion[i].clone();
        }
	}
	
	//Ordenar la población de mayor a menor aptitud
	private void ordenaMayorAMenor(){
		
		Cromosoma aux;
		for(int i = 0; i < individuos; i++){
			for(int j = 0; j < i; j++){
				if(poblacion[i].getAptitud() > poblacion[j].getAptitud()){
					aux = poblacion[i].clone();
					poblacion[i] = poblacion[j].clone();
					poblacion[j] = aux.clone();
				}
			}
		}
	}
	
	//Reintegrar la élite en la población
	private void reintegrar(){
		int cont = 0;
		//Sustituímos los peores individuos de la población por la élite 
		for(int i = individuos-1; i > individuos - tamElite; i--){
			poblacion[i] = elite[cont].clone();
			cont++;
		}
	}
	
	/*******************************************************************************/
	
	//Comprobar si el algoritmo ha terminado
	private boolean terminacion(int t) {
		return (t >= generaciones);
	}
	
	//Evaluar población
	private void evaluarPoblacion(int t) {
		double puntAcumulada = 0;
		double sumaAptitudes = 0;
		
		int mejorAptitud = 0;
		int posicionMejor = 0;
		
		for (int i = 0; i < individuos; i++) {
			int a = poblacion[i].getAptitud();
			sumaAptitudes += a;
			
			if (a > mejorAptitud) {	
				posicionMejor = i;
				mejorAptitud = a;
			}
			
		}
		
		for (int i = 0; i < individuos; i++) {
			poblacion[i].puntua(sumaAptitudes, puntAcumulada);
			puntAcumulada += poblacion[i].getPuntuacion();
		}
		
		if (mejor == null) {
			mejor = new Cromosoma(funcion, profundidad, entradas, condicion);
			mejor.setAptitud(0);
			mejor = poblacion[posicionMejor].clone();
		}else{
			if (mejorAptitud > mejor.getAptitud()) {		
				mejor = poblacion[posicionMejor].clone();
			}
		}
		
		mejorGeneracion[t] = poblacion[posicionMejor].getAptitud();
							
		mejorGlobal[t] = mejor.getAptitud();

		mediaAptitud[t] = sumaAptitudes / individuos;
	}

	//Inicializar la población
	private void inicializaPoblacion() {
		for (int i = 0; i < individuos; i++) {
			Cromosoma c = new Cromosoma(funcion, profundidad, entradas, condicion);
			poblacion[i] = c.clone();
			poblacion[i].calcularAptitud();	
			
		}
		actMediaPrf();
	}
	
	private void actMediaPrf(){
		int sum = 0;
		for (int i = 0; i < individuos; i++) {
			sum += poblacion[i].getHMax();
		}
		mediaPrf = (sum / individuos)+1;
		//System.out.println(mediaPrf);
	}
		
	/*************** Getters y Setters *****************/
	public double[] getMejorPorGeneracion() {
		return this.mejorGeneracion;
	}
	
	public double[] getMejorGlobal() {
		return this.mejorGlobal;
	}
	
	public double[] getMediaAptitud() {
		return this.mediaAptitud;
	}
}
