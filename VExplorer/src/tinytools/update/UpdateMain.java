package tinytools.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import tinytools.update.Update.Backup;

public class UpdateMain
{
	public static final String UPDATE_PROPERTIES_FILE = "Update.properties";
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException, NoSuchAlgorithmException
	{
		Update update = null;
		
		if(args.length == 0)
		{
			System.out.println("Alternative usage: Update <remoteURL> <localFile>");
			
			Properties props = new Properties();
			props.load(new FileInputStream(UPDATE_PROPERTIES_FILE));
			
			String baseURL = props.getProperty("BaseURL", "http://tinytools.googlecode.com/svn/trunk/VExplorer/releases/");
			
			//first update Update.properties
			System.out.println("Updating " + UPDATE_PROPERTIES_FILE);
			URL updatePropertyURL = new URL(baseURL + UPDATE_PROPERTIES_FILE);
			Update propUpdate = new Update(updatePropertyURL, new File(UPDATE_PROPERTIES_FILE));
			propUpdate.update(Backup.CreateBackup);
			
			List<String> filesToUpdate = getFilesToUpdate(props);
			for(String file : filesToUpdate)
			{
				System.out.println("------------------------------------------------------------------------------------");
				URL remoteURL = new URL(baseURL+file);
				File localFile = new File(file);
				update = new Update(remoteURL, localFile);
				update.update(Backup.CreateBackup);
			}
		}
		else if(args.length == 2)
		{
			System.err.println("Usage: Update <remoteURL> <localFile>");
			File localFile = new File(args[1]);
			update = new Update(new URL(args[0]), localFile);
			update.update(Backup.CreateBackup);
		}
	}
	
	private static List<String> getFilesToUpdate(Properties props) throws FileNotFoundException, IOException
	{
		List<String> filesToUpdate = new ArrayList<String>();
		
		//get all keys starting with 'File'
		Set keys = props.keySet();
		for(Object o : keys)
		{
			String key = (String)o;
			if(key.startsWith("File"))
			{
				String path = (String) props.get(key);
				System.out.println("Adding file to update: " + path);
				filesToUpdate.add( path );
			}
		}
		return filesToUpdate;
	}
}
