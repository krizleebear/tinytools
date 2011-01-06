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
	private Settings settings = Settings.getInstance();
	
	public static PowerNap getInstance()
	{
		return instance;
	}
	
	private PowerNap()
	{
		Settings settings = Settings.getInstance();
		
		osx.initialize();
		
		addCountdownListener(this);
		
		gui = new NapFrame(APP_NAME);
		gui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		gui.setSize(256, 276);
		gui.setResizable(false);
		gui.setVisible(true);
		
		gui.setHoursAndMinutes(settings.getHours(), settings.getMinutes());
	}
	
	public void addCountdownListener(CountdownListener listener)
	{
		countdown.addCountdownListener(listener);
	}
	
	public void startCountdown(int hours, int minutes)
	{
		Settings settings = Settings.getInstance();
		
		settings.setHours(hours);
		settings.setMinutes(minutes);
		settings.save();
		
		countdown.startCountdown(hours, minutes, 0);
		
		osx.startCountdown();
	}
	
	public void stopCountdown()
	{
		countdown.stop();
		osx.stopCountdown();
	}

	public void countdownChanged(int percent, long remainingMillis)
	{
//		System.out.println(remainingMillis/1000 + " seconds remaining");
		osx.setProgress(percent);
		gui.setProgress(percent, remainingMillis);
	}
	
	public void countdownTriggered()
	{
		gui.setRunning(false);
		osx.countdownTriggered();
		
		standbyComputer();
	}
	
	public void countdownStopped()
	{
		gui.setHoursAndMinutes(settings.getHours(), settings.getMinutes());
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
