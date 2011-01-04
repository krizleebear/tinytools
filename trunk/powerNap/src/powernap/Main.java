package powernap;

import java.awt.Toolkit;

import javax.swing.UIManager;

public class Main
{
    public static boolean IS_MAC = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    public static int MENU_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    
    
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		PowerNap.getInstance();
	}
}
