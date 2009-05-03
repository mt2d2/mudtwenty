package server.universe;

public abstract class Species {

	private long health = 30;
	private long mana = 10;
	private long STR = 5;
	private long DEF = 5;
	private short HIT = 5;
	private long ATK = 5;
	private long mres = 5;
	private double gFactor = 0.01;
	
	public Species(){
		
		
	}
	
	public long getHealth(){
		
		return health;
		
	}
	
	public long getMana(){
		
		return mana;
	}
	
	
	public long getSTR(){
		
		return STR;
	}
	
	public long getDEF(){
		
		return DEF;
	}
	
	public short getHIT(){
		
		return HIT;
	}
	
	public long getATK(){
		
		return ATK;
	}
	
	public long getMRes(){
		
		return mres;
	}
	
	public double getGFactor(){
		
		return gFactor;
				
	}
	
	public void set(double chg){
		
		gFactor = chg;
		
	}
	
}
