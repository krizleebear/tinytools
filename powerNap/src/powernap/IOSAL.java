package powernap;

public interface IOSAL
{
	public void initialize();
	public void standby();
	public void setProgress(int percent);
	public void countdownTriggered();
	public void stopCountdown();
	public void startCountdown();
}
