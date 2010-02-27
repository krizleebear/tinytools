package de.chle.jgantt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main
{
	public static void main(String[] args) throws ParseException
	{
		Calendar start = Calendar.getInstance(Locale.GERMANY);
		start.clear();
		start.set(2009, Calendar.DECEMBER, 28); //attention: month starts with 0 !
		
		Calendar end = Calendar.getInstance(Locale.GERMANY);
		end.clear();
		end.set(2011, Calendar.JANUARY, 1);
		
		Layout layouter = new Layout(start.getTime(), end.getTime(), 1000, 300);
		List<Task> tasks = new ArrayList<Task>();
		
		tasks.add(new Task("#1", "erster Task", 40, "2010-02-27", 50));
		tasks.add(new Task("#2", "zweiter Task", 10, "2010-02-30", 75));
		
		layouter.layout(tasks);
	}
}
