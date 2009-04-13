package message;

public class ServerMessage extends Message
{	
	private Command command;
	
	public ServerMessage()
	{
		super();
	}
	
	public Command getCommand()
	{
		return this.command;
	}
}
