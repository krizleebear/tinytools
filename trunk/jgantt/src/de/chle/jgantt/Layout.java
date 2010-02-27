package de.chle.jgantt;

import java.util.Date;
import java.util.List;

public class Layout
{
	Date startDate, endDate;
	int width, height;
	StringBuffer sb = new StringBuffer(2048);
	
	//default resolution: days

	public Layout(Date startDate, Date endDate)
	{
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	private void appendLine(String line)
	{
		sb.append(line);
		sb.append('\n');
	}
	
	public void layout(List<Task> tasks, int width, int height)
	{
		long viewMillis = endDate.getTime() - startDate.getTime();
		
		double widthFactor = (double)width / (double)viewMillis;
		
		appendLine("<html>");
		appendLine("<head>");
		appendLine("<style type='text/css'>");
		appendLine(".task { height: 15px; position: absolute; background-color:lightgray; text-align:left; vertical-align:middle; ; white-space:nowrap}");
		appendLine(".done { height: 15px; position: absolute; background-color:#FF99FF; }");
		appendLine("</style>");
		appendLine("</head>");
		appendLine("<body style='position: absolute; top:20px; left:20px; margin: 40px; font-family: Arial; font-size: 10px'>");
		
		//TODO html entities!
		
		/* time line */
		
		
		/* background :: calender weeks */
		
		
		/* tasks */
		
		int taskCount = 2;
		for(Task task : tasks)
		{
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
