package de.chle.jgantt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Configuration
{
	private static Configuration instance = new Configuration();

	/* settings */
	private Locale locale = Locale.GERMANY;
	private DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	private long workMillisPerDay = 8*60*60*1000; //8 working hours a day
	private String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "Oktober", "November", "December"};

	private Configuration()
	{
		
	}
	
	public static Configuration getInstance()
	{
		return instance;
	}

	/* getters/setters */
	
	/**
	 * Default: Locale.GERMANY
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * Default: Locale.GERMANY
	 * @param locale
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Default: yyyy-MM-dd
	 * @return
	 */
	public DateFormat getDateformat()
	{
		return dateformat;
	}

	/**
	 * Default: yyyy-MM-dd
	 * @param dateformat
	 */
	public void setDateformat(DateFormat dateformat)
	{
		this.dateformat = dateformat;
	}

	public long getWorkMillisPerDay()
	{
		return workMillisPerDay;
	}
	
	/**
	 * default: 8 working hours per day
	 * @param hours
	 */
	public void setWorkingHoursPerDay(int hours)
	{
		workMillisPerDay = hours * 60*60*1000;
	}

	public String[] getMonths()
	{
		return months;
	}

	/**
	 * Set the array defining the month's names, e.g.:
	 * months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "Oktober", "November", "December"};  
	 * @param months
	 */
	public void setMonths(String[] months)
	{
		this.months = months;
	}
	
}
