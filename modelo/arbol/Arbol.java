package modelo.arbol;

import java.util.Map;

public class Arbol {

	
	/*private int profundidad;
	private boolean esRaiz;

	private int posSimbolo;*/
	
	private Nodo raiz;
    private int numFun = 0;

  	private int cont=0;
  	private Arbol clonAux = null;
  	  	
  	public class Nodo {

        private String valor;
        private Arbol [] hijos;
        private int reff = 0;

    	//Constructora
        Nodo(Arbol [] hi, String val){
            hijos = hi;
            valor = val;
        }

        public int getReff(){
        	return reff;
        }
        
        public Arbol[] gethijos(){
        	return hijos;
        }
        
        public void sethijos(Arbol[] hi){
        	hijos = hi;
        }

        public String getValor(){
        	return valor;
        }
        
        public void setValor(String s){
        	valor = s;
        }
    }

	//Constructora
	public Arbol(){
	       raiz = null;
	}
	public Arbol(String elem){
		raiz = new Nodo(null, elem);
		
	}
	public Arbol(Arbol [] hi, String elem){
	     raiz = new Nodo(hi, elem);
	}
	
	
	public boolean esVacio(){
        return (raiz == null);
    }
	
	
	public int numNodos() {
		int resul = 1;
        if (!esVacio()) {
            
            if(raiz.hijos != null){
            	
            	for(int i=0; i < raiz.hijos.length; i++){
            		resul = resul + raiz.hijos[i].numNodos();
                }
            }else{
         
            	return resul;
            }
            
        }else{
        	return resul;
        }
		return resul;
	}
	
	/****OPERACIONES OBSERVADORAS****/
	
	public void addReff() {
        cont = 0;
        addReffAux(raiz);
	}
	
	public int numFunciones(){
		return numFuncionesAux(raiz);
	}
	
	public int numFuncsMutables(){
		return numFuncsMutablesAux(raiz);
	}
	
	public int numTerminales(){
		return numTerminalesAux(raiz);
	}
	
	public int profundidad(){
		return profundidadAux(raiz);
	}
	
	public String buscarTerminales(){
		return buscarTerminalesAux(raiz);
	}
	
	public void printAB(){
		printABAux(raiz);
	}
	
	/****OPERACIONES DE CLONACION****/
	
	public void subArbol(Nodo ra, int pto){
		subArbolAux(raiz, pto);
	}
	
	public void addSubArbol(Arbol ab, int pto) {
		raiz = addSubArbolAux(raiz, ab, pto);
	}
	
	public Arbol clonar(){
		return clonarAux(raiz);
	}
	
	/****OPERACIONES DE CALCULO****/
	
	public int calcularFuncion(Map<String, Integer> tMap){
		return calcularFuncionAux(raiz, tMap);
	}
	
	/****Metodos auxiliares (recursivos) de observacion****/	
	
	private void addReffAux(Nodo ra) {
		
        if (ra!=null) {
            
            if(ra.hijos != null){
            	cont++;
            	ra.reff = cont;
            	for(int i=0; i < ra.hijos.length; i++){
            		addReffAux(ra.hijos[i].raiz);
                }
            }
        }
	}
	
	private int numFuncionesAux(Nodo ra) {
		int resul = 1;
        if (ra != null) {
            if(ra.hijos != null){
            	for(int i=0; i < ra.hijos.length; i++)
	        		resul = resul + numFuncionesAux(ra.hijos[i].raiz);
	        }else{
	        	return 0;
	        }
        }else{
        	return resul;
        }
        	
		return resul;
	}
	
	//Necesario para la mutación funcional simple
	public int numFuncsMutablesAux(Nodo ra){
		int resul = 0;
		if(ra != null){
			if(ra.hijos != null){
				if(!ra.valor.equals("IF")){
					if(!ra.valor.equals("NOT")){
						resul = 1;
					}
				}
				for(int i = 0; i < ra.hijos.length; i++){
					resul += numFuncsMutablesAux(ra.hijos[i].raiz);
				}
			}else{
				return 0;
			}
			return resul;
		}
		return resul;
	}
	
	private int numTerminalesAux(Nodo ra){
		int resul = 0;		
		if(ra == null){
			return resul;
		}
		if(ra.hijos != null){
			for(int i = 0; i < ra.hijos.length; i++){
				resul += numTerminalesAux(ra.hijos[i].raiz);
			}
		}
		else{
			return 1;
		}
		return resul;
	}

	private int profundidadAux(Nodo ra) {
		
		int mejor = 0, p;
		if(ra == null ){
			return 0;
		}else{
			if( ra.hijos != null){
				for(int i=0; i < ra.hijos.length; i++){
					
			        p =  profundidadAux(ra.hijos[i].raiz);
			        	
			        if (p > mejor){
			        	mejor = p;
			        		
			        }			        	
			    }
				return mejor + 1;
			}else{
				return 1;
			}
		}
	}
	
