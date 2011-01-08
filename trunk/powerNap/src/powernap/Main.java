package powernap;

import javax.swing.UIManager;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		PowerNap.getInstance();
		PowerNap.getInstance().init();
	}

	public static boolean isMac()
	{
		return System.getProperty("os.name").toLowerCase().startsWith("mac os x");
	}
	
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
}
