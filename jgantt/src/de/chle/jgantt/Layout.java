package de.chle.jgantt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Layout
{
	Date startDate, endDate;
	int width, height;
	StringBuffer sb = new StringBuffer(2048);
	
	//default resolution: days

	public Layout(Date startDate, Date endDate, int width, int height)
	{
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.width = width;
		this.height = height;
	}
	
	private void appendLine(String line)
	{
		sb.append(line);
		sb.append('\n');
	}
	
	private int weeksBetween(Date startDate, Date endDate)
	{
		Calendar start = Calendar.getInstance();
		start.setMinimalDaysInFirstWeek(7);
		start.clear();
		start.setTime(startDate);
		int startYear = start.get(Calendar.YEAR);
		int startWeek = start.get(Calendar.WEEK_OF_YEAR);
		
		Calendar end = Calendar.getInstance();
		end.setMinimalDaysInFirstWeek(1);
		end.clear();
		end.setTime(endDate);
		int endYear = end.get(Calendar.YEAR);
		int endWeek = end.get(Calendar.WEEK_OF_YEAR);
		
		// very simple if in same year:
		if(startYear==endYear)
		{
			return endWeek - startWeek;
		}
		
		int numWeeks = 1; //one is minimum
		
		/* first year */
		numWeeks += (start.getActualMaximum(Calendar.WEEK_OF_YEAR) - startWeek);

		/* years between */
		for(int i=startYear+1; i<endYear; i++)
		{
			Calendar currentYear = Calendar.getInstance();
			
			currentYear.set(Calendar.YEAR, i);
			numWeeks += currentYear.getActualMaximum(Calendar.WEEK_OF_YEAR) + 1; //starting at 0
		}
		
		/* end year */
		numWeeks += endWeek;
		
		return numWeeks;
	}
	
	private void insertCalendarWeeks(double widthFactor)
	{
		/* count number of total weeks to show */
		int totalWeeks = weeksBetween(startDate, endDate);
		int weekWidth = (int) ((double)width  / (double)totalWeeks);
		
		
		for(int i=0; i<totalWeeks; i++)
		{
			sb.append("<div class='");
			if(i%2==0)
			{
				sb.append("calendarweekEven");
			}
			else
			{
				sb.append("calendarweekOdd");
			}
			sb.append("' style='width:");
			sb.append(weekWidth);
			sb.append("px; left:");
			sb.append(weekWidth*i);
			appendLine("px'>&nbsp;</div>");
		}
	}
	
	public void layout(List<Task> tasks)
	{
		long viewMillis = endDate.getTime() - startDate.getTime();
		
		double widthFactor = (double)width / (double)viewMillis;
		
		appendLine("<html>");
		appendLine("<head>");
		appendLine("<style type='text/css'>");
		appendLine(".task { height: 15px; position: absolute; background-color:lightgray; text-align:left; vertical-align:middle; ; white-space:nowrap; z-index:9;}");
		appendLine(".done { height: 15px; position: absolute; background-color:#FF99FF; z-index:10; }");
		appendLine(".calendarweekEven { height: "+height+"px; background-color:#EEEEEE; position: absolute; z-index:5;}");
		appendLine(".calendarweekOdd  { height: "+height+"px; background-color:#FFFFFF; position: absolute; z-index:5;}");
		
		appendLine("</style>");
		appendLine("</head>");
		appendLine("<body style='position: absolute; top:20px; left:20px; margin: 40px; font-family: Arial; font-size: 10px'>");
		
		//TODO html entities in description and name!
		
		/* time line */
		
		
		/* background :: calender weeks */
		insertCalendarWeeks(widthFactor);
		
		/* tasks */
		
		int taskCount = 2;
		for(Task task : tasks)
		{
//			System.out.println(task);
			
			int taskWidth = (int) (task.getDurationMillis() * widthFactor) ;
			int doneWidth = (int) ((double)taskWidth * (double)task.percentDone/100d);
			int left = (int) ((double)(task.getBeginMillis() - startDate.getTime()) * widthFactor);
			
			int top = taskCount++ * 30;
			if(task.getPercentDone() > 0)
			{
				sb.append("<div title='"+task.getPercentDone()+"%' class='done' style=' top:"+top+"px; left:"+left+"px; width:"+doneWidth+"px;'>&nbsp;</div>");
			}
			sb.append("<div ");
			sb.append("title='");
			sb.append(task.getDescription());
			sb.append("' class='task' ");
			sb.append("style='top:");
			sb.append(top);
			sb.append("px;left:");
			sb.append(left + doneWidth);
			sb.append("px; width:");
			sb.append(taskWidth - doneWidth);
			sb.append("px; text-indent:");
			sb.append(taskWidth - doneWidth + 5);
			sb.append("px'>");
			sb.append(task.getName());
			appendLine("</div>");
		}
		
		appendLine("</body>");
		appendLine("</html>");
		
		System.out.println(sb.toString());
	}
	

}
