package server.universe.mob;

import java.io.Serializable;

//I win the game Aaron gets to commit
/**
 * A BehaviorStrategy represents a behavior that a MOB can have at any given time.
 * For example, a different behaviors could be the behaviors
 * of moving/fighting, running away, or staying still.
 */
public interface BehaviorStrategy extends Serializable
{

	/**
	 * In this method, one action will be performed;
	 * this action will depend on both the particular strategy and
	 * the MOB that is performing the action.
	 */
	public void doAction(MOB mob);

}
