package server.universe.mob;

import server.Server;
import server.universe.Creature;

public class Kitten extends MOB {

	private static final long serialVersionUID = 1L;
	
	public Kitten(String name) {
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a cute, little, white, fluffy kitten. How cute!");
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
