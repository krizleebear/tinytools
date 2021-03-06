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
	
	public void update(Backup createBackup) throws IOException, NoSuchAlgorithmException
	{
		if(isUpdateNeeded())
		{
			File tempFile = download();
			replaceFile(tempFile, localFile, createBackup);
		}
	}
	
	public enum Backup { CreateBackup, DontCreateBackup }
	
	private void replaceFile(File tempFile, File destFile, Backup createBackup) throws MalformedURLException, IOException
	{
		//copy temporary file to active file
		if(destFile.exists() && createBackup == Backup.CreateBackup)
		{
			File backupFile = new File(destFile.getAbsolutePath()+"_OLD");
			System.out.println("Creating backup of current version: " + backupFile.getAbsolutePath());
			moveFile(destFile, backupFile, Overwrite.Overwrite);
		}
		
		System.out.println("Renaming tempfile to " + destFile.getAbsolutePath());
		moveFile(tempFile, destFile, Overwrite.Overwrite);
	}
	
	private boolean isUpdateNeeded() throws IOException
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
	
	public static String getOnlineChecksum(URL checksumURL) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(checksumURL.openStream()));
		String line = in.readLine();
		in.close();
		
		return line;
	}
	
	private File download() throws MalformedURLException, IOException, NoSuchAlgorithmException
	{
		System.out.println("Creating temporary file.");
		File localTempFile = File.createTempFile("tintytools_update", "_temp", new File("."));
		localTempFile.deleteOnExit();
		
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

	public static void download(URL url, File destFile) throws IOException
	{
		//delete an old existing temporary file
		if(destFile.exists())
			destFile.delete();
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try
		{
			in = new BufferedInputStream(url.openStream());
			out = new BufferedOutputStream(new FileOutputStream(destFile));
			
			int i;
	        while ((i = in.read()) != -1)
	        {
	           out.write( i );
	        }
		}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (Exception ex1) {} //ignore

			try
			{
				if (out != null)
					out.close();
			}
			catch (Exception ex2) {} //ignore
		}
	}
	
	public static String calculateChecksum(File f) throws IOException,
			NoSuchAlgorithmException
	{
		MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		BufferedInputStream in = null;
		
		try
		{
			in = new BufferedInputStream(new FileInputStream(f));

			int i;
			while ((i = in.read()) != -1)
			{
				digest.update((byte) i);
			}
		}
		finally
		{
			if (in != null)
				in.close();
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
	
	public static void copyFile(File source, File dest) throws IOException
	{		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
		
		int i;
        while ((i = in.read()) != -1)
        {
           out.write( i );
        }
        
        in.close();
        out.close();
	}
	
	public enum Overwrite { Overwrite, DontOverwrite }
	
	public static void moveFile(File source, File dest, Overwrite overwrite) throws IOException
	{
		boolean success = false;
		
		if(dest.exists() && overwrite == Overwrite.Overwrite)
		{
			success = dest.delete();
			if(!success)
				throw new IOException("Could not overwrite " + dest.getAbsolutePath());
		}
		
		success = source.renameTo(dest);
		if(!success)
			throw new IOException(source.getAbsolutePath() + " could not be moved to " + dest.getAbsolutePath());
	}
	
}