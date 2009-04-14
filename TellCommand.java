/**
 * A TellCommand is created when the user types "tell <someone> <something>".
 */
public class TellCommand implements Command {

	private String textSaid;

	private String receiver;

	private String teller;

	public TellCommand(String receiver, String textSaid, String teller) {
		this.receiver = receiver;
		this.textSaid = textSaid;
		this.teller = teller;
	}

	/**
	 * Put a message onto the message queue.
	 * 
	 * This tell command is sent to only one person -- so it uses a constructor
	 * for Message that provides this.
	 */
	public void execute() {
		String toReceiver = teller + " tells you '" + textSaid + "'";
		MessageSender.addMessage(new Message(toReceiver, receiver));
		String toTeller = "You tell " + receiver + " '" + textSaid + "'";
		MessageSender.addMessage(new Message(toTeller, teller));
	}

}
