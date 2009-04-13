package message;

import java.io.Serializable;

public abstract class Message implements Serializable 
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
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
