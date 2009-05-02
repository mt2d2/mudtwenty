package server.universe;

import server.Server;

public class Kitten extends MOB {

	private static final long serialVersionUID = 1L;
	
	public Kitten(String name) {
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a cute, little, white, fluffy kitten. How cute!");
		this.setBehavior(new StationaryBehavior());
		this.setDialog(new MeowDialog());
	}
	
	private class MeowDialog implements DialogStrategy
	{
		private static final long serialVersionUID = 1L;

		public void tell(Creature sender, String message)
		{
			Server.getUniverse().sendMessageToCreature(Kitten.this, sender, "Meow.");
		}
	}
}
