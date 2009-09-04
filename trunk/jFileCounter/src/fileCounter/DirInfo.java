package fileCounter;

import java.io.File;
import java.text.NumberFormat;

public class DirInfo
{

	File path;
	int fileCount;
	int depth;
	DirInfo parent;
	long byteCount;

	public long getByteCount()
	{
		return byteCount;
	}

	public DirInfo getParent()
	{
		return parent;
	}

	public void setParent(DirInfo parent)
	{
		this.parent = parent;
	}

	public DirInfo(File f)
	{
		this.path = f;
	}

	public File getPath()
	{
		return path;
	}

	public void incFileCount(int count)
	{
		fileCount += count;
		DirInfo parentInfo = getParent();
		if (parentInfo != null)
		{
			parentInfo.incFileCount(count);
		}
	}

	public void incBytecount(long count)
	{
		this.byteCount += count;
		DirInfo parentInfo = getParent();
		if (parentInfo != null)
		{
			parentInfo.incBytecount(count);
		}
	}

	public void setPath(File path)
	{
		this.path = path;
	}

	public int getFileCount()
	{
		return fileCount;
	}

	public void setFileCount(int fileCount)
	{
		this.fileCount = fileCount;
	}

	public int getDepth()
	{
		return depth;
	}

	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	private static NumberFormat nf = NumberFormat.getIntegerInstance();

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getDepth(); i++)
		{
			sb.append("|----");
		}
		sb.append(path.getName());
		sb.append(" (");
		sb.append(getFileCount());
		sb.append(" files --- ");

		sb.append(nf.format(getByteCount()));

		// sb.append(getByteCount());
		sb.append(" bytes");
		sb.append(")");
		return sb.toString();

	}

}
