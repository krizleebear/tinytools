package de.chle.jgantt;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import net.iharder.base64.Base64;

public class Layout
{
	private static String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "Oktober", "November", "December"};
	private Date startDate, endDate;
	private int width, height;
	private StringBuffer sb = new StringBuffer(2048);
	private static long dayMillis = 24*60*60*1000;
	
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
		Calendar start = Calendar.getInstance(Configuration.getInstance().getLocale());
		start.clear();
		start.setTime(startDate);
		int startYear = start.get(Calendar.YEAR);
		int startWeek = start.get(Calendar.WEEK_OF_YEAR);
		
		Calendar end = Calendar.getInstance(Configuration.getInstance().getLocale());
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
	
	private void addWeeks(double widthFactor)
	{
		/* count number of total weeks to show */
		int totalWeeks = getWeeksBetween(startDate, endDate);
		int weekWidth = (int) ((double) width / (double) totalWeeks);
		
		Calendar cal = Calendar.getInstance(Configuration.getInstance().getLocale());
		cal.clear();
		cal.setTime(startDate);
		
		//check if startDate is first day of week
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek()-dayOfWeek); //go back to first day of week
		
		int lastMonth = cal.get(Calendar.MONTH);
		boolean yearAlreadyPrinted = false;
		
		for(int i=0; i<totalWeeks; i++)
		{
			if(cal.getTimeInMillis() > endDate.getTime()) //small workaround to prevent more complex handling
				return;
			
			int currentYear = cal.get(Calendar.YEAR);
			int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
			int left = (int) ((double)(cal.getTime().getTime() - startDate.getTime()) * widthFactor);
			
			if(currentWeek==1 || (!yearAlreadyPrinted ))
			{
				addYear(cal, widthFactor);
				yearAlreadyPrinted = true;
			}
			
			int currentMonth = cal.get(Calendar.MONTH);
			if(currentMonth != lastMonth)
			{
				addMonth(cal, widthFactor);
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
	
	private void addMonth(Calendar cal, double widthFactor)
	{
		Calendar monthCal = (Calendar) cal.clone();
		monthCal.set(Calendar.DAY_OF_MONTH, 1); //use 1st of month to display
		
		int left = (int) ((double)(monthCal.getTime().getTime() - startDate.getTime()) * widthFactor);
		String month = months[monthCal.get(Calendar.MONTH)];
		appendLine("<div class='month' style='left:"+left+"px'>"+month+"</div>");
	}
	
	private int yearZIndex = 6; //newer year label should overlap the old year (if startDate is very near to beginning of next year)
	private void addYear(Calendar cal, double widthFactor)
	{
		Calendar yearCal = (Calendar) cal.clone();
		int left = 0;
		if(yearCal.get(Calendar.WEEK_OF_YEAR) == 1)
		{
			yearCal.set(Calendar.DAY_OF_YEAR, 1); //use January 1st to display year label 
			left = (int) ((double)(yearCal.getTime().getTime() - startDate.getTime()) * widthFactor);
		}
		
		appendLine("<div class='year' style='left:"+left+"px; z-index: "+ yearZIndex++ +"'>"+yearCal.get(Calendar.YEAR)+"</div>");
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
	
	/**
	 * not threadsafe!
	 * @param tasks
	 */
	public void layout(List<Task> tasks, List<Milestone> milestones)
	{
		long viewMillis = endDate.getTime() - startDate.getTime();
		double widthFactor = ((double)width / (double)viewMillis);
		
		appendLine("<html>");
		
		/* HTML header, incl. CSS style sheets */
		appendLine("<head>");
		appendStyleSheets();
		appendLine("</head>");
		
		/* HTML body */
		appendLine("<body style='position: absolute; top:20px; left:20px; margin: 40px; font-family: Arial; font-size: 10px'>");
		
		/* years, months and calendar week labels */
		addWeeks(widthFactor);
		
		/* current date indicator line */
		Date today = new Date();
		int todayleft = (int) ((double)(today.getTime() - startDate.getTime()) * widthFactor);
		appendLine("<div class='today' style='left: "+todayleft+"px'>&nbsp;</div>");
		
		/* tasks */
		addTasks(tasks, widthFactor);
		
		/* milestones */
		addMilestones(milestones, widthFactor);
		
		appendLine("</body>");
		appendLine("</html>");
	}

	private void appendStyleSheets()
	{
		appendLine("<style type='text/css'>");
		appendLine(".task { height: 15px; position: absolute; background-color:lightgray; text-align:left; vertical-align:middle; ; white-space:nowrap; z-index:9;}");
		appendLine(".done { height: 15px; position: absolute; background-color:#FF99FF; z-index:10; }");
		appendLine(".calendarweekEven { height: "+height+"px; background-color:#EEEEEE; position: absolute; z-index:5; text-align:center; border-top:1px solid black;}");
		appendLine(".calendarweekOdd  { height: "+height+"px; background-color:#FFFFFF; position: absolute; z-index:5; text-align:center; border-top:1px solid black;}");
		appendLine(".month { top: -15px; height: 15px; background-color:white; position: absolute; z-index:7; text-align:left; padding-left:2px; vertical-align:top; font-size: 10px;}");
		appendLine(".year  { top: -30px; height: 17px; background-color:white; position: absolute; z-index:6; text-align:left; padding-left:2px; vertical-align:top; font-size: 12px; font-weight:bold}");
		appendLine(".today  { top: 0px; height: "+height+"px; width: 1px; background-color:none; position: absolute; z-index:100; text-align:left; padding-left:2px; vertical-align:text-bottom; font-size: 12px; border-left:1px solid red; white-space:nowrap;}");

		/* embed Milestone image */
		try
		{
			appendImageCssClass("milestone", new File("./karo.png"), "bottom left no-repeat", "position: absolute; z-index:20; width:20px; height:40px; text-align:left; text-indent:8px; vertical-align:bottom; top: -26px; z-index:105; white-space:nowrap;");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		appendLine("</style>");
	}

	private void addMilestones(List<Milestone> milestones, double widthFactor)
	{
		if(milestones==null)
			return;
		
		for(Milestone milestone : milestones)
		{
			int left = (int) ((double)(milestone.getDueDate().getTime() - startDate.getTime()) * widthFactor);
			appendLine("<div class='milestone' style='left: "+left+"px;'>"+milestone.getName()+"</div>");
		}
	}

	private void addTasks(List<Task> tasks, double widthFactor)
	{
		if(tasks==null)
			return;
		
		int taskCount = 0;
		for(Task task : tasks)
		{
			StringBuffer description = new StringBuffer();
			description.append(task.getPercentDone());
			description.append("% &#13; ");
			appendHtmlEscaped(task.getDescription(), description);

			int taskWidth = (int) (task.getDurationMillis() * (dayMillis/Configuration.getInstance().getWorkMillisPerDay()) * widthFactor) ;
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
	}
	
	private void appendImageCssClass(String className, File imageFile, String backgroundAlignment, String otherDefinitions) throws IOException
	{
		BufferedImage img = ImageIO.read(imageFile);
		String encodedImg = base64EncodeImage(img);
		
		sb.append(".").append(className).append(" { background: url(data:image/png;base64,");
		sb.append(encodedImg);
		sb.append(") ").append(backgroundAlignment).append("; ");
		if(otherDefinitions!=null)
			sb.append(otherDefinitions);
		appendLine(" }");
	}
	
	private String base64EncodeImage(BufferedImage img) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", baos);
		return Base64.encodeBytes(baos.toByteArray());
	}

	public String getHTML()
	{
		return sb.toString();
	}

}
