/**
 * This will not necessarily be a separate class. (Originally, I was thinking of
 * putting the parse method in the client handler class).
 * 
 * But here's my rationale for making it one: Both the main method (which waits
 * for commands on the console), and each of the client handlers, can
 * potentially use the same commands, if they administrative commands.
 * 
 * Also, it involves a lot of code that is not directly related to handling
 * clients, so it makes sense to separate it out.
 */
public class CommandParser {

	String name;

	/**
	 * The types of commands that are valid depend on whether the invoker is a
	 * mod. This can be checked by checking the name. Also, some commands (such
	 * as 'help') need to know who invoked them.
	 */
	public CommandParser(String name) {
		this.name = name;
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return A Command object, representing the command.
	 * @throws InvalidCommandException
	 */
	public Command parse(String line) throws InvalidCommandException {
		String commandWord = headWord(line);
		int numWords = numWords(line);
		boolean isMod = this.name.equals("god");
		if (commandWord.equals("help")) {
			return new HelpCommand(this.name);
		} else if (commandWord.equals("say") && numWords > 1) {
			return new SayCommand(tailWords(line), this.name);
		} else if (commandWord.equals("tell") && numWords > 2) {
			return new TellCommand(secondWord(line), tailTail(line), this.name);
		} else if (commandWord.equals("shutdown") && isMod) {
			return new ShutdownCommand();
		} else if (commandWord.equals("quit")) {
			return new QuitCommand(this.name);
		} else if (commandWord.equals("kick") && numWords > 1 && isMod) {
			return new QuitCommand(secondWord(line));
		} else {
			throw new InvalidCommandException();
		}
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return The first word that the user typed -- the command word.
	 */
	private String headWord(String line) {
		int index = line.indexOf(" ");
		if (index == -1) {
			return line;
		} else {
			return line.substring(0, index);
		}
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return The second word.
	 */
	private String secondWord(String line) {
		return headWord(tailWords(line));
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return The text that the user typed after the command word.
	 */
	private String tailWords(String line) {
		int index = line.indexOf(" ");
		if (index == -1) {
			return "";
		} else {
			return line.substring(index).trim();
		}
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return The words after the second word.
	 */
	private String tailTail(String line) {
		return tailWords(tailWords(line));
	}

	/**
	 * @param line
	 *            The line of text that the user typed.
	 * @return The number of whitespace-delimited words in the string.
	 */
	private int numWords(String line) {
		return line.split("\\s").length;
	}

}
