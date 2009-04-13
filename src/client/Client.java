package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextArea textArea;
	private JTextField textField;

	public Client() {
		this.setTitle("mudtwenty");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(this.layoutInterface());
		this.setJMenuBar(this.layoutMenuBar());
		this.setPreferredSize(new Dimension(640, 480));
		this.pack();

		this.textField.requestFocusInWindow();
	}

	private void handleExitEvent() {
		System.exit(0);
	}

	private void handleAboutEvent() {
		JOptionPane.showMessageDialog(this, "About");
	}

	protected void handleSendEvent() {
		// disable textField and grab input
		String input = this.textField.getText();
		if (input.length() != 0) {
			this.textField.setEnabled(false);
			this.textArea.append(input.trim() + "\n");
		}

		// restore text to blank
		this.textField.setText("");
		this.textField.setEnabled(true);
		this.textField.requestFocusInWindow();
	}

	private JMenuBar layoutMenuBar() {
		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.this.handleAboutEvent();
			}
		});

		fileMenu.add(about);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.this.handleExitEvent();
			}
		});
		fileMenu.add(exit);

		menu.add(fileMenu);

		return menu;
	}

	private JPanel layoutInterface() {
		JPanel panel = new JPanel(new BorderLayout());
		ActionListener sendEventListener = new SendEventListener();

		// setup instance components
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textField = new JTextField();
		this.textField.registerKeyboardAction(sendEventListener, KeyStroke
				.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
				JComponent.WHEN_FOCUSED);

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

	private class SendEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Client.this.handleSendEvent();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Client().setVisible(true);
			}
		});
	}
}
