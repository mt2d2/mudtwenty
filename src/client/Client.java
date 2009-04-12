package client;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client extends JFrame
{
	private static final long	serialVersionUID	= 1L;

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
