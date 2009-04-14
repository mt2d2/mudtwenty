import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A ClientHandler is a thread that tends to one client. When it's started, it
 * waits for input, blocking, until it receives input, at which point it parses
 * and executes the command.
 * 
 * I am very uncertain about the proper handling of dropped connections in this
 * class. Ideally, if a user disconnects, there is some cleanup that will be run
 * (removing this ClientHandler from the list of threads in ConnectionAccepter,
 * potentially saving data, etc.)
 */
class ClientHandler extends Thread {

	private Socket incoming;

	private BufferedReader in;

	private PrintWriter out;

	private String name;

	/**
	 * When a ClientHandler is created, the instance variables, such as the
	 * input and output objects that will be later used when this thread is
	 * running, are set.
	 * 
	 * The name of the user will be reset once the thread manages to get that
	 * information from the user.
	 * 
	 * @param incoming
	 *            The client socket.
	 * @param id
	 *            The ID number of the client socket.
	 */
	public ClientHandler(Socket incoming) {
		this.incoming = incoming;
		this.name = "";
		try {
			if (incoming != null) {
				this.in = new BufferedReader(new InputStreamReader(incoming
						.getInputStream()));
				this.out = new PrintWriter(new OutputStreamWriter(incoming
						.getOutputStream()));
			}
		} catch (IOException e) {
			System.err.println("Couldn't create a client handler.");
			ConnectionAccepter.removeHandler(this);
		}
	}

	public void run() {
		try {
			this.safePrintln("Hello! Enter quit to exit. What is your name?");
			this.name = in.readLine().trim();
			this.safePrintln("Welcome, " + this.name + ".");
			CommandParser parser = new CommandParser(this.name);
			while (true) {
				safePrint("> ");
				String line = in.readLine();
				try {
					Command cmd = parser.parse(line);
					cmd.execute();
				} catch (InvalidCommandException ice) {
					this.safePrintln("Invalid command. Type 'help'.");
				}
			}
		} catch (IOException e) {
			disconnect();
		} catch (NullPointerException npe) {
			disconnect();
		}
	}

	/**
	 * Send a string to the client if possible.
	 * 
	 * @param msg
	 *            The string message which will be sent to the client.
	 */
	public synchronized void safePrintln(String msg) {
		if (out != null) {
			out.println(msg);
			out.flush();
		}
	}

	/**
	 * Send a string to the client if possible.
	 * 
	 * @param msg
	 *            The string message which will be sent to the client.
	 */
	public synchronized void safePrint(String msg) {
		if (out != null) {
			out.print(msg);
			out.flush();
		}
	}

	public String getUserName() {
		return this.name;
	}

	/**
	 * Disconnect this client from the server and clean up. If possible, notify
	 * the user.
	 * 
	 * In our MUD, a method like this might also be responsible for saving a
	 * player's data.
	 */
	public synchronized void disconnect() {
		this.safePrintln("You are being disconnected.");
		try {
			incoming.close();
		} catch (Exception e) {
		}
		ConnectionAccepter.removeHandler(this);
	}

}
