package powernap;

import javax.swing.UIManager;

public class Main
{
    public static boolean IS_MAC = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		PowerNap.getInstance();
	}
}
