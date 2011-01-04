package powernap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

public class OSXSupport implements ApplicationListener
{
	private Application app = null;
	private BufferedImage originalIcon;
	
	public OSXSupport()
	{
	}
	
	public void initialize()
	{
		if(Main.IS_MAC)
		{
			app = new DefaultApplication();
			app.addApplicationListener(this);
			originalIcon = app.getApplicationIconImage();
		}
	}
	
	int lastRenderedPercent = Integer.MIN_VALUE;
	public void setProgress(int percent)
	{
		int diff = Math.abs(percent-lastRenderedPercent);
		if(diff < 1)
		{
			//don't render as the difference to the last rendering is too small
			return;
		}
		lastRenderedPercent = percent;

		int iconWidth = originalIcon.getWidth();
		int iconHeight = originalIcon.getHeight();
		
		BufferedImage newIcon = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D graphics = (Graphics2D) newIcon.getGraphics();

	    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    graphics.drawImage(originalIcon, 0, 0, null);
	    
	    int progressBarHeight = (int) (((float)iconHeight)/7.0);
	    int progressBarWidth = (int) (((float)iconWidth)/100.0 * percent);
	    
	    //draw white background fill
	    graphics.setColor(Color.WHITE);
	    graphics.fillRect(0, iconHeight-progressBarHeight, iconWidth, progressBarHeight);

	    //draw status bar
	    graphics.setColor(Color.decode("#0066CC"));
	    graphics.fillRect(0, iconHeight-progressBarHeight, progressBarWidth, progressBarHeight);

	    graphics.dispose();

	    app.setApplicationIconImage(newIcon);
	}

	public void handleQuit(ApplicationEvent event)
	{
		PowerNap.getInstance().quit();
	}

	public void handleReOpenApplication(ApplicationEvent event)
	{
		PowerNap.getInstance().show();
	}
	
	public void handleAbout(ApplicationEvent event)
	{
	}

	public void handleOpenApplication(ApplicationEvent event)
	{
	}

	public void handleOpenFile(ApplicationEvent event)
	{
	}

	public void handlePreferences(ApplicationEvent event)
	{
	}

	public void handlePrintFile(ApplicationEvent event)
	{
	}
}
