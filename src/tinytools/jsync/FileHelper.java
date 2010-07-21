package tinytools.jsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileHelper
{
	/**
	 * File copy using Java NewIO - inspired by
	 * http://forums.sun.com/thread.jspa?threadID=439695&messageID=2917510
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException
	{
		FileChannel in = new FileInputStream(src).getChannel();
		FileChannel out = new FileOutputStream(dest).getChannel();
		try
		{
			/*
			 * like described on Sun Forums, Windows has problems with chunks
			 * >64 MB on UNCs
			 */
			int maxCount = (64 * 1024 * 1024) - (32 * 1024);
			long size = in.size();
			long position = 0;
			while (position < size)
			{
				position += in.transferTo(position, maxCount, out);
			}
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * From http://www.rgagnon.com/javadetails/java-0483.html
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path)
	{
		if (path.exists())
		{
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]);
				}
				else
				{
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

}
