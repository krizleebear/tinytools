package powernap;

public class Countdown
{
	private CountdownListener listener;
	private long countdownEndMillis = Long.MAX_VALUE;
	private Thread countdownThread;

	public Countdown()
	{
		this.countdownThread = new Thread(new CountdownThread());
	}

	public interface CountdownListener
	{
		void countdownElapsed();
	}

	public void setCountdownListener(CountdownListener listener)
	{
		this.listener = listener;
	}

	private class CountdownThread implements Runnable
	{
		public void run()
		{
			for (;;)
			{
				if (System.currentTimeMillis() > countdownEndMillis)
				{
					if (listener != null)
					{
						listener.countdownElapsed();
					}
					break;
				}
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

	public void startCountdown(int hours, int minutes, int seconds)
	{
		long nowMillis = System.currentTimeMillis();
		
		long durationMillis = 0;
		durationMillis += hours * 3600 * 1000;
		durationMillis += minutes * 60 * 1000;
		durationMillis += seconds * 1000;
		
		countdownEndMillis = nowMillis + durationMillis;

		countdownThread.start();
	}
}
