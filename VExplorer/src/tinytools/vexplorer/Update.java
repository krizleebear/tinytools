package tinytools.vexplorer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Update
{
	
	private final static String UPDATE_TEMP_FILE = "VExplorer_update.jar";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Update update = new Update();
		try
		{
			int versionOnline = update.getOnlineVersionNumber();
			System.out.println("Current version number: " +Props.VERSION_CURRENT);
			
			System.out.println("Version number online available: " + versionOnline);
			
			try
			{
				System.out.println("Current checksum: " +calculateChecksum(new File("VExplorer.jar")));	
				//calculateChecksum(new File(UPDATE_TEMP_FILE));
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			
			if(versionOnline > Props.VERSION_CURRENT)
			{
				System.out.println("This software needs an update. Downloading most current version.");
				Update.download(new URL(Props.UPDATE_URL), new File(UPDATE_TEMP_FILE));
				System.out.println("Updated version was downloaded to a temporary file.");
			}
			else
			{
				System.out.println("We're up to date.");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getOnlineVersionNumber() throws IOException
	{
		URL url = new URL(Props.VERSION_ONLINE_URL);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = in.readLine();
		in.close();
		
		return Integer.parseInt(line);
	}
	
	public int getOnlineChecksum() throws IOException
	{
		URL url = new URL(Props.VERSION_ONLINE_URL);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = in.readLine();
		in.close();
		
		return Integer.parseInt(line);
	}

	public static void download(URL url, File file) throws IOException
	{
		//delete an old existing temp file
		if(file.exists())
			file.delete();
		
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		
		int i;
        while ((i = in.read()) != -1)
        {
           out.write( i );
        }
        
        in.close();
        out.close();
	}
	
	public static String calculateChecksum(File f) throws IOException, NoSuchAlgorithmException
	{
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		
		int i;
        while ((i = in.read()) != -1)
        {
           digest.update((byte)i);
        }
        
        byte[] hash = digest.digest();
        String hashString = getHexString(hash);
        return hashString;
	}
	
	public static String getHexString(byte[] bytes)
	{
		String result = "";
		for (int i = 0; i < bytes.length; i++)
		{
			result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
}
