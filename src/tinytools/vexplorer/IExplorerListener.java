package tinytools.vexplorer;

public interface IExplorerListener
{
	void updateFileList(FileInfo[] filelist);
	void updateFile(FileInfo file);
	void updateStatus(int itemsDone, int itemsTotal);
}