	private String buscarTerminalesAux(Nodo ra){
		String pre ="";
        if (ra != null) {
            
            if(ra.hijos != null){

            	for(int i=0; i < ra.hijos.length; i++){
                	pre = pre + buscarTerminalesAux(ra.hijos[i].raiz);
                }
            }else{
            	pre = pre +ra.valor +" " ;         	
            	
            	return pre;
            }
        }else{
        	return pre;
        }
		return pre;
    }
	
	private void printABAux(Nodo ra){
        if (ra != null) {
            
            if(ra.hijos != null){
            	//System.out.print( "("+ra.valor + " ref: " +ra.getReff()+ " "  );
            	System.out.print( "("+ra.valor + " "  );
            	for(int i=0; i < ra.hijos.length; i++){
            		printABAux(ra.hijos[i].raiz);
                }
            	System.out.print(")");
            	
            }else{  	
            	
            	System.out.print( " "+ra.valor + " "  );
            }
        }
    }
	
	/****Metodos auxiliares (recursivos) de clonacion****/
	
	private void subArbolAux(Nodo ra, int pto){

		if(ra != null){
			
			if(ra.hijos != null){
				
				for(int i=0; i < ra.hijos.length; i++){
	        		
					subArbolAux(ra.hijos[i].raiz, pto);
	            }
				if(ra.reff == pto){
					clonAux = clonarAux(ra);
				}
			}
		}
  
    }
	
	private Nodo addSubArbolAux(Nodo ra, Arbol add, int pto){
		if(ra != null){
			
			if(ra.hijos != null){
				
				
				if(ra.reff == pto){
					ra = add.raiz;
				
				}else{
					for(int i=0; i < ra.hijos.length; i++){
		        		
						ra.hijos[i].raiz = addSubArbolAux(ra.hijos[i].raiz, add, pto);
		            }
				}
			}
		}
		return ra;
	}
	
	
	private Arbol clonarAux(Nodo ra){
		
		Nodo tempClon = null;
        if (ra != null) {
            
            if(ra.hijos != null){
            	
            	Arbol [] ab = new Arbol[ra.hijos.length];
            	
            	for(int i=0; i < ra.hijos.length; i++){
                	ab[i] = clonarAux(ra.hijos[i].raiz);
                }
            	
            	tempClon = new Nodo (ab, ra.valor);
            	
            }else{
            	
            	tempClon =  new Nodo ( null, ra.valor);
            	
            }
            
        }else{
        	tempClon = null;
        	return null;
        }
        Arbol clon = new Arbol();
        
        clon.raiz = tempClon;
        
		return clon;
    }
	
	/****Metodos auxiliares (recursivos) de calculo****/	
	
	private int calcularFuncionAux(Nodo ra, Map<String, Integer> tMap){
		
		int pre=0;
        if (ra != null) {
            
            if(ra.hijos != null){
            	
            	int [] resul =  new int [ra.hijos.length];
            	
            	
            	for(int i=0; i < ra.hijos.length; i++){
            		
            		resul[i] = calcularFuncionAux(ra.hijos[i].raiz, tMap);
                	
                }
            	
            	//un array [A0,D0,A0]
            	//switch --> IF, AND, ...
            	switch (ra.valor){
            	case "IF":
            		pre =  IF(resul[0], resul[1], resul[2]);
            		break;
            	case "AND":
            		pre = AND(resul[0], resul[1]);
            		break;
            	case "OR":
            		pre = OR(resul[0], resul[1]);
            		break;
            	case "NOT":
            		pre = NOT(resul[0]);
            		break;
            	}

            }else{
            	
            	//Hoja
            	//BUSACAMOS en el hashmap el valor
            	if(tMap.containsKey(ra.valor)){
            		pre = tMap.get(ra.valor);
            	}else{System.out.println("Error: no existe el terminal en este arbol");}
            	         	

            	return pre;
            }
            
        }else{
        	return pre;
        }
		return pre;
	}
	
	private int IF(int v1, int v2, int v3){
		
		if(v1 == 1 ){
			return v2;
		}else{
			return v3;
		}
		
	}
	
	private int AND(int v1, int v2){
		
		if(v1 == 1 && v2 == 1){
			return 1;
		}else{
			return 0;
		}
		
	}
	
	private int OR(int v1, int v2){
		
		if(v1 == 0 && v2 == 0){
			return 0;
		}else{
			return 1;
		}
		
	}
	
	private int NOT(int v){
		if(v == 0){
			return 1;
		}else{
			return 0;
		}
	}
	
	/***************Getters y Setters************/

	public Arbol getClonAux(){
		return clonAux;
	}
	
	public Nodo getRaiz(){
		return raiz;
	}
	
	public void setRaiz(Nodo r){
		raiz = r;
	}
	
	public void setRaiz(String p){
		raiz.valor = p;
	}
	
	public int getNumFun(){
		return numFun;
	}
	
}