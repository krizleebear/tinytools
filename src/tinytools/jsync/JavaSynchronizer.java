package tinytools.jsync;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JavaSynchronizer extends SynchronizerBase
{

	@Override
	protected void handleAddedFiles(List<File> added) throws Exception
	{
		for(File f : added)
		{
			File destFile = new File(slaveDir, f.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
			
			if(f.isDirectory())
			{
				destFile.mkdirs();
			}
			else
			{
				createPathTo(destFile);
				copyFile(f, destFile);
			}
		}
	}

	@Override
	protected void handleDeletedFiles(List<File> deleted) throws Exception
	{
		for(File f : deleted)
		{
			if(f.isDirectory())
			{
				FileHelper.deleteDirectory(f);
			}
			else
			{
				f.delete();
			}
		}
	}

	@Override
	protected void handleModifiedFiles(List<File> modified) throws Exception
	{
		for(File f : modified)
		{
			if(f.isDirectory())
				continue;
			
			File destFile = new File(slaveDir, f.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
			copyFile(f, destFile);
		}
	}

	@Override
	protected void post() throws Exception
	{
	}

	@Override
	protected void pre() throws Exception
	{
	}
	
	private void copyFile(File source, File dest) throws IOException
	{		
		FileHelper.copyFile(source, dest);

        /* set the timestamp of dest to the value of source */
        dest.setLastModified(source.lastModified());
	}
	
	private void createPathTo(File destFile) throws IOException
	{
		File destParent = destFile.getParentFile();
		if(destParent.exists())
		{
			return;
		}
		
		if(!destParent.mkdirs())
		{
			throw new IOException("Could not create path to: " + destFile.getAbsolutePath());
		}
	}
}
