package powernap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Countdown
{
	public interface CountdownListener
	{
		void countdownTriggered();
		void countdownChanged(int percent, long remainingMillis);
		void countdownStopped();
	}
	
	private CountdownThread countdownThread;
	private EventDispatcher dispatcher = new EventDispatcher();

	public Countdown()
	{
		this.countdownThread = new CountdownThread();
	}
	
	public void startCountdown(int hours, int minutes, int seconds)
	{
		countdownThread.startCountdown(hours, minutes, seconds);
	}

	public void stop()
	{
		countdownThread.stop();
	}

	public void addCountdownListener(CountdownListener listener)
	{
		dispatcher.addCountdownListener(listener);
	}
	
	public void removeCountdownListener(CountdownListener listener)
	{
		dispatcher.removeCountdownListener(listener);
	}
	
	class EventDispatcher implements CountdownListener
	{
		private Set listeners = new HashSet();
		
		public void addCountdownListener(CountdownListener listener)
		{
			listeners.add(listener);
		}
		
		public void removeCountdownListener(CountdownListener listener)
		{
			listeners.remove(listener);
		}

		public void countdownChanged(int percent, long remainingMillis)
		{
			Iterator it = listeners.iterator();
			while(it.hasNext())
			{
				CountdownListener listener = (CountdownListener) it.next();
				listener.countdownChanged(percent, remainingMillis);
			}
		}

		public void countdownStopped()
		{
			Iterator it = listeners.iterator();
			while(it.hasNext())
			{
				CountdownListener listener = (CountdownListener) it.next();
				listener.countdownStopped();
			}
		}

		public void countdownTriggered()
		{
			Iterator it = listeners.iterator();
			while(it.hasNext())
			{
				CountdownListener listener = (CountdownListener) it.next();
				listener.countdownTriggered();
			}
		}
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
			while (running)
			{
				long remainingMillis = countdownEndMillis - System.currentTimeMillis();
				
				if (remainingMillis <= 0)
				{
					dispatcher.countdownTriggered();
					break;
				}
				else
				{
					double percent = (((double)(totalMillis-remainingMillis))/(double)totalMillis) * 100.0;
					dispatcher.countdownChanged((int)percent, remainingMillis);
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

		public void stop()
		{
			running = false;
			dispatcher.countdownStopped();
		}
	}
}
