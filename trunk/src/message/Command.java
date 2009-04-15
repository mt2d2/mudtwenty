package message;

/**
 * A simple list of acceptable commands in the MUD. User input must match one of
 * these options; all recognized input is directed to UNKNOWN.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public enum Command
{
	ECHO, EXIT, LOOK, OOC, UNKNOWN
}
