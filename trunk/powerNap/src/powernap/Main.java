package powernap;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main
{
    public static boolean IS_MAC = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    public static int MENU_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    public static String APP_NAME = "PowerNap";

	public static void main(String[] args) throws Exception
	{
//		com.apple.eawt...
//com.apple.eawt.Application a;
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		final JFrame f = new NapFrame(APP_NAME);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		f.setSize(256, 276);
		f.setResizable(false);
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
