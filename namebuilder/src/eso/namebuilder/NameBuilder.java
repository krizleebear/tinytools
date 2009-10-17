package eso.namebuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NameBuilder
{
	public static void main(String[] args) throws Exception
	{
		File firstnames, lastnames;
		firstnames = new File("firstnames.txt");
		lastnames = new File("lastnames.txt");
		NameBuilder nb = new NameBuilder(firstnames, lastnames);
		nb.build();
	}
	
	File firstnameFile, lastnameFile;
	
	public NameBuilder(File firstnames, File lastnames)
	{
		this.firstnameFile = firstnames;
		this.lastnameFile = lastnames;
	}
	
	private void readLines(File textFile, List<String> list) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(textFile));
		String line;
		while((line=br.readLine())!=null)
		{
			line = line.trim(); // trim whitespaces
			
			if(line.length()<=0) //ignore empty lines
				continue;
			
			if(line.startsWith("<") && line.endsWith(">")) //ignore tagged comments
				continue;
			
			if(line.trim().length()>0) 
			{
				list.add(line);
			}
        }
	}
	
	public void build() throws IOException
	{
		List<String> firstnames = new ArrayList<String>();
		List<String> lastnames = new ArrayList<String>();
		
		readLines(firstnameFile, firstnames);
		readLines(lastnameFile, lastnames);
		
		for(int i=0; i<firstnames.size(); i++)
		{
			String firstname = firstnames.get(i % (firstnames.size()));
			
			for(int j=0; j<lastnames.size(); j++)
			{
				String lastname = lastnames.get(j % (lastnames.size()));
				String newName = firstname + " " + lastname;
				
				handleName(newName);
			}
		}
	}
	
	public void handleName(String newName)
	{
		System.out.println(newName);
	}
}
