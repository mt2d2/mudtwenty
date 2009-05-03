package server.universe.mob;

import java.io.Serializable;

import server.universe.Creature;

/**
 * The MOB class represents a mobile non-player character.
 *
 * Thus quoteth The Spec:
 *
 * "MOBs can also move from room to room, and players may have complex
 * interactions with them. Though most MOBs are hostile towards the player, they
 * do not all need to be. Some MOBs may offer quests, services or games to the
 * players."
 *
 */
public abstract class MOB extends Creature implements Runnable, Serializable
{
	private static final long	serialVersionUID	= 1L;
	
	private static final long SLEEP_TIME = 1000;

 	private BehaviorStrategy behavior;
 	private DialogStrategy dialog;
 	private boolean alive;

	public MOB(String name)
	{
		super(name);
 		this.alive = true;
 		this.behavior = new NullBehavior();
 		this.dialog = new NullDialog();
	}

	/**
	 * This is what the MOB does once it is put into the World. It should,
	 * perhaps, do something about once per second-- look around the room,
	 * attack players if they're there, etc.
	 */
	public void run()
	{
 		while (this.alive)
 		{
 			behavior.doAction(this);
 			
 			// Check whether the MOB's dead yet.
 			if (this.getHealth() <= 0)
 				this.alive = false;
 			
 			// Sleep until the next found.
 			try
 			{
				Thread.sleep(SLEEP_TIME);
			}
 			catch (InterruptedException e)
 			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		}
	}
	
	/**
	 * Receive a message (from either a player or another MOB).
	 * This will be called when a user says something in the room of a MOB
	 * or tells something to a mob.
	 * 
	 * Depending on the current DialogStrategy of the MOB, the MOB may ignore
	 * the message or respond in some way.
	 * 
	 * @param message the text of what is said.
	 */
	public void tell(Creature sender, String message)
	{
		dialog.tell(sender, message);
	}
	
	/**
	 * Set the current behavior strategy.
	 */
	public void setBehavior(BehaviorStrategy behavior)
	{
		this.behavior = behavior;
	}
	
	/**
	 * Set the current dialog strategy.
	 */
	public void setDialog(DialogStrategy dialog)
	{
		this.dialog = dialog;
	}
	
	private class NullBehavior implements BehaviorStrategy
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Do the action of doing nothing. How zen.
		 */
		public void doAction(MOB mob)
		{
		}
	}

	private class NullDialog implements DialogStrategy
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Listen but do not speak. How peaceful.
		 */
		public void tell(Creature sender, String message)
		{
		}
	}

}