package powernap;

public class Countdown
{
	private CountdownListener listener;
	private CountdownThread countdownThread;

	public Countdown()
	{
		this.countdownThread = new CountdownThread();
	}

	public interface CountdownListener
	{
		void countdownTriggered();
		void remainingMillis(long remainingMillis);
	}

	public void setCountdownListener(CountdownListener listener)
	{
		this.listener = listener;
	}

	private class CountdownThread implements Runnable
	{
		private boolean running = false;
		private Thread thread;
		private long countdownEndMillis = Long.MAX_VALUE;
		
		public void start()
		{
			if(!running)
			{
				thread = new Thread(this);
				thread.start();
			}
		}
		
		public void run()
		{
			running = true;
			for (;;)
			{
				long remainingMillis = countdownEndMillis - System.currentTimeMillis();
				
				if (remainingMillis <= 0)
				{
					if (listener != null)
					{
						listener.countdownTriggered();
					}
					break;
				}
				else
				{
					listener.remainingMillis(remainingMillis);
				}
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
				}
			}
			running = false;
		}

		public void setEndMillis(long countdownEndMillis)
		{
			this.countdownEndMillis = countdownEndMillis;
		}
	}

	public void startCountdown(int hours, int minutes, int seconds)
	{
		long nowMillis = System.currentTimeMillis();
		
		long durationMillis = 0;
		durationMillis += hours * 3600 * 1000;
		durationMillis += minutes * 60 * 1000;
		durationMillis += seconds * 1000;

		long countdownEndMillis = nowMillis + durationMillis;
		
		countdownThread.setEndMillis(countdownEndMillis);
		countdownThread.start();
	}
}
