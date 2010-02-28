package de.chle.jgantt;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import java.util.Locale;

public class Layout
{
	private Locale locale = Locale.GERMANY;
	private static String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "Oktober", "November", "December"};
	private Date startDate, endDate;
	private int width, height;
	private StringBuffer sb = new StringBuffer(2048);
	private static long dayMillis = 24*60*60*1000;
	private long workMillisPerDay = 8*60*60*1000; //8 working hours a day
	
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
	
	private int getWeeksBetween(Date startDate, Date endDate)
	{
		Calendar start = Calendar.getInstance(locale);
//		start.setMinimalDaysInFirstWeek(1);
		start.clear();
		start.setTime(startDate);
		int startYear = start.get(Calendar.YEAR);
		int startWeek = start.get(Calendar.WEEK_OF_YEAR);
		
		Calendar end = Calendar.getInstance(locale);
//		end.setMinimalDaysInFirstWeek(1);
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
		int totalWeeks = getWeeksBetween(startDate, endDate);
		int weekWidth = (int) ((double)width  / (double)totalWeeks);
		
		Calendar cal = Calendar.getInstance(locale);
		cal.clear();
//		cal.setFirstDayOfWeek(Calendar.MONDAY);
//		cal.setMinimalDaysInFirstWeek(7);
		cal.setTime(startDate);
		int lastMonth = cal.get(Calendar.MONTH);
		boolean yearAlreadyPrinted = false;
		
		for(int i=0; i<totalWeeks; i++)
		{
			if(cal.getTimeInMillis()>endDate.getTime()) //small workaround to prevent more complex handling
				return;
			
			int currentYear = cal.get(Calendar.YEAR);
			int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
			
			int left = weekWidth * i;
			
			if(currentWeek==1 || (!yearAlreadyPrinted ))
			{
				addYear(left, cal);
				yearAlreadyPrinted = true;
			}
			
			int currentMonth = cal.get(Calendar.MONTH);
			if(currentMonth != lastMonth)
			{
				addMonth(left, cal);
				lastMonth = currentMonth;
			}
			
			sb.append("<div title='");
			sb.append(currentYear + "/" + currentWeek);
			sb.append("' class='");
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
			sb.append(left);
			sb.append("px'>");
			sb.append(currentWeek);
			appendLine("</div>");
			
			cal.add(Calendar.WEEK_OF_YEAR, 1); //goto next week
		}
	}
	
	private void addMonth(int leftPx, Calendar cal)
	{
		String month = months[cal.get(Calendar.MONTH)];
		appendLine("<div class='month' style='left:"+leftPx+"px'>"+month+"</div>");
	}
	private int yearZIndex = 6;
	private void addYear(int leftPx, Calendar date)
	{
		appendLine("<div class='year' style='left:"+leftPx+"px; z-index: '"+ yearZIndex++ +">"+date.get(Calendar.YEAR)+"</div>");
	}
	
	private String htmlEscape(String s)
	{
		StringBuffer sb = new StringBuffer();
		appendHtmlEscaped(s, sb);
		return sb.toString();
	}
	
	private void appendHtmlEscaped(String s, StringBuffer sb)
	{
		char[] chars = s.toCharArray();
		
		for(char c : chars)
		{
			if	(
					(c >= '!' && c <= ',') 
				||	(c >= ':' && c <= '@')
				||	(c >= '[' && c <= '^')
				||	(c == '`')
				||	(c > 'z')
				||  (c < '!')
				)
			{
				sb.append("&#").append((int)c).append(';');
			}
//			else if(c==' ')
//			{
//				sb.append('+');
//			}
			else
			{
				sb.append(c);
			}
		}
	}
	
	public void layout(List<Task> tasks)
	{
		long viewMillis = endDate.getTime() - startDate.getTime();
		double widthFactor = ((double)width / (double)viewMillis);
		
		appendLine("<html>");
		appendLine("<head>");
		appendLine("<style type='text/css'>");
		appendLine(".task { height: 15px; position: absolute; background-color:lightgray; text-align:left; vertical-align:middle; ; white-space:nowrap; z-index:9;}");
		appendLine(".done { height: 15px; position: absolute; background-color:#FF99FF; z-index:10; }");
		appendLine(".calendarweekEven { height: "+height+"px; background-color:#EEEEEE; position: absolute; z-index:5; text-align:center; border-top:1px solid black;}");
		appendLine(".calendarweekOdd  { height: "+height+"px; background-color:#FFFFFF; position: absolute; z-index:5; text-align:center; border-top:1px solid black;}");
		appendLine(".month { top: -15px; height: 15px; background-color:white; position: absolute; z-index:7; text-align:left; padding-left:2px; vertical-align:top; font-size: 10px;}");
		appendLine(".year  { top: -30px; height: 17px; background-color:white; position: absolute; z-index:6; text-align:left; padding-left:2px; vertical-align:top; font-size: 12px; font-weight:bold}");
		appendLine(".today  { top: 0px; height: "+height+"px; width: 1px; background-color:none; position: absolute; z-index:100; text-align:left; padding-left:2px; vertical-align:text-bottom; font-size: 12px; border-left:1px solid red; white-space:nowrap;}");
		
		appendLine("</style>");
		appendLine("</head>");
		appendLine("<body style='position: absolute; top:20px; left:20px; margin: 40px; font-family: Arial; font-size: 10px'>");
		
		insertCalendarWeeks(widthFactor);
		
		Date today = new Date();
		int todayleft = (int) ((double)(today.getTime() - startDate.getTime()) * widthFactor);
		appendLine("<div class='today' style='left: "+todayleft+"px'>&nbsp;</div>");
		
		/* tasks */
		int taskCount = 0;
		for(Task task : tasks)
		{
			StringBuffer description = new StringBuffer();
			description.append(task.getPercentDone());
			appendHtmlEscaped("% \n", description);
			appendHtmlEscaped(task.getDescription(), description);

			//			System.out.println(task);
			
			int taskWidth = (int) (task.getDurationMillis() * (dayMillis/workMillisPerDay) * widthFactor) ;
			int doneWidth = (int) ((double)taskWidth * (double)task.getPercentDone()/100d);
			int left = (int) ((double)(task.getBeginMillis() - startDate.getTime()) * widthFactor);
			
			int top = taskCount++ * 30 + 25;
			if(task.getPercentDone() > 0)
			{
				sb.append("<div title='");
				sb.append(description);
				sb.append("' class='done' style=' top:"+top+"px; left:"+left+"px; width:"+doneWidth+"px;'>&nbsp;</div>");
			}
			sb.append("<div ");
			sb.append("title='");
			sb.append(description);
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
			appendHtmlEscaped(task.getName(), sb);
			appendLine("</div>");
		}
		
		appendLine("</body>");
		appendLine("</html>");
		
		System.out.println(sb.toString());
	}
	
	public String getHTML()
	{
		return sb.toString();
	}
	
	/**
	 * default: 8
	 * @param hours
	 */
	public void setWorkingHoursPerDay(int hours)
	{
		workMillisPerDay = hours*60*60*1000;
	}
	

}
