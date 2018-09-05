package modelo.cromosoma;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import modelo.arbol.Arbol;
import modelo.arbol.Arbol.Nodo;

public class Cromosoma {
	private int aptitud; //función de evaluación fitness adaptación
	private double puntuacion; //puntuacion relativa(aptitud/suma)
	private double punt_acum; //puntuacion acumulada para selección
	private Arbol arbol; 
	private int hMax=2; 
	private boolean ajustado;
	private Map<String, Integer> termsMap = new HashMap<String, Integer>();
	private String [] m2;
	private int [][] matriz; 
	private int filas;
	private int columnas;
	private String func;
	private int entradas = 2;
	private boolean condicion;	

	private String[] cjtoFuns; 	//= {"AND", "OR", "NOT", "IF"};
	private String[] cjtoTerms; //= {A0,A1,A2,D0,D1,D2,D3,D4,D5,D6,D7,D8}; 
	private String[] cjtoCompleto;

	
	//Contructor
	public Cromosoma(String funcion, int profundidad, int entr, boolean cond){
		arbol = new Arbol();
		//Minimo 2, que seria el NOT por ejemplo
		if(hMax <= profundidad){
			hMax = profundidad;
		}
		condicion = cond;
		entradas = entr;
		func = funcion;
		crearConjuntos();
		
		switch(funcion){
			case "Ramped && Half":
				arbol = rampedAndHalf(0);
				break;
			case "Completa":
				arbol = inicializacionCompleta(0);
				break;
			case "Creciente":
				arbol = inicializacionCreciente(0);
				break;
		}
		arbol.buscarTerminales();
		
		crearMatriz();
		
		hMax = arbol.profundidad();
		
		//cargarMatrice();
			
	}
	
	public Cromosoma(){}
	
	//Inicializar
	
	public Cromosoma(Arbol ab, int aptitud, double puntuacion, double punt_acum, int [][] matriz, int hMax, int col, int fil, String [] cjtT, String [] cjtF, String [] cjtC, String fun){
		
		this.arbol = ab;
		this.aptitud = aptitud;
		this.puntuacion = puntuacion;
		this.punt_acum =  punt_acum;
		this.matriz = matriz;
		this.hMax = hMax;
		this.columnas = col;
		this.filas = fil;
		this.cjtoTerms = cjtT;
		this.cjtoFuns = cjtF;
		this.cjtoCompleto = cjtC;
		this.func = fun;
		
	}
	
	public Cromosoma clone() {
		
		Arbol ab = arbol.clonar();
		
		return new Cromosoma(ab, aptitud, puntuacion, punt_acum, matriz, hMax , columnas, filas, cjtoTerms, cjtoFuns, cjtoCompleto, func);
	}
	
	public void modArbol(String p){
		arbol.setRaiz(p);
	}
	
	//------------------INICIALIZACIÓN----------------------------------
	
	private Arbol inicializacionCompleta(int profundidad) {
		int tam = 0;
		Random r = new Random();
		if(profundidad > hMax-1){
			int s = r.nextInt(cjtoTerms.length);
			String raiz = cjtoTerms[s] ;
			//System.out.println(raiz);
			return new Arbol(raiz);
		}else { // leer recursivamente los hijos
			
			int s = r.nextInt(cjtoFuns.length);
			String raiz = cjtoFuns[s] ;
			//System.out.println("completa: " + raiz);
			if(raiz.equals("IF")){
				tam = 3;
			}else if(raiz.equals("NOT")){
				tam = 1;
			}else{
				tam = 2;
			}
			Arbol [] hijos = new Arbol [tam];
			
			for(int i = 0; i < tam; i++){
				Arbol hi = inicializacionCompleta(profundidad+1);
				hijos[i] = hi;
			}
			
			return new Arbol (hijos, raiz);
		}
	}

