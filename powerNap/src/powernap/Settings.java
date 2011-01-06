package powernap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
	File settingsFile = new File("powernap.properties"); 
	Properties props = new Properties();
	
	private int hours = 0;
	private int minutes = 0;
	
	private static Settings instance = new Settings();
	
	public static Settings getInstance()
	{
		return instance;
	}
	
	private Settings()
	{
		if(settingsFile.exists())
		{
			try
			{
				props.load(new FileReader(settingsFile));
			}
			catch (FileNotFoundException e)
			{
			}
			catch (IOException e)
			{
			}
		}
		
		String hourString = props.getProperty("hours", "0");
		String minuteString = props.getProperty("minutes", "0");
		try
		{
			setHours(Integer.parseInt(hourString));
			setMinutes(Integer.parseInt(minuteString));
		}
		catch(NumberFormatException ex)
		{
			setHours(0);
			setMinutes(0);
		}
	}
	
	public void save()
	{
		props.setProperty("hours", Integer.toString(getHours()));
		props.setProperty("minutes", Integer.toString(getMinutes()));
		
		try
		{
			props.store(new FileWriter(settingsFile), "");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setHours(int hours)
	{
		this.hours = hours;
	}

	public int getHours()
	{
		return hours;
	}

	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}

	public int getMinutes()
	{
		return minutes;
	}

	
}
