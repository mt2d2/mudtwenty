
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This is the thread that watches a message queue and sends messages off.
 * 
 * This might be a useful way to handle message sending because the
 * ClientHandler threads are all blocking, waiting for input, and the main
 * server thread is blocking, waiting for exceptions.
 * 
 * I am still uncertain about what the best way for other classes to access the
 * message queue should be -- because there is only ever one message queue, it
 * makes sense for all the methods herein to be static.
 */
public class MessageSender extends Thread {

	private static BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(
			10);

	public synchronized static void addMessage(Message message) {
		try {
			messageQueue.put(message);
			System.out.println("MessageSender: A message has been added.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				Message message = messageQueue.take();
				System.out
						.println("MessageSender: A message has been taken off.");
				for (ClientHandler handler : ConnectionAccepter.listHandlers()) {
					if (message.isBroadcast()
							|| message.getRecepient().equals(
									handler.getUserName())) {
						handler.safePrintln(message.getText());
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
