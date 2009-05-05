package server.universe.mob;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.universe.Creature;

public class Bunny extends MOB {

	private static final long serialVersionUID = 2L;

	public Bunny(String name) {
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a cute, little, white, fluffy bunny. It looks hungry.");
	}

	public void takeTurn() {
		if (Math.random() > 0.75)
			setBehavior(new MoveBehavior());
		else
			setBehavior(new NullBehavior());
	}
}