	private Arbol inicializacionCreciente(int profundidad) {
		int tam = 0;
		Random r = new Random();
		if(profundidad > hMax-1){
			int s = r.nextInt(cjtoTerms.length);
			String raiz = cjtoTerms[s] ;
			//System.out.println(raiz);
			return new Arbol(raiz);
		}else { // leer recursivamente los hijos
			int s = 0;
			String raiz = "";
			if(profundidad == 0){
				s = r.nextInt(cjtoFuns.length);
				raiz = cjtoFuns[s] ;
			}else{
				s = r.nextInt(cjtoCompleto.length);
				raiz = cjtoCompleto[s] ;
			}
			
			//System.out.println("creciente: "+raiz );
			
			if(s > 3){
				return new Arbol ( null, raiz);
			}
			
			if(raiz.equals("IF")){
				tam = 3;
			}else if(raiz.equals("NOT")){
				tam = 1;
			}else{
				tam = 2;
			}
			Arbol [] hijos = new Arbol [tam];
			
			for(int i = 0; i < tam; i++){
				Arbol hi = inicializacionCreciente(profundidad+1);
				hijos[i] = hi;
			}
			
			return new Arbol (hijos, raiz);
		}
	}

	private Arbol rampedAndHalf(int profundidad){
		Random r = new Random();
		int tam = 0;
		if(profundidad > hMax-1){
			int s = r.nextInt(cjtoTerms.length);
			String raiz = cjtoTerms[s] ;
			//System.out.println("salida: "+raiz);
			return new Arbol(raiz);
			
		}else if(profundidad < (hMax)/2 ){ // leer recursivamente los hijos
			int s = r.nextInt(cjtoFuns.length);
			String raiz = cjtoFuns[s] ;
			//System.out.println("completa: " + raiz);
			if(raiz.equals("IF")){
				tam = 3;
			}else if(raiz.equals("NOT")){
				tam = 1;
			}else{
				tam = 2;
			}
			Arbol [] hijos = new Arbol [tam];
			
			for(int i = 0; i < tam; i++){
				Arbol hi = rampedAndHalf(profundidad+1);
				hijos[i] = hi;
			}
			
			return new Arbol (hijos, raiz);
		}else{
			int s = r.nextInt(cjtoCompleto.length);
			String raiz = cjtoCompleto[s] ;
			//System.out.println("creciente: "+raiz );
			
			if(s > 3){
				return new Arbol ( null, raiz);
			}
			
			if(raiz.equals("IF")){
				tam = 3;
			}else if(raiz.equals("NOT")){
				tam = 1;
			}else{
				tam = 2;
			}
			Arbol [] hijos = new Arbol [tam];
			
			for(int i = 0; i < tam; i++){
				Arbol hi = rampedAndHalf(profundidad+1);
				hijos[i] = hi;
			}
			
			return new Arbol (hijos, raiz);
		}
	}

	public void calcularAptitud() {	
		
		
		/*
		 * 
		 * Obtener número de terminales y tabla de verdad
		 * 	1-Recorremos el arbol en forma preorden
		 * 	2-Realizamos split para dividirlo en un array de Strings
		 * 	3-Con un hasMap eliminamos las repeticiones
		 *  4-Generamos tabla de la verdad de los elementos terminales
		 * 
		 */
		
		aptitud = 0;
		
		String a = arbol.buscarTerminales();
		String []ab = a.split(" ");
		
		for (int i = 0; i < ab.length; i++) {

			if(!termsMap.containsKey(ab[i])){
				termsMap.put(ab[i], 0);
			}
		}
		
		generarTabla(termsMap.size());
		
		/*
		 * Calcular aptitud
		 * 
		 * 	1-Integramos en el hasMap los valores correspondientes a cada 
		 * 	  fila de la tabla de verdad
		 * 	2-Calculamos la funcion con esos valores
		 *  3-Hacemos matching con el resultado de la funcion
		 * 
		 */
		
		for (int i = 0; i < m2.length; i++) {
			
			Iterator <String>  it = termsMap.keySet().iterator();
			
			for(int j = 0; j < m2[i].length();j++){
				String key = it.next();
				String ae =  ""+m2[i].charAt(j);
				termsMap.put(key,  Integer.parseInt(ae));
			}

			int salida  = arbol.calcularFuncion(termsMap);

			aptitud = aptitud + matching(salida);
			
			//System.out.println("Salida: " +  salida);
			
		}
	}
	
