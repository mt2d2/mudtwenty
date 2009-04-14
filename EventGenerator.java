
/**
 * An event generator just generates messages which are put on the
 * message queue. This is used to simulate events in the world that
 * are not a response to a user's commands.
 */
public class EventGenerator extends Thread {

	private static final int delay = 20000;
	
	public void run() {
	while (true) {
		try {
			sleep(delay);
		} catch (InterruptedException ie) {
		}
		Message message = new Message("The event generator has generated an event!");
		MessageSender.addMessage(message);
		}
	}
}
