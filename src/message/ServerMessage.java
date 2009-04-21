package message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * This extends MessageProtocol, providing data useful for a Server to receive.
 * That is, a client might send a ServerMessage to a server -- this is not a message
 * that a server might create.
 * In this message, the server receives a command and a List<String> arguments.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ServerMessage extends MessageProtocol
{
	private static final long	serialVersionUID	= 1L;

	private Command				command;
	private List<String>		arguments;

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
	public ServerMessage(Command command, List<String> arguments)
	{
		super();

		this.arguments = arguments;
		this.command = command;
	}

	/**
	 * @return Command to execute on Server.
	 */
	public Command getCommand()
	{
		return this.command;
	}

	/**
	 * @return list of arguments for this message's command
	 */
	public List<String> getArguments()
	{
		return this.arguments;
	}

	/* (non-Javadoc)
	 * @see message.MessageProtocol#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		super.readExternal(in);

		// read command, non-nullable
		//this.command = Command.valueOf(in.readUTF());
		this.command = Command.get(in.readByte());
		
		// read arguments List, non-nullable
		int arrayCount = in.readInt();
		this.arguments = new ArrayList<String>();
		// read arguments, nullable
		if (arrayCount > 0)
		{
			for (int i = arrayCount; i > 0; i--)
				this.arguments.add(in.readUTF());
		}
	}

	/* (non-Javadoc)
	 * @see message.MessageProtocol#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		// properties of message
		super.writeExternal(out);

		// write Command, non-nullable
		out.write(this.command.getCode());
		
		// write arguments List, non-nullable
		out.writeInt(this.arguments.size());
		// write arguments, nullable
		for (String s : this.arguments)
			out.writeUTF(s);
	}
}
