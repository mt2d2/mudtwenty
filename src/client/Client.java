package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame
{
	private static final long	serialVersionUID	= 1L;

	private JTextArea			textArea;
	private JTextField			textField;

	public Client()
	{
		this.setTitle("mudtwenty");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(this.layoutInterface());
		this.setJMenuBar(this.layoutMenuBar());
		this.setPreferredSize(new Dimension(640, 480));
		this.pack();
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

	private void handleExitEvent()
	{
		System.exit(0);

	}

	private void handleAboutEvent()
	{
		JOptionPane.showMessageDialog(this, "About");
	}

	private JPanel layoutInterface()
	{
		JPanel panel = new JPanel(new BorderLayout());

		// setup instance components
		this.textArea = new JTextArea();
		this.textField = new JTextField();
		this.textArea.setEditable(false);

		// text area
		panel.add(this.textArea, BorderLayout.CENTER);

		// entry panel
		JPanel entryPanel = new JPanel(new BorderLayout());
		entryPanel.add(this.textField, BorderLayout.CENTER);
		entryPanel.add(new JButton("Send"), BorderLayout.EAST);
		panel.add(entryPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				new Client().setVisible(true);
			}
		});
	}
}
