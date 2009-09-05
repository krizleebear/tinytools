package fileCounter;

import java.io.File;
import java.text.NumberFormat;

public class DirInfo
{
	private File path;
	private int fileCount;
	private int depth;
	private DirInfo parent;
	private long byteCount;
	private int longestFileNameLength;
	private long biggestFileSize;

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

	private void incFileCount(int count)
	{
		fileCount += count;
		DirInfo parentInfo = getParent();
		if (parentInfo != null)
		{
			parentInfo.incFileCount(count);
		}
	}
	
	private void updateBiggestFileSize(long size)
	{
		if(size > this.biggestFileSize)
		{
			this.biggestFileSize = size;
			
			DirInfo parentInfo = getParent();
			if (parentInfo != null)
			{
				parentInfo.updateBiggestFileSize(size);
			}
		}
	}

	public long getBiggestFileSize()
	{
		return biggestFileSize;
	}

	public void addFile(File f)
	{
		incBytecount(f.length());
		incFileCount(1);
	}
	
	private void incBytecount(long count)
	{
		this.byteCount += count;
		
		updateBiggestFileSize(count);
		
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
			sb.append("|---");
		}
		sb.append(path.getName());
		sb.append(" (");
		
		sb.append(getFileCount());
		sb.append(" files --- ");

		sb.append(nf.format(getByteCount()));
		sb.append(" bytes");
		
		sb.append(" --- biggest file: ");
		sb.append(nf.format(getBiggestFileSize()));
		
		sb.append(" --- longest filename: ");
		sb.append(getLongestFileNameLength());
		
		sb.append(")");
		return sb.toString();
	}

	public void updateLongestFileNameLength(int currentFileNameLength)
	{
		if(currentFileNameLength > getLongestFileNameLength())
		{
			this.longestFileNameLength = currentFileNameLength;
			if(this.getParent()!=null)
				getParent().updateLongestFileNameLength(currentFileNameLength);
		}
	}

	public int getLongestFileNameLength()
	{
		return longestFileNameLength;
	}

}
