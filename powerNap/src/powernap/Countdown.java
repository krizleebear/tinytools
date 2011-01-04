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
		void countdownChanged(int percent, long remainingMillis);
	}

	public void setCountdownListener(CountdownListener listener)
	{
		this.listener = listener;
	}

	class CountdownThread implements Runnable
	{
		private boolean running = false;
		private Thread thread;
		private long countdownEndMillis = Long.MAX_VALUE;
		private long startMillis;
		private long totalMillis;
		
		private void startThread()
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
					double percent = (((double)(totalMillis-remainingMillis))/(double)totalMillis) * 100.0;
					listener.countdownChanged((int)percent, remainingMillis);
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

		public void startCountdown(int hours, int minutes, int seconds)
		{
			startMillis = System.currentTimeMillis();
			
			long durationMillis = 0;
			durationMillis += hours * 3600 * 1000;
			durationMillis += minutes * 60 * 1000;
			durationMillis += seconds * 1000;

			countdownEndMillis = startMillis + durationMillis;
			totalMillis = countdownEndMillis - startMillis;
			
			startThread();
		}
	}

	public void startCountdown(int hours, int minutes, int seconds)
	{
		countdownThread.startCountdown(hours, minutes, seconds);
	}
}
