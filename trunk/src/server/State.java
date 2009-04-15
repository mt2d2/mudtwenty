package server;

/**
 * Represents the state of a ServerThread. For example, if the thread is up and
 * running, processing input, then its state will be OK. If the client quits (or
 * is terminated), the ServerThread's state will be DONE.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public enum State
{
	/**
	 * ServerThread is connected to client, processing events successfully.
	 */
	OK,

	/**
	 * ServerThread is not connected to client, event processing terminated
	 */
	DONE
}
