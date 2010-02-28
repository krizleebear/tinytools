package de.chle.jgantt;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main
{
	public static void main(String[] args) throws ParseException, IOException
	{
		DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

		/* create some test data */
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task("#1", "erster Task", 40, "2010-02-27", 50));
		tasks.add(new Task("#2", "zweiter Task", 10, "2010-02-30", 75));
		
		List<Milestone> milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone("B2 sample", dateParser.parse("2010-03-31")));

		/* Configuration class offers some basic options */
		Configuration.getInstance().setLocale(Locale.GERMANY);
		
		Layout layouter = new Layout(dateParser.parse("2009-12-24"), dateParser.parse("2011-01-31"), 1000, 300);
		layouter.layout(tasks, milestones);
		
		String html = layouter.getHTML();
		
		FileWriter outFile = new FileWriter("gantt.html");
		outFile.write(html);
		outFile.close();

	}
}
