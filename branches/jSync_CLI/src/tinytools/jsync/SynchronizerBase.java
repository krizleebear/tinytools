package tinytools.jsync;

import java.io.File;

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
	public abstract void synchronize() throws Exception;
}
