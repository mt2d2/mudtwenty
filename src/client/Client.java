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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;

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
	private static Logger		logger				= Logger.getLogger("mudtwenty");

	private static final long	serialVersionUID	= 1L;

	private static final Color	DEFAULT_TEXT_COLOR	= Color.BLACK;
	private static final String	GAME_CARD			= "GAME_CARD";
	private static final String	CONNECTOR_CARD		= "CONNECTOR_CARD";

	private JPanel				cards;
	private JTextPane			textArea;
	private JTextField			textField;
	private ClientThread		clientThread;

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
		this.setPreferredSize(new Dimension(640, 480));
		this.cards = new JPanel(new CardLayout());
		this.cards.add(this.layoutGameInterface(), GAME_CARD);
		this.cards.add(this.layoutConnectorInterface(), CONNECTOR_CARD);
		this.getContentPane().add(this.cards);
		this.pack();

		// spawn the ClientThread to communicate with the server
		// TODO move to ActionListener on Button from connector screen
		this.clientThread = new ClientThread(this, "localhost", 4000);
		new Thread(this.clientThread).start();

		// request focus on the input as default
		this.textField.requestFocusInWindow();

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
		this.appendServerText(text, null);
	}

	/**
	 * Adds text to the server display window with a given color.
	 * 
	 * @param text
	 *            the text to append
	 * @param color
	 *            the color to append that text in
	 */
	public void appendServerText(String text, Color color)
	{
		Document doc = this.textArea.getDocument();
		StyleConstants.setForeground(this.textArea.getStyle("color"), color == null ? DEFAULT_TEXT_COLOR : color);

		try
		{
			doc.insertString(doc.getLength(), text + "\n", this.textArea.getStyle("color"));
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
	private void handleSendEvent()
	{
		// disable textField and grab input
		String input = this.textField.getText();

		// don't do anything if the user didn't type any input.
		if (input.length() == 0)
			return;

		// send the message to the server
		this.textField.setEnabled(false);
		this.clientThread.sendMessage(input.trim());

		// restore text to blank
		this.textField.setText("");
		this.textField.setEnabled(true);
		this.textField.requestFocusInWindow();
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
	 * @return JPanel containing the components for the terminal emulator
	 */
	private JPanel layoutGameInterface()
	{
		JPanel panel = new JPanel(new BorderLayout());
		ActionListener sendEventListener = new SendEventListener();

		// setup instance components
		this.textArea = new JTextPane();
		this.textArea.addStyle("color", null);
		this.textArea.setEditable(false);
		this.textField = new JTextField();
		this.textField.registerKeyboardAction(sendEventListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);

		// text area
		panel.add(new JScrollPane(this.textArea), BorderLayout.CENTER);

		// entry panel
		JPanel entryPanel = new JPanel(new BorderLayout());
		entryPanel.add(this.textField, BorderLayout.CENTER);
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
		public void actionPerformed(ActionEvent e)
		{
			Client.this.handleSendEvent();
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
