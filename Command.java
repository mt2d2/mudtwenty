
/**
 * Later, in the MUD, a Command might represent anything that can be executed,
 * and a Command might be created whenever the user sends a command.
 */
public interface Command {
	/**
	 * Carry out the command.
	 */
	void execute();
}
