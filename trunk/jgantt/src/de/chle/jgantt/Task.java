package de.chle.jgantt;

import java.util.Date;

public class Task
{
	String name;
	String description;
	int durationHours;
	Date dueDate;
	int percentDone;
	
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

	public Object getDescription()
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
	
}
