package message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This is the default communication protocol between the Client and Server.
 * This abstract class provides basic functionality, including a timestamp and
 * status of a message. Further, it implements Externalizable to avoid the
 * expensive introspection of Serializable. In the future, this entire object
 * hierarchy could be replaced with a third-party message library, specifically
 * Google's Protocol Buffers.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public abstract class MessageProtocol implements Externalizable
{
	private static final long	serialVersionUID	= 1L;

	protected long				time;
	protected Status			status;

	/**
	 * Sole constructor. Assigns the current time for the timestamp and assumes
	 * a status of OK.
	 */
	public MessageProtocol()
	{
		this.time = System.currentTimeMillis();
		this.status = Status.OK;
	}

	/**
	 * @return timestamp of this message.
	 */
	public long getTime()
	{
		return this.time;
	}

	/**
	 * @return status of this message.
	 */
	public Status getStatus()
	{
		return this.status;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.time = in.readLong();
		this.status = Status.get(in.readByte());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeLong(this.time);
		out.write(this.status.getCode());
	}
}
