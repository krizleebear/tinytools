package tinytools.jsync;

import java.io.File;

public interface ISynchronizer
{

	public void setChanges(ChangeSet changes);
	public void setMasterDir(File masterDir);
	public void setSlaveDir(File slaveDir);
	
	public void synchronize() throws Exception;
	
}
