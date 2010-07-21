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
		pre();

		handleAddedFiles(changes.addedFiles);
		handleModifiedFiles(changes.modifiedFiles);
		
		/* should we also delete files? */
		if(options.isDeleteOnSlave())
		{
			handleDeletedFiles(changes.deletedFiles);
		}

		post();
	}
	
	protected abstract void pre() throws Exception;
	
	protected abstract void handleAddedFiles(List<File> added) throws Exception;
	protected abstract void handleDeletedFiles(List<File> deleted) throws Exception;
	protected abstract void handleModifiedFiles(List<File> modified) throws Exception;

	protected abstract void post() throws Exception;
}
