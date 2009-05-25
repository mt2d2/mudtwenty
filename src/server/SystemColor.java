package server;

import java.awt.Color;

public class SystemColor
{
	/**
	 * System messages are associated with a single color when sent to the
	 * client. This is a dark green.
	 */
	public static final Color				DEFAULT	= new Color(0, 51, 0);

	/**
	 * Error messages ought to be red, it's what people expect.
	 */
	public static final Color				ERROR	= Color.RED;

	/**
	 * Messages from other users should appear blue. It'll balance the world
	 * out.
	 */
	public static final Color				MESSAGE	= Color.BLUE;
}
