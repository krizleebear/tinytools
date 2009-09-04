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

		int warnLevelCount = Integer.parseInt(System.getProperty("WarnLevelCount", "0"));
		int warnLevelSize = Integer.parseInt(System.getProperty("WarnLevelSize", "0"));

		for (DirInfo info : infoStore)
		{
			if (warnLevelCount <= info.getFileCount() || warnLevelSize <= info.getByteCount())
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
		DirInfo info = new DirInfo(currentDir);
		info.setDepth(depth);
		info.setParent(parentInfo);

		infoStore.add(info);

		File[] files = currentDir.listFiles();
		for (File f : files)
		{
			if (f.isDirectory())
			{
				dirCounter++;
				climb(f, info, depth + 1);
			}
			else
			{
				fileCounter++;

				info.incBytecount(f.length());
				info.incFileCount(1);
			}

			if (System.currentTimeMillis() - lastOutput > 1000)
			{
				log(".");
				lastOutput = System.currentTimeMillis();
			}
		}
	}

}
