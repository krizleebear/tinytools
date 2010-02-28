package de.chle.jgantt;

import java.text.ParseException;
import java.util.Date;

public class Task
{
	private String name;
	private String description;
	private int durationHours;
	private Date dueDate;
	private int percentDone;
	
	/**
	 * Define date as string 
	 * Using dateformat defined in Configuration (default: yyyy-MM-dd)
	 * @param name
	 * @param description
	 * @param durationHours
	 * @param dueDateString
	 * @param percentDone
	 * @throws ParseException 
	 */
	public Task(String name, String description, int durationHours, String dueDateString, int percentDone) throws ParseException
	{
		this(name, description, durationHours, Configuration.getInstance().getDateformat().parse(dueDateString), percentDone);
	}
	
	public Task(String name, String description, int durationHours, Date dueDate, int percentDone)
	{
		super();
		this.name = name;
		this.description = description;
		this.durationHours = durationHours;
		this.dueDate = dueDate;
		this.percentDone = percentDone;
	}

	public double getDurationDays()
	{
		return durationHours / 24;
	}
	
	public Date getBeginDate()
	{
		return new Date( dueDate.getTime() - getDurationMillis() );
	}
	
	public long getBeginMillis()
	{
		return dueDate.getTime() - getDurationMillis();
	}
	
	public long getDurationMillis()
	{
		return durationHours * 60*60*1000;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public int getPercentDone()
	{
		return percentDone;
	}
	public double getDoneFactor()
	{
		return (double)percentDone / 100;
	}
	
	public String toString()
	{
		return "Task " + name + " - Begin: " + getBeginDate() + " - Due to: " + dueDate;
	}
	
}
