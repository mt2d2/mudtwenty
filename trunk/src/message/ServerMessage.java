package message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This extends MessageProtocol, providing data useful for a Server to receive.
 * It could replace ServerMessage.
 */
public class ServerMessage extends MessageProtocol {
	
	private static final long	serialVersionUID	= 1L;

	private String text;

	/**
	 * This constructor is required by Externalizable. There are no setters to
	 * access the data, making a blank message. DO NOT USE!
	 */
	public ServerMessage() // Externalizable requires blank constructor
	{
	}

	/**
	 * Constructs a message that contains a Command for Server to execute and a
	 * list of arguments.
	 * 
	 * @param command
	 *            Command to be executed by the Server
	 * @param arguments
	 *            arguments passed from the client for a given command
	 */
	public ServerMessage(String text)
	{
		super();
		this.text = text;
	}

	/**
	 * Get the text of the message.
	 */
	public String getPayload()
	{
		return this.text;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		super.readExternal(in);
		// text of message
		this.text = in.readUTF();
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		// properties of message
		super.writeExternal(out);
		// text of message
		out.writeUTF(this.text);
	}

}
