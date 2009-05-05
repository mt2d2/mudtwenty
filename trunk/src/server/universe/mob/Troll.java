package server.universe.mob;

import message.ClientMessage;
import server.Server;
import server.universe.Creature;
/**
 * The merchant class models a MOB that can sell his items to players.
 * This creature will sell whatever he has in his inventory until runs out.
 */
public class Troll extends MOB {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor sets up dialog.
	 * 
	 * @param name
	 * 			name of the merchant
	 */
	public Troll(String name) {
		super(name);
		this.setMaxHealth(20);
		this.setDescription("Ugly and covered with phlegm. Attack it if you dare!");
		this.setDialog(new TauntDialog());
		this.setAttack(5);
	}
	
	/**
	 * A simple dialog, simply taunting the user if they say anything. 
	 */
	private class TauntDialog implements DialogStrategy
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Taunts a player if they say anything.
		 */
		public void tell(Creature sender, String message)
		{
			Server.getUniverse().sendMessageToCreature(Troll.this, sender, new ClientMessage("Attack me if you dare!"));
		}
	}
	
	/**
	 * Don't do anything.
	 */
	public void takeTurn()
	{
		setBehavior(new NullBehavior());
	}
	
	/**
	 * Heal player, remove potion from inventory, and possibly notify user.
	 */
	public ClientMessage attack(Creature creature)
	{
		if (this.getHealth() > 0)
		{
			creature.decreaseHealth(this.getAttack());
			this.decreaseHealth(creature.getAttack());
			if (this.getHealth() <= 0)
				return new ClientMessage("You killed " + this.getName());
			else
				return new ClientMessage("You attack " + this.getName() + " for " + creature.getAttack() + " and he gets you back for " + this.getAttack());
		}
		else
		{
			return new ClientMessage("This troll is already dead!");
		}
	}
}