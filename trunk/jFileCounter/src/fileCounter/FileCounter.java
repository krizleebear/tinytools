package fileCounter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileCounter
{
	private File path;
	private List<DirInfo> infoStore = new LinkedList<DirInfo>();

	public FileCounter(File path)
	{
		this.path = path;
	}

	public void run()
	{
		climb(this.path, null, 0);

		/* Files will only be printed if they fulfill certain criteria */
		int warnLevelCount = Integer.parseInt(System.getProperty("WarnLevelCount", "0"));
		int warnLevelSize = Integer.parseInt(System.getProperty("WarnLevelSize", "0"));

		for (DirInfo info : infoStore)
		{
			if (warnLevelCount <= info.getFileCount() && warnLevelSize <= info.getByteCount())
			{
				logLine(info.toString());
			}
		}

		logLine("Analyzed " + dirCounter + " directories and " + fileCounter + " files within " + path.getAbsolutePath() + ".");
	}

	void logLine(String s)
	{
		System.out.println(s);
	}

	void log(String s)
	{
		System.out.print(s);
	}

	private int fileCounter = 0;
	private int dirCounter = 0;
	private long lastOutput = System.currentTimeMillis();

	private void climb(File currentDir, DirInfo parentInfo, int depth)
	{
		DirInfo dirInfo = new DirInfo(currentDir);
		dirInfo.setDepth(depth);
		dirInfo.setParent(parentInfo);

		infoStore.add(dirInfo);

		File[] files = currentDir.listFiles();
		for (File currentFile : files)
		{
			dirInfo.updateLongestFileNameLength(currentFile.getName().length());
			
			if (currentFile.isDirectory())
			{
				dirCounter++;
				climb(currentFile, dirInfo, depth + 1);
			}
			else
			{
				fileCounter++;
				dirInfo.addFile(currentFile);
			}

			if (System.currentTimeMillis() - lastOutput > 1000)
			{
				log(".");
				lastOutput = System.currentTimeMillis();
			}
		}
	}

}
