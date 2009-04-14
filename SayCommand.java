
/**
 * A SayCommand is created when the user types "say <something>".
 */
public class SayCommand implements Command {

	private String textSaid;
	private String sayer;

	public SayCommand(String textSaid, String sayer) {
		this.textSaid = textSaid;
		this.sayer = sayer;
	}

	/**
	 * Put a message onto the message queue.
	 *
	 * This say command broadcasts to everyone -- so it uses a constructor
	 * for Message that provides this.
	 */
	public void execute() {
		String str = sayer + " says '" + textSaid + "'";
		MessageSender.addMessage(new Message(str));
		
	}

}
