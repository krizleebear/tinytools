package powernap;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main
{
    public static boolean IS_MAC = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    public static int MENU_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    public static String APP_NAME = "PowerNap";

	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JFrame f = new NapFrame(APP_NAME);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		f.setSize(256, 276);
		f.setResizable(false);
		f.setVisible(true);
	}
}