	private int matching(int entrada){
		
		int n=0;
		/*
		 * Matching
		 * 	1-Recorremos las filas de la tabla 
		 * 	2-Recorremos los identificadores terminales 
		 * 	  que tenemos de cada fila (A0, D3, ..)
		 * 	3-Buscamos su posicion en la tabla
		 * 	4-Comparamos si son iguales los valores
		 *    usados en la funcion con los de la tabla
		 *  5-Si fuera a si contariamos el acierto en la aptitud
		 * 
		 */
		for (int f = 0; f < filas; f++){
			
			boolean is = false;
			Iterator <String> it = termsMap.keySet().iterator();
			while(it.hasNext() && !is ){
			  String key = it.next();
			  int pos = posiciones(key);
			  
			  if(!(matriz[f][pos] == termsMap.get(key))){
				  is = true;
			  }
			}
			
			if(!is && entrada == matriz[f][columnas]){
				n++;
			}
			
		}
		return n;
		
	}
	
	private int posiciones(String p){
		int pos = 0;
		
		for(int i = 0; i < cjtoTerms.length; i++){
			if(cjtoTerms[i].equals(p)){
				pos = i;
			}
		}
		
		return pos;
	}
	
	private void generarTabla(int tam){
		
		String numBin = ""; 
		
		int tamaño = (int)Math.pow(2, tam);
		
		m2 = new String [tamaño];
        
		for(int i = 0;i<tamaño;i++){ 
            numBin = Integer.toBinaryString(i); 
           
            while(numBin.length()<tam){ 
                numBin = 0+numBin; 
            }
            m2 [i] = numBin;
            //System.out.println(numBin); 
        } 
	}
	
	/****GENERACION DE MATRICES Y CONJUTOS DE FUNCIONES && TERMINALES****/
	
	private void crearMatriz(){
		
		int tamEntradas = (int)Math.pow(2, entradas); // Tamaño de entradas
		int tamEnSal = tamEntradas + entradas; // Tamaño de entradas y salidas
		int tamaño = (int)Math.pow(2, tamEnSal);
		filas = tamaño;
		columnas = tamEnSal;
		
		String [] genMat = new String [tamaño];
        
		String numBin = ""; 
		
		for(int i = 0;i<tamaño;i++){ 
            numBin = Integer.toBinaryString(i); 
           
            while(numBin.length()<tamEnSal){ 
                numBin = 0+numBin; 
            }
            genMat [i] = numBin;
        } 
		
		matriz = new int [tamaño][tamEnSal+1];
		
		for(int i = 0; i < genMat.length; i++){
			for(int j = 0; j < genMat[i].length();j++){
				
				String ae =  ""+genMat[i].charAt(j);
				matriz[i][j] = Integer.parseInt(ae);
			}
		}
		
		int salidas = entradas;
		int fil = 0;
		for(int i = 0; i < (int)Math.pow(2, entradas); i++){
			
			for(int j = 0; j < (int)Math.pow(2, tamEntradas);j++){
	
				matriz [fil][tamEnSal] = matriz[fil][salidas];
				fil++;				
			}
			salidas++;			
		}		
	}
	
