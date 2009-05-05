package server.universe.mob;

import java.util.List;

import server.Server;

/**
 * ICritters, like the ICritters of our previous project, delight in breeding.
 */
public abstract class ICritter extends MOB
{

	private static final long serialVersionUID = 2L;

	/**
	 * Create a new, general ICritter.
	 */
	public ICritter(String name)
	{
		super(name);
		this.setMaxHealth(20);
	}

	/**
	 * Reproduce sometimes, but do nothing at other times.
	 */
	public void takeTurn()
	{
		double random = Math.random();
		if (random > 0.2)
			setBehavior(new ReproduceBehavior());
		else if (random > 0.8)
			setBehavior(new NullBehavior());

	}

	/**
	 * Reproduce with another ICritter if possible.
	 */
	private class ReproduceBehavior implements BehaviorStrategy
	{

		private static final long serialVersionUID = 1L;

		/**
		 * Breed if another ICritter is found and the room hasn't reached its carrying capacity.
		 */
		public void doAction(MOB mob)
		{
			List<MOB> mobs = Server.getUniverse().getMOBsInRoom(mob.getRoom());
			if (mobs.size() < 10)
			{
				for (MOB otherMob : mobs)
				{
					if (otherMob instanceof ICritter)
					{
						breed(mob, otherMob);
						break;
					}
				}
			}
		}

		private void breed(MOB mob, MOB otherMob)
		{
			MOB child = mob.clone();
			Server.getUniverse().spawnMob(child, mob.getRoom());
		}
	}

}

