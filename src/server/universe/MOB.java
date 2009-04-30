package server.universe;

import java.io.Serializable;

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

	//private BehaviorStrategy strategy;
	private boolean alive;
	
	public MOB(String name)
	{
		super(name);
		this.alive = true;
	}

	/**
	 * This is what the MOB does once it is put into the World. It should,
	 * perhaps, do something about once per second-- look around the room,
	 * attack players if they're there, etc.
	 */
	public void run()
	{
		while (!this.alive)
		{
			//strategy.doAction();
		}
	
	}

}