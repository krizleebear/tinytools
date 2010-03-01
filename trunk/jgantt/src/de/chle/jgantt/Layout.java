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
	private Date startDate, endDate;
	private int width, height;
	private StringBuffer sb = new StringBuffer(2048);
	private static long dayMillis = 24*60*60*1000;
	long viewMillis = -1;
	double widthFactor = 0;
	
	public Layout(Date startDate, Date endDate, int width, int height)
	{
		super();
		viewMillis = endDate.getTime() - startDate.getTime();
		widthFactor = ((double)width / (double)viewMillis);
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
		long millisBetween = endDate.getTime()-startDate.getTime();
		return (int)(Math.ceil((double)millisBetween / ((double)dayMillis*7)));
	}
	
	private void addWeeks()
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
			int left = dateToPixels(cal.getTime());
			
			if(currentWeek==1 || (!yearAlreadyPrinted ))
			{
				addYear(cal);
				yearAlreadyPrinted = true;
			}
			
			int currentMonth = cal.get(Calendar.MONTH);
			if(currentMonth != lastMonth)
			{
				addMonth(cal);
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
	
	private void addBorder()
	{
		appendLine("<div style='z-index:300; position:absolute; top:0px; left:0px; width:"+width+"px; height:"+height+"px; border:1px solid black'>&nbsp;</div>");
	}
	
	private void addMonth(Calendar cal)
	{
		Calendar monthCal = (Calendar) cal.clone();
		monthCal.set(Calendar.DAY_OF_MONTH, 1); //use 1st of month to display
		
		int left = dateToPixels(monthCal.getTime());
		String[] months = Configuration.getInstance().getMonths();
		String month = months[monthCal.get(Calendar.MONTH)];
		appendLine("<div class='month' style='left:"+left+"px'>"+month+"</div>");
	}
	
	private int yearZIndex = 6; //newer year label should overlap the old year (if startDate is very near to beginning of next year)
	private void addYear(Calendar cal)
	{
		Calendar yearCal = (Calendar) cal.clone();
		int left = 0;
		if(yearCal.get(Calendar.WEEK_OF_YEAR) == 1)
		{
			yearCal.set(Calendar.DAY_OF_YEAR, 1); //use January 1st to display year label 
			left = dateToPixels(yearCal.getTime());
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
		appendLine("<html>");
		/* HTML header, incl. CSS style sheets */
		appendLine("<head>");
		appendStyleSheets();
		appendScripts();
		appendLine("</head>");
		
		/* HTML body */
		appendLine("<body onload='updateToday()' style='position: absolute; top:20px; left:20px; margin: 40px; font-family: Arial; font-size: 10px'>");
		
		addBorder();
		
		/* years, months and calendar week labels */
		addWeeks();
		
		/* current date indicator line as javascript */
		addCurrentDate();
		
		/* tasks */
		addTasks(tasks);
		
		/* milestones */
		addMilestones(milestones);
		
		appendLine("</body>");
		appendLine("</html>");
	}
	
	private int dateToPixels(Date date)
	{
		return (int) ((double)(date.getTime() - startDate.getTime()) * widthFactor);
	}
	
	private void appendScripts()
	{
		appendLine("<script type='text/javascript'>");
		appendLine("function updateToday() ");
		appendLine("{");
		appendLine("   var todayDiv = document.getElementById('todayIndicator');");
		appendLine("   if(todayDiv == null) return;");
		appendLine("   var now = new Date();");
		appendLine("   var left = ((now.getTime() - "+startDate.getTime()+") * "+widthFactor+");");
		appendLine("   todayDiv.style.left = left;");
		appendLine("}");
		appendLine("</script>");
	}

	private void addCurrentDate()
	{
		Date today = new Date();
		int todayleft = dateToPixels(today);
		appendLine("<div id='todayIndicator' class='today' style='left: "+todayleft+"px'>&nbsp;</div>");
	}

	/**
	 * Clears the buffer
	 */
	public void reset()
	{
		sb = new StringBuffer();
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

	private void addMilestones(List<Milestone> milestones)
	{
		if(milestones==null)
			return;
		
		for(Milestone milestone : milestones)
		{
			int left = dateToPixels(milestone.getDueDate());
			appendLine("<div class='milestone' style='left: "+left+"px;'>"+milestone.getName()+"</div>");
		}
	}

	private void addTasks(List<Task> tasks)
	{
		if(tasks==null)
			return;
		
		int taskCount = 0;
		for(Task task : tasks)
		{
			StringBuffer description = new StringBuffer();
			description.append(task.getPercentDone());
			description.append("% \n");
			appendHtmlEscaped(task.getDescription(), description);

			int taskWidth = (int) (task.getDurationMillis() * (dayMillis/Configuration.getInstance().getWorkMillisPerDay()) * widthFactor) ;
			int doneWidth = (int) ((double)taskWidth * (double)task.getPercentDone()/100d);
			int left = dateToPixels(task.getBeginDate());
			
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
