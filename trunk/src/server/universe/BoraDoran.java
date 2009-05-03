package server.universe;

public class BoraDoran implements Species{

	private long health = 55;
	private long mana = 15;
	private long Intel = 0;
	private long STR = 1;
	private long DEF = 1;
	private short HIT = 5;
	private long ATK = 5;
	private long mres = 50;
	private double gFactor = 0.00025;
	private String descript = new String("Dancing creatures who prefer to party and vacation" +
			" whom are assexual. They lack in intelligence, strength, and defense. They grow " +
			"since the are plant-like, and they have some serious mana skills");
	
	public BoraDoran(){
		
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
	public String getDescription() {
		// TODO Auto-generated method stub
		return descript;
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
	public long getIntel() {
		// TODO Auto-generated method stub
		return Intel;
	}
	
	
	
}
