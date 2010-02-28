package de.chle.jgantt;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Main
{
	public static void main(String[] args) throws ParseException, IOException
	{
		Calendar start = Calendar.getInstance(Locale.GERMANY);
		start.clear();
		start.set(2010, Calendar.FEBRUARY, 21); //attention: month starts with 0 !
		
		Calendar end = Calendar.getInstance(Locale.GERMANY);
		end.clear();
		end.set(2011, Calendar.APRIL, 2);
		
		Layout layouter = new Layout(start.getTime(), end.getTime(), 1000, 300);
		List<Task> tasks = new ArrayList<Task>();
		
		tasks.add(new Task("#1", "erster Task", 40, "2010-02-27", 50));
		tasks.add(new Task("#2", "zweiter Task", 10, "2010-02-30", 75));
		
		layouter.layout(tasks);
		
		String html = layouter.getHTML();
		
		FileWriter outFile = new FileWriter("gantt.html");
		outFile.write(html);
		outFile.close();

	}
}
