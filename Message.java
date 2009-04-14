
/**
 * Messages are like letters: they have information about who will receive them
 * as well as the actual message itself.
 *
 * The Message class might be modified to allow sending messages to certain
 * groups of people as well as everyone or an individual.
 */
public class Message {

	private String recepient;

	private boolean isBroadcast;

	private String text;

	/**
	 * Create a broadcast message.
	 */
	public Message(String message) {
		this.recepient = "";
		this.isBroadcast = true;
		this.text = message;
	}

	/**
	 * Create a message addressed to a particular person.
	 */
	public Message(String message, String recepient) {
		this.recepient = recepient;
		this.isBroadcast = false;
		this.text = message;
	}

	public String getText() {
		return text;
	}

	public boolean isBroadcast() {
		return isBroadcast;
	}

	public String getRecepient() {
		return recepient;
	}
}
