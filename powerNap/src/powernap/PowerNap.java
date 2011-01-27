package powernap;

import powernap.Countdown.CountdownListener;

public class PowerNap implements CountdownListener
{
	public static String APP_NAME = "PowerNap";
	
	private static PowerNap instance = new PowerNap();
	
	private Countdown countdown = new Countdown();
	private IOSAL osal;
	private NapFrame gui = null;
	private Settings settings = Settings.getInstance();
	
	public static PowerNap getInstance()
	{
		return instance;
	}
	
	private PowerNap()
	{
		Settings settings = Settings.getInstance();
		
		addCountdownListener(this);
		
		gui = new NapFrame(APP_NAME);
		gui.setSize(256, 276);
		gui.setResizable(false);
		gui.setVisible(true);
		gui.setHoursAndMinutes(settings.getHours(), settings.getMinutes());
	}
	
	public void init()
	{
		osal = OSALFactory.getOSAL();
		osal.initialize();
	}
	
	public void setWindowCloseOperation(int jframeCloseOperation)
	{
		gui.setDefaultCloseOperation(jframeCloseOperation);
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
		
		osal.startCountdown();
	}
	
	public void stopCountdown()
	{
		countdown.stop();
		osal.stopCountdown();
	}

	public void countdownChanged(int percent, long remainingMillis)
	{
//		System.out.println(remainingMillis/1000 + " seconds remaining");
		osal.setProgress(percent);
		gui.setProgress(percent, remainingMillis);
	}
	
	public void countdownTriggered()
	{
		gui.countdownTriggered();
		gui.setHoursAndMinutes(settings.getHours(), settings.getMinutes());
		osal.countdownTriggered();
		
		standbyComputer();
	}
	
	public void countdownStopped()
	{
		gui.setHoursAndMinutes(settings.getHours(), settings.getMinutes());
	}

	private void standbyComputer()
	{
		osal.standby();
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
