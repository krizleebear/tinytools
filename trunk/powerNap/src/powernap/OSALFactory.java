package powernap;

public class OSALFactory
{
	public static IOSAL getOSAL()
	{
		if(Main.isMac())
		{
			return new OSXSupport();
		}
		else if(Main.isWindows())
		{
			return new WindowsSupport();
		}
		else
		{
			System.err.println("Platform seems not to be supported. Sorry.");
			return null;
		}
	}
}
