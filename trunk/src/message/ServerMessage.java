package message;

import java.util.List;

public class ServerMessage extends Message
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	

	private Command	command;
	private List<String> arguments;
	
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
}
