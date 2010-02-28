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

	
	
}
