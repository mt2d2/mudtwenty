package server.universe;

public class Sapoman implements Species{
	
	private long health = 30;
	private long mana = 0;
	private long Intel = 15;
	private long STR = 5;
	private long DEF = 5;
	private short HIT = 5;
	private long ATK = 5;
	private long mres = 0;
	private double gFactor = 0.0001;
	private String descript = new String("This is a humanoid species, they lack in" +
			" magical skill, magic defense, and are average mostly. Though they are " + 
			"somewhat intelligent");
	
	
	public Sapoman(){
		
	}

	@Override
	public long getATK() {
		// TODO Auto-generated method stub
		return ATK;
	}

	@Override
	public long getDEF() {
		// TODO Auto-generated method stub
		return DEF;
	}

	@Override
	public double getGFactor() {
		// TODO Auto-generated method stub
		return gFactor;
	}

	@Override
	public short getHIT() {
		// TODO Auto-generated method stub
		return HIT;
	}

	@Override
	public long getHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public long getMRes() {
		// TODO Auto-generated method stub
		return mres;
	}

	@Override
	public long getMana() {
		// TODO Auto-generated method stub
		return mana;
	}

	@Override
	public long getSTR() {
		// TODO Auto-generated method stub
		return STR;
	}

	@Override
	public void setFactor(double chg) {
		// TODO Auto-generated method stub
		gFactor = chg;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return descript;
	}

	@Override
	public long getIntel() {
		// TODO Auto-generated method stub
		return Intel;
	}
	
}
