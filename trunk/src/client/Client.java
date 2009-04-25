package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;

import message.Command;
import message.ServerMessage;
import util.InputParser;

/**
 * GUI client that can connect to the MUD Server. Allows the user to configure
 * the host and port of the server, and, if the connection were successful,
 * communicates with the server through a basic terminal emulator.
 * 
 * This is a huge work in progress. Currently, the user has no option when
 * connecting to a server. It currently only connects to localhost on port 8080.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Client extends JFrame
{
	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static Logger				logger					= Logger.getLogger("mudtwenty");

	private static final long			serialVersionUID		= 1L;

	private static final Color			DEFAULT_TEXT_COLOR		= Color.BLACK;
	private static final String			GAME_CARD				= "GAME_CARD";
	private static final String			CONNECTOR_CARD			= "CONNECTOR_CARD";
	private static final Dimension		FRAME_SIZE				= new Dimension(640, 480);
	private static final List<Command>	ALLOWED_CHAT_COMMANDS	= Arrays.asList(new Command[] { Command.OOC, Command.SAY, Command.TELL, Command.WHO });

	private ClientThread				clientThread;
	private JPanel						cards;

	// game panel
	private JTextPane					gameArea;
	private JTextField					gameField;

	// chat panel
	private JTextPane					chatArea;
	private JTextField					chatField;

	/**
	 * Sole constructor for client. Sets up the JFrame, adding appropriate
	 * contents and listeners.
	 */
	public Client()
	{
		// setup the JFrame
		this.setTitle("mudtwenty");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(this.layoutMenuBar());
		this.setPreferredSize(FRAME_SIZE);
		this.cards = new JPanel(new CardLayout());

		this.cards.add(this.layoutSplitInterace(), GAME_CARD);

		this.cards.add(this.layoutConnectorInterface(), CONNECTOR_CARD);
		this.getContentPane().add(this.cards);
		this.pack();

		// spawn the ClientThread to communicate with the server
		// TODO move to ActionListener on Button from connector screen
		this.clientThread = new ClientThread(this, "localhost", 4000);
		new Thread(this.clientThread).start();

		// request focus on the input as default
		this.gameField.requestFocusInWindow();

		// add a window close listener
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				Client.this.handleExitEvent();
			}
		});
	}

	/**
	 * Adds text to the server display window with a null color. The system will
	 * resort to its default color.
	 * 
	 * @param text
	 *            the text to append
	 */
	public void appendServerText(String text)
	{
		this.appendGameText(text, null);
	}

	/**
	 * Adds text to the server display window with a given color.
	 * 
	 * @param input
	 *            the text to append
	 * @param color
	 *            the color to append that text in
	 */
	public void appendGameText(String input, Color color)
	{
		this.appendToArea(this.gameArea, input, color);
	}

	/**
	 * Adds text to the chat display window with a given color.
	 * 
	 * @param text
	 *            the text to append
	 * @param color
	 *            the color to append that text in
	 */
	public void appendChatText(String text, Color color)
	{
		this.appendToArea(this.chatArea, text, color);
	}

	/**
	 * @param area
	 * @param input
	 * @param color
	 */
	private void appendToArea(JTextPane area, String input, Color color)
	{
		Document doc = area.getDocument();
		StyleConstants.setForeground(area.getStyle("color"), color == null ? DEFAULT_TEXT_COLOR : color);

		try
		{
			doc.insertString(doc.getLength(), input + "\n", area.getStyle("color"));
		}
		catch (BadLocationException e)
		{
			// don't log, should never get here
			e.printStackTrace();
		}
	}

	/**
	 * Gracefully handles an exit event. This asks the connection to the server
	 * to close before quitting the client.
	 */
	private void handleExitEvent()
	{
		this.clientThread.closeConnection();
		System.exit(0);
	}

	/**
	 * Simple about screen. Nothing fancy here.
	 */
	private void handleAboutEvent()
	{
		JOptionPane.showMessageDialog(this, "About");
	}

	/**
	 * Invoked when the user wishes to send a message to the server, either by
	 * hitting enter when the entry field is in focus or by pressing the send
	 * button.
	 */
	private void handleChatSendEvent()
	{
		// disable textField and grab input
		String input = this.chatField.getText();

		// don't do anything if the user didn't type any input.
		if (input.length() == 0)
			return;

		// check to see if its a chat command
		ServerMessage message = InputParser.parse(input.trim());

		if (ALLOWED_CHAT_COMMANDS.contains(message.getCommand()))
		{
			// send the message to the server
			this.chatField.setEnabled(false);
			this.clientThread.sendMessage(message);

			// restore text to blank
			this.chatField.setText("");
			this.chatField.setEnabled(true);
			this.chatField.requestFocusInWindow();
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Only chat commands are allowed in the chat area; e.g., ooc, say, tell, and who are allowed.");
		}
	}

	/**
	 * Invoked when the user wishes to send a message to the server, either by
	 * hitting enter when the entry field is in focus or by pressing the send
	 * button.
	 */
	private void handleGameSendEvent()
	{
		// disable textField and grab input
		String input = this.gameField.getText();

		// don't do anything if the user didn't type any input.
		if (input.length() == 0)
			return;

		// send the message to the server
		this.gameField.setEnabled(false);
		this.clientThread.sendMessage(InputParser.parse(input.trim()));

		// restore text to blank
		this.gameField.setText("");
		this.gameField.setEnabled(true);
		this.gameField.requestFocusInWindow();
	}

	/**
	 * @return JMenuBar that will be used by the application
	 */
	private JMenuBar layoutMenuBar()
	{
		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Client.this.handleAboutEvent();
			}
		});

		fileMenu.add(about);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Client.this.handleExitEvent();
			}
		});
		fileMenu.add(exit);

		menu.add(fileMenu);

		return menu;
	}

	/**
	 * @return JPanel with components related to the server and port setup
	 */
	private JPanel layoutConnectorInterface()
	{
		JPanel panel = new JPanel();

		panel.add(new JLabel("Connect?"));
		panel.add(new JButton("Connect"));

		return panel;
	}

	/**
	 * @return the split interface between the chat and the game
	 */
	private JSplitPane layoutSplitInterace()
	{
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		splitPane.add(this.layoutChatInterfae());
		splitPane.add(this.layoutGameInterface());

		return splitPane;
	}

	/**
	 * @return JPanel to hold all chat interactions
	 */
	private JPanel layoutChatInterfae()
	{
		ActionListener sendEventListener = new SendEventListener(true);
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension((int) (FRAME_SIZE.width * 0.3), FRAME_SIZE.height));

		// setup instance components
		this.chatArea = new JTextPane();
		this.chatArea.addStyle("color", null);
		this.chatArea.setEditable(false);
		this.chatField = new JTextField();
		this.chatField.registerKeyboardAction(sendEventListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		// text area
		panel.add(new JScrollPane(this.chatArea), BorderLayout.CENTER);

		// entry panel
		JPanel entryPanel = new JPanel(new BorderLayout());
		entryPanel.add(this.chatField, BorderLayout.CENTER);
		JButton chatButton = new JButton("Chat");
		chatButton.addActionListener(sendEventListener);
		entryPanel.add(chatButton, BorderLayout.EAST);
		panel.add(entryPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * @return JPanel containing the components for the terminal emulator
	 */
	private JPanel layoutGameInterface()
	{
		JPanel panel = new JPanel(new BorderLayout());
		ActionListener sendEventListener = new SendEventListener(false);

		// setup instance components
		this.gameArea = new JTextPane();
		this.gameArea.addStyle("color", null);
		this.gameArea.setEditable(false);
		this.gameField = new JTextField();
		this.gameField.registerKeyboardAction(sendEventListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		// text area
		panel.add(new JScrollPane(this.gameArea), BorderLayout.CENTER);

		// entry panel
		JPanel entryPanel = new JPanel(new BorderLayout());
		entryPanel.add(this.gameField, BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(sendEventListener);
		entryPanel.add(sendButton, BorderLayout.EAST);
		panel.add(entryPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Invokes handleSendEvent() when an action is fired.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class SendEventListener implements ActionListener
	{
		private boolean	chat;

		public SendEventListener(boolean isChat)
		{
			this.chat = isChat;
		}

		public void actionPerformed(ActionEvent e)
		{
			if (this.chat)
				Client.this.handleChatSendEvent();
			else
				Client.this.handleGameSendEvent();
		}
	}

	/**
	 * Main entrance to the client. Attempts to set a matching plaf for the
	 * jvm's host and makes a new Client visible.
	 * 
	 * @param args
	 *            there are no arguments to Client
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		catch (ClassNotFoundException e)
		{
			logger.throwing("Client", "main", e);
		}
		catch (InstantiationException e)
		{
			logger.throwing("Client", "main", e);
		}
		catch (IllegalAccessException e)
		{
			logger.throwing("Client", "main", e);
		}
		catch (UnsupportedLookAndFeelException e)
		{
			logger.throwing("Client", "main", e);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new Client().setVisible(true);
			}
		});
	}
}
