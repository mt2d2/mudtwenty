package server.universe.mob;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.universe.Creature;

public class Deer extends MOB {

	private static final long serialVersionUID = 2L;

	public Deer(String name) {
		super(name);
		this.setMaxHealth(1);
		this.setDescription("A great, noble animal of the forest.");
	}

	public void takeTurn() {
		if (Math.random() > 0.75)
			setBehavior(new MoveBehavior());
		else
			setBehavior(new NullBehavior());
	}
}
