package message;

public abstract class Message
{
	protected long	time;
	protected Status status;
	
	public Message()
	{
		this.time = System.currentTimeMillis();
		this.status = Status.OK;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public Status getStatus()
	{
		return this.status;
	}
}
