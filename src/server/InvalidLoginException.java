package server;

/**
 * This exception is thrown in the event that a user provides an invalid
 * combination of username and password. A user visible error message is part of
 * this exception.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class InvalidLoginException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Generate an exception with a user-visible response indicating a the
	 * failure of login, and possibly why.
	 * 
	 * @param string
	 *            error message for the user
	 */
	public InvalidLoginException(String string)
	{
		super(string);
	}
}
