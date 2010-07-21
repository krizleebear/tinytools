package tinytools.jsync;

import java.io.File;
import java.util.List;

public abstract class SynchronizerBase implements ISynchronizer
{
	protected ChangeSet changes;
	protected File masterDir, slaveDir;
	protected Options options;
	
	@Override
	public void setChanges(ChangeSet changes)
	{
		this.changes = changes;
	}

	@Override
	public void setMasterDir(File masterDir)
	{
		this.masterDir = masterDir;
	}

	@Override
	public void setSlaveDir(File slaveDir)
	{
		this.slaveDir = slaveDir;
	}
	
	@Override
	public void setOptions(Options options)
	{
		this.options = options;
	}

	@Override
	public void synchronize() throws Exception
	{
		if(options.isOnlyListModified())
		{
			listChanges();
			return;
		}
		
		pre();

		handleAddedFiles(changes.addedFiles);
		handleModifiedFiles(changes.modifiedFiles);
		
		/* should we also delete files? */
		if(options.isDeleteOnSlave())
		{
			handleDeletedFiles(changes.deletedFiles);
		}
		else
		{
			if(changes.deletedFiles != null && changes.deletedFiles.size()>0)
			{
				System.out.println(changes.deletedFiles.size() + " files have been deleted on master. This changes will be ignored. Use --deleteOnSlave to do a full sync.");
				changes.deletedFiles.clear();
			}
		}

		post();
	}
	
	public void listChanges()
	{
		for(File addedFile : changes.addedFiles)
		{
			System.out.println("+ " + addedFile.getAbsolutePath());
		}
		for(File deletedFile : changes.deletedFiles)
		{
			System.out.println("- " + deletedFile.getAbsolutePath());	
		}
		for(File modifiedFile : changes.modifiedFiles)
		{
			System.out.println("~ " + modifiedFile.getAbsolutePath());
		}
	}
	
	protected abstract void pre() throws Exception;
	
	protected abstract void handleAddedFiles(List<File> added) throws Exception;
	protected abstract void handleDeletedFiles(List<File> deleted) throws Exception;
	protected abstract void handleModifiedFiles(List<File> modified) throws Exception;

	protected abstract void post() throws Exception;
}
