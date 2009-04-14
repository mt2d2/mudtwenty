import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * I made this example to illustrate one way that we could deal with connections
 * and commands.
 * 
 * This chat server is capable of: - Accepting different kinds of chat commands,
 * and conditionally sending messages to everyone or a specific person. -
 * Sending messages to clients at any time, not just in response to a user
 * command.
 * 
 * The ChatServer class contains the main method of the server. It opens up a
 * ServerSocket that waits for connections and spawns ClientHandler threads to
 * deal with clients, and it keeps track of the ClientHandlers that it spawned.
 * 
 * You can test out this prototype by running it and telnetting to port 4000 on
 * localhost.
 */
public class ConnectionAccepter extends Thread {

	private static List<ClientHandler> activeThreads;

	private static final int PORT = 4000; // For use in the lab.

	public synchronized static void removeHandler(ClientHandler handler) {
		activeThreads.remove(handler);
	}

	public synchronized static List<ClientHandler> listHandlers() {
		return activeThreads;
	}

	public void run() {
		activeThreads = new ArrayList<ClientHandler>();

		try {
			ServerSocket s = new ServerSocket(PORT);
			System.out.println("ChatServer: started.");
			while (true) {
				Socket incoming = s.accept();
				System.out.println("ChatServer: Spawning client thread.");
				ClientHandler newThread = new ClientHandler(incoming);
				activeThreads.add(newThread);
				newThread.start();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}

		System.out.println("ChatServer: stopped.");
	}

}
