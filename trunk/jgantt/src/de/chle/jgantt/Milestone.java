package de.chle.jgantt;

import java.text.ParseException;
import java.util.Date;

public class Milestone
{
	private String name;
	private Date dueDate;
	
	/**
	 * Using dateformat defined in Configuration (default: yyyy-MM-dd)
	 * @param name
	 * @param dueDateString
	 * @throws ParseException 
	 */
	public Milestone(String name, String dueDateString) throws ParseException
	{
		super();
		this.name = name;
		this.dueDate = Configuration.getInstance().getDateformat().parse(dueDateString);
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(Date dueDate)
	{
		this.dueDate = dueDate;
	}

	public Milestone(String name, Date dueDate)
	{
		super();
		this.name = name;
		this.dueDate = dueDate;
	}
	
	
}
