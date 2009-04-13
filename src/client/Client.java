package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

public class Client extends JFrame
{
	private static Logger		logger				= Logger.getLogger("mudtwenty");
	private static final long	serialVersionUID	= 1L;
	private static final String	GAME_CARD			= "GAME_CARD";
	private static final String	CONNECTOR_CARD		= "CONNECTOR_CARD";

	private JPanel				cards;
	private JTextPane			textArea;
	private JTextField			textField;
	private ClientThread		clientThread;

	public Client()
	{
		this.setTitle("mudtwenty");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.cards = new JPanel(new CardLayout());
		this.cards.add(this.layoutGameInterface(), GAME_CARD);
		this.cards.add(this.layoutConnectorInterface(), CONNECTOR_CARD);
		this.getContentPane().add(this.cards);

		this.setJMenuBar(this.layoutMenuBar());
		this.setPreferredSize(new Dimension(640, 480));
		this.pack();

		this.textField.requestFocusInWindow();

		this.clientThread = new ClientThread(this, "localhost", 8080);
		new Thread(this.clientThread).start();
	}

	public void appendServerText(String text)
	{
		this.appendServerText(text, null);
	}

	public void appendServerText(String text, Color color)
	{
		Document doc = this.textArea.getDocument();
		StyleConstants.setForeground(this.textArea.getStyle("color"), color != null ? color : Color.BLACK);

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

	private void handleExitEvent()
	{
		System.exit(0);
	}

	private void handleAboutEvent()
	{
		JOptionPane.showMessageDialog(this, "About");
	}

	protected void handleSendEvent()
	{
		// disable textField and grab input
		String input = this.textField.getText();

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

	private JMenuBar layoutMenuBar()
	{
		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Client.this.handleAboutEvent();
			}
		});

		fileMenu.add(about);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Client.this.handleExitEvent();
			}
		});
		fileMenu.add(exit);

		menu.add(fileMenu);

		return menu;
	}

	private JPanel layoutConnectorInterface()
	{
		JPanel panel = new JPanel();

		panel.add(new JLabel("Connect?"));
		panel.add(new JButton("Connect"));

		return panel;
	}

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

	private class SendEventListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Client.this.handleSendEvent();
		}
	}

	/**
	 * @param args
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
			@Override
			public void run()
			{
				new Client().setVisible(true);
			}
		});
	}
}