	private void crearConjuntos(){
		int tamEntradas = (int)Math.pow(2, entradas); // Tamaño de entradas
		int tamEnSal = tamEntradas + entradas; // Tamaño de entradas y salidas
		cjtoTerms = new String[tamEnSal];
		
		for(int i = 0; i < entradas; i++){
			cjtoTerms[i] = "A"+i;
		}
		int c = 0;
		for(int i = entradas; i < tamEnSal; i++){
			cjtoTerms[i] = "D"+c;
			c++;
		}
		
		
		int tamCjt = 3;
		
		if(condicion){
			tamCjt = 4;
		}
		
		cjtoFuns = new String [tamCjt];
		cjtoCompleto =  new String [tamCjt+tamEnSal] ; // = {"AND", "OR", "NOT", "IF", "A0", "A1", "D0", "D1", "D2", "D3"};
		
		cjtoCompleto[0] = "AND";
		cjtoCompleto[1] = "OR";
		cjtoCompleto[2] = "NOT";
		cjtoFuns[0] = "AND";
		cjtoFuns[1] = "OR";
		cjtoFuns[2] = "NOT";
		
		if(condicion){
			cjtoCompleto[3] = "IF";
			cjtoFuns[3] = "IF";
		}
		
		c =0 ;
		for(int i = tamCjt; i < tamEnSal+tamCjt; i++){
			cjtoCompleto[i] = cjtoTerms[c];
			c++;
		}
		//System.out.println();
		
	}
	
	public void mutarSubArbol(int pto){
		
		arbol.setRaiz(mutarSubArbolAux(arbol.getRaiz(),pto, 0));
		
	}
	
	private Nodo mutarSubArbolAux(Nodo ra, int pto, int cont){
		Arbol ab = null;
		if(ra != null){
			
			if(ra.gethijos() != null){
				
				if(ra.getReff() == pto){
					switch(func){
						case "Ramped && Half":
							ab = rampedAndHalf(cont);
							ra = ab.getRaiz();
							break;
						case "Completa":
							ab = inicializacionCompleta(cont);
							ra = ab.getRaiz();
							break;
						case "Creciente":
							ab = inicializacionCreciente(cont);
							ra = ab.getRaiz();
							break;
					}
				
				}else{
					Arbol [] hi = ra.gethijos();
					cont++;
					for(int i=0; i < hi.length; i++){
		        		
						hi[i].setRaiz(mutarSubArbolAux(hi[i].getRaiz(), pto, cont));
		            }
					ra.sethijos(hi);
				}
			}
		}
		return ra;
	}
	
	
	public String toString(){
		return null;
	}
		
	public void puntua(double suma, double acumulada) {
		puntuacion = aptitud/suma;
		punt_acum = acumulada + puntuacion;
	}	
	
	/***********Bridge***********/
	
	public Arbol calSubArbol(int pto){
		
		arbol.subArbol(arbol.getRaiz(), pto); 
		
		return arbol.getClonAux();
				
	}
	
	public Arbol addSubArbol(Arbol add, int pto){
		arbol.addSubArbol(add, pto);
		return  add;
		
	}
	
	public Arbol clonarArbol(){
		Arbol ab = arbol.clonar();
		return ab;
	}
	
	public void addReferencia(){
		arbol.addReff();
	}
	
	public int getNumFunciones(){
		return arbol.numFunciones();
	}
	
	public void actProfundidad(){
		hMax = arbol.profundidad();
	}
	
	public void printAB(){
		arbol.printAB();
	}
	
	/***************Getters y Setters************/
	
	public Arbol getArbol(){
		return arbol;
	}
	
	public void setAptitud(int a) {
		this.aptitud = a;
	}

	public double getPunt_acum() {
		return this.punt_acum;
	}

	public int getAptitud() {
		return this.aptitud;
	}

	public void setPuntuacion(double p) {
		this.puntuacion = p;		
	}

	public double getPuntuacion() {
		return this.puntuacion;
	}

	public void setPunt_acum(double pa) {
		this.punt_acum = pa;
	}
	
	public int getNumCjtoTerms(){
		return this.cjtoTerms.length;
	}
	
	public int getNumCjtoFuns(){
		return this.cjtoFuns.length;
	}
	
	public boolean getAjustado() {
		return ajustado;
	}

	public void setAjustado(boolean ajustado) {
		this.ajustado = ajustado;
	}
	
	public int getHMax(){
		return hMax;
	}
	
	public String [] getCjtoTerms(){
		return cjtoTerms;
	}
}
