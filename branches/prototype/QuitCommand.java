/**
 * A command to disconnect some user.
 * 
 * A user to disconnect needs to be specified -- this might not necessarily be
 * the string name of the user.
 */
public class QuitCommand implements Command {

	private String name;

	/**
	 * @param name
	 *            The person who asked to quit.
	 */
	public QuitCommand(String name) {
		this.name = name;
	}

	public void execute() {
		for (ClientHandler thread : ConnectionAccepter.listHandlers()) {
			if (thread.getUserName().equals(this.name)) {
				thread.disconnect();
				break; // The user has been found.
				// If the loop doesn't break here, an exception is thrown.
			}
		}
	}

}
