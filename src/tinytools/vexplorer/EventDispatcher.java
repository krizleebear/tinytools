package tinytools.vexplorer;

import java.util.List;
import java.util.Vector;

public class EventDispatcher implements IExplorerListener, IExplorerService
{
	private static EventDispatcher instance = new EventDispatcher();

	private Vector<IExplorerListener> listeners = new Vector<IExplorerListener>();
	private Vector<IExplorerService> services = new Vector<IExplorerService>();

	private EventDispatcher()
	{
	}

	public static EventDispatcher getInstance()
	{
		return instance;
	}

	public void addService(IExplorerService service)
	{
		services.add(service);
	}

	public void addListener(IExplorerListener listener)
	{
		listeners.add(listener);
	}

	public void updateFile(FileInfo file)
	{
		for (IExplorerListener listener : listeners)
			listener.updateFile(file);
	}

	public void updateFileList(FileInfo[] filelist)
	{
		for (IExplorerListener listener : listeners)
			listener.updateFileList(filelist);
	}

	public void readSettings() throws Exception
	{
		for(IExplorerService service : services)
			service.readSettings();
	}

	public void startExploring() throws Exception
	{
		for(IExplorerService service : services)
			service.startExploring();
	}

	public void updateStatus(int itemsDone, int itemsTotal)
	{
		for (IExplorerListener listener : listeners)
			listener.updateStatus(itemsDone, itemsTotal);
	}
}
