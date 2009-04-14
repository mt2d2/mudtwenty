import java.util.Scanner;

/**
 * This is the entry point for the chat server.
 * 
 * In this prototype, there are several threads, all waiting for different
 * things. Each thread is like a person doing a different job:
 * 
 * The Main thread listens to input on the console (i.e. shutdown, kick). The
 * ConnectionAccepter thread waits for connections. The ClientHandlers wait for
 * input from users. The MessageSender waits for messages to send to users.
 * Later, there might be MOB threads.
 * 
 * There are more ways to handle the 'accepting input at the server console'
 * problem. One way that would make sense is to somehow login a mod with the
 * streams System.in and System.out rather than socket.getInputStream() etc.
 */
public class Main {

	/**
	 * Start the necessary threads then start listening for commands.
	 * 
	 * @param args
	 *            The arguments are ignored.
	 */
	public static void main(String[] args) {

		// The threads are started in a particular order:
		new ConnectionAccepter().start();
		// The message sender needs the connection accepter's thread list.
		new MessageSender().start();
		// The event generator needs the message sender's queue.
		new EventGenerator().start();

		listenToCommands();
	}

	/**
	 * In our MUD, this method might serialize and save the world.
	 */
	public static void shutdown() {
		System.out.println("Server shutting down!");
		System.exit(0);
	}

	/**
	 * Listen to commands on the server.
	 * 
	 * Later, we could use the same command parser as is used for clients, so
	 * that both (a) mods in clients and (b) a mod at the server computer can
	 * run the same commands (i.e. shutdown, kick, ban ...)
	 */
	private static void listenToCommands() {
		CommandParser parser = new CommandParser("god");
		Scanner keyboard = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String line = keyboard.nextLine();
			try {
				Command cmd = parser.parse(line);
				cmd.execute();
			} catch (InvalidCommandException e) {
				System.out.println("Invalid command.");
			}
		}
	}
}
