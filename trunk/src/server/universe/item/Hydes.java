package server.universe.item;

import java.util.ArrayList;

import message.ClientMessage;
import server.universe.Player;


public class Hydes extends Armor implements Cloneable{

	private ArrayList<String> arrSpecies;
	private long DEF = 5;
	
	//Humans ans gnorians can wear hydes.
	public Hydes(){
		arrSpecies.add("Sapomen");
		arrSpecies.add("Gnorian");
		
	}
	
	public void setDEF(long bonus){
		
		DEF = bonus;
		
	}
	
	//Determines which species can wear hydes.
	public ArrayList<String> wearable(){
		
		return arrSpecies;
	}
	
	public long getDEF(){
		
		return DEF;
		
	}
	
}
