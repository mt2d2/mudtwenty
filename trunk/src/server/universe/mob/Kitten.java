package server.universe.mob;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.universe.Creature;

/**
 * The kitten is clearly the cutest mob. It moves and meows.
 */
public class Kitten extends MOB
{

	private static final long serialVersionUID = 2L;
	
	/**
	 * Create a kitten with the given name.
	 */
	public Kitten(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a cute, little, white, fluffy kitten. How cute!");
		this.setDialog(new MeowDialog());
	}
	
	/**
	 * The "dialog strategy" of meowing in response to everything. Very cute.
	 */
	private class MeowDialog implements DialogStrategy
	{
		private static final long serialVersionUID = 1L;

		public void tell(Creature sender, String message)
		{
			Server.getUniverse().sendMessageToCreature(Kitten.this, sender,
					new ClientMessage("Fluffy says Meow.", Status.CHAT, Server.MESSAGE_TEXT_COLOR));
		}
	}

	/**
	 * Move sometimes, else do nothing.
	 */
	public void takeTurn()
	{
		if (Math.random() > 0.5)
			setBehavior(new MoveBehavior());
		else
			setBehavior(new NullBehavior());
	}
}
