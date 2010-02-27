package de.chle.jgantt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main
{
	public static void main(String[] args)
	{
		Layout layouter = new Layout(new Date(2010,02,01), new Date(2010,02,28));
		List<Task> tasks = new ArrayList<Task>();
		
		tasks.add(new Task("#1", "erster Task", 40, new Date(2010, 02, 27), 50));
		tasks.add(new Task("#2", "zweiter Task", 10, new Date(2010, 02, 30), 75));
		
		layouter.layout(tasks, 500, 300);
	}
}
