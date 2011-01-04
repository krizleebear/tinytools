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
		gui.setRunning(false);
		
		standbyComputer();
	}

	private void standbyComputer()
	{
		if(Main.IS_MAC)
		{
			osx.standby();
		}
		else
		{
			new Exception("not implemented for platforms other than OSX right now.").printStackTrace();
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
