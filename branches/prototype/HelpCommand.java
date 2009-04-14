
/**
 * A HelpCommand is created when the user types "help".
 */
public class HelpCommand implements Command {

	private String name;

	/**
	 * @param name The name of the person who asked for help.
	 */
	public HelpCommand(String name) {
		this.name = name;
	}

	/**
	 * Put a message onto the message queue.
	 *
	 * This tell command is sent to only one person -- so it uses a constructor
	 * for Message that provides this.
	 */
	public void execute() {
		String help = "Commands:\n"
			+ "say <something> -- Say something to all connected clients\n"
			+ "tell <somebody> <something> -- Say something to one person.\n"
			+ "help -- Get this list of commands.\n"
			+ "quit -- Quit.";
		MessageSender.addMessage(new Message(help, name));
	}

}
