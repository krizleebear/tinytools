package tinytools.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Update
{
	private String localChecksum = null;
	private String remoteChecksum = null;

	private URL remoteURL = null;
	private File localFile = null;
	
	public Update(URL remoteURL, File localFile)
	{
		this.remoteURL = remoteURL;
		this.localFile = localFile;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException, NoSuchAlgorithmException
	{
		if(args.length < 2)
		{
			System.err.println("Usage: Update <remoteURL> <localFile>");
			System.exit(0);
		}
		
		Update update = new Update(new URL(args[0]), new File(args[1]));
		if(update.isUpdateNeeded())
		{
			File tempFile = update.download();
			update.update(tempFile, update.localFile);
		}
	}
	
	public void update(File tempFile, File destFile) throws MalformedURLException, IOException
	{
		//copy temp file to active file
		destFile.renameTo(new File(destFile.getAbsolutePath()+"_OLD"));
		tempFile.renameTo(destFile);
	}
	
	public boolean isUpdateNeeded() throws IOException
	{
		URL checksumURL = new URL(remoteURL.toString()+".MD5");
		System.out.println("Getting remote checksum from " + checksumURL.toString());
		remoteChecksum = getOnlineChecksum(checksumURL);

		try
		{
			localChecksum = calculateChecksum(localFile);
		}
		catch(IOException e1)
		{
			System.out.println("Could not calculate local checksum.");
		}
		catch (NoSuchAlgorithmException e2)
		{
			e2.printStackTrace();
		}
		
		System.out.println("Local  checksum: " + localChecksum);
		System.out.println("Remote checksum: " + remoteChecksum);

		if(localChecksum == null || !localChecksum.equalsIgnoreCase(remoteChecksum))
		{
			System.out.println("This software needs an update. Downloading most current version.");
			return true;
		}
		else
		{
			System.out.println("We're up to date.");
			return false;
		}
	}
	
	public String getOnlineChecksum(URL checksumURL) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(checksumURL.openStream()));
		String line = in.readLine();
		in.close();
		
		return line;
	}
	
	public File download() throws MalformedURLException, IOException, NoSuchAlgorithmException
	{
		System.out.println("Creating temporary file.");
		File localTempFile = File.createTempFile("tintytools_update", "_temp");
		
		System.out.println("Downloading current version to temporary file " + localTempFile.getAbsolutePath());
		download(remoteURL, localTempFile);
		System.out.println("Updated version was downloaded to temporary file " + localTempFile.getAbsolutePath());
		
		//verify the checksum
		System.out.println("Verifying the checksum...");
		String tempChecksum = calculateChecksum(localTempFile);
		System.out.println("Checksum downloaded is " + tempChecksum);
		if(!tempChecksum.equalsIgnoreCase(remoteChecksum))
		{
			throw new IOException("Checksum error in file " + localTempFile.getAbsolutePath());
		}
		System.out.println("Checksum acknowledged.");
		return localTempFile;
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
