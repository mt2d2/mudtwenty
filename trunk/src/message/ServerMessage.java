package message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * This extends MessageProtocol, providing data useful for a Server to receive.
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
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		// avoid a call to super for speed
		// super.readExternal(in);
		this.time = in.readLong();
		this.status = Status.valueOf(in.readUTF());

		// read command, non-nullable
		this.command = Command.valueOf(in.readUTF());

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
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		// properties of message
		// avoid a call to super for speed
		// super.writeExternal(out);
		out.writeLong(this.time);
		out.writeUTF(this.status.name());

		// write command, non-nullable
		out.writeUTF(this.command.name());

		// write arguments List, non-nullable
		out.writeInt(this.arguments.size());
		// write arguments, nullable
		for (String s : this.arguments)
			out.writeUTF(s);
	}
}
