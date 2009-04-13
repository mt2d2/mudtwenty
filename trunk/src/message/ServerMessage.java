package message;

public class ServerMessage extends Message
{	
	private Command command;
	
	public ServerMessage(Command command)
	{
		super();
		
		this.command = command;
	}
	
	public Command getCommand()
	{
		return this.command;
	}
}
