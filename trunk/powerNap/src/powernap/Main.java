package powernap;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main
{
	public static void main(String[] args) throws Exception
	{
//		com.apple.eawt...

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		final JFrame f = new NapFrame("PowerNap");
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		f.setSize(256, 276);
//		f.setResizable(false);
		f.setVisible(true);
		
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0)
			{
				System.out.println("closing");
				//f.setVisible(false);
			}
		});
	}
}
