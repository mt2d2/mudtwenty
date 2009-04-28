package server.connection;

import java.io.IOException;
import java.net.Socket;

import message.ClientMessage;
import message.ServerMessage;

/**
 * Provides a common interface for connecting and communicating with a client.
 * This neatly abstracts certain concepts that are useful in the Template design
 * pattern found in ServerThread.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public interface Communicable
{
	/**
	 * Opens a connection to a client. A read/write connection can be
	 * established using the provided socket.
	 * 
	 * @param socket
	 *            client connection as a socket
	 * @throws IOException
	 */
	public void openConnection(Socket socket) throws IOException;

	/**
	 * This blocks until a message is sent from the client, at which put the
	 * method returns. This is useful in a loop to constantly get input from the
	 * client.
	 * 
	 * @return message sent from the client
	 */
	public ServerMessage getMessage();

	/**
	 * Sends a message to a client.
	 * 
	 * @param message
	 *            the message that is sent to the client
	 */
	public void sendMessage(ClientMessage message);

	/**
	 * Closes the connection to the client.
	 */
	public void terminate();
}
