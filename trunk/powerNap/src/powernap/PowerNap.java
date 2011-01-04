package powernap;

import javax.swing.JFrame;

import powernap.Countdown.CountdownListener;

public class PowerNap implements CountdownListener
{
	public static String APP_NAME = "PowerNap";
	
	private static PowerNap instance = new PowerNap();
	
	private Countdown countdown = new Countdown();
	private OSXSupport osx = new OSXSupport(); 
	private NapFrame gui = null;
	
	public static PowerNap getInstance()
	{
		return instance;
	}
	
	private PowerNap()
	{
		osx.initialize();
		
		countdown.setCountdownListener(this);
		
		gui = new NapFrame(APP_NAME);
		gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		gui.setSize(256, 276);
		gui.setResizable(false);
		gui.setVisible(true);
	}
	
	public void startCountdown(int hours, int minutes)
	{
		countdown.startCountdown(hours, minutes, 0);
	}

	public void countdownChanged(int percent, long remainingMillis)
	{
		System.out.println(remainingMillis/1000 + " seconds remaining");
		osx.setProgress(percent);
	}
	
	public void countdownTriggered()
	{
		if(Main.IS_MAC)
		{
			try
			{
				System.out.println("sending this mac to sleep...");
				String[] cmd = { "osascript", "-e",	"tell application \"System Events\" to sleep" };

				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				int returnCode = p.exitValue();
				System.out.println("return code: " + returnCode);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void quit()
	{
		System.exit(0);		
	}
	
	public void show()
	{
		gui.setVisible(true);
	}
}
