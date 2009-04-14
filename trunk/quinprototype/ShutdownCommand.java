/**
 * A command to shutdown the server and exit. 
 */
public class ShutdownCommand implements Command {

	public void execute() {
		Main.shutdown();
	}

}
