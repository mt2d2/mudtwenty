package message;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ServerMessage extends Message
{
	private static final long	serialVersionUID	= 1L;

	private Command				command;
	private List<String>		arguments;

	public ServerMessage() // Externalizable requires blank constructor
	{
	}

	public ServerMessage(Command command, List<String> arguments)
	{
		super();

		this.arguments = arguments;
		this.command = command;
	}

	public Command getCommand()
	{
		return this.command;
	}

	public List<String> getArguments()
	{
		return this.arguments;
	}

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
