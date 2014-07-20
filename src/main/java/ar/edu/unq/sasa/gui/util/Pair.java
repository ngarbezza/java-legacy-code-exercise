package ar.edu.unq.sasa.gui.util;

/**
 * Clase para representar un Par de objetos.
 */
public class Pair<A, B> {
	private Object first;
	private Object second;
	
	public Pair(A fst, B snd){
		first = fst;
		second = snd;
	}
	
	public Object getFirst(){
		return first;
	}
	
	public Object getSecond(){
		return second;
	}
}
