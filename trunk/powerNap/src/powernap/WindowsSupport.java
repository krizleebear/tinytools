package powernap;

import java.io.IOException;

import javax.swing.JFrame;

public class WindowsSupport implements IOSAL
{
	public void initialize()
	{
		PowerNap.getInstance().setWindowCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setProgress(int percent)
	{
	}

	public void standby()
	{
		try
		{
			Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void countdownTriggered()
	{
	}

	public void stopCountdown()
	{
	}

	public void startCountdown()
	{
	}
}
