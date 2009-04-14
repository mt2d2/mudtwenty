package message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class Message implements Externalizable 
{
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

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.time = in.readLong();
		this.status = Status.valueOf(in.readUTF());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(this.time);
		out.writeUTF(this.status.name());
	}
}
