package server.universe;

import java.io.Serializable;

/**
 * A BehaviorStrategy represents a behavior that a MOB can have at any given time.
 * For example, a different behaviors could be the behaviors
 * of moving/fighting, running away, or staying still.
 */
public interface DialogStrategy extends Serializable
{

	/**
	 * This method will be called whenever somebody says something to a MOB.
	 * It will then probably 'say' or 'tell' something in response to this
	 * message.
	 */
	public void tell(Creature sender, String message);

}
