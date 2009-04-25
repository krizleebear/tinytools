package tinytools.vexplorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class HtmlGenerator 
{
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();
	private FileInfo[] videos;
	private StringBuilder sb = new StringBuilder();

	public HtmlGenerator(HashMap<String, FileInfo> fileIndex)
	{
		this.fileIndex = fileIndex;
		videos = fileIndex.values().toArray(new FileInfo[0]);
		Arrays.sort(videos);
	}
	
	public void generate(String destinationFile) throws IOException
	{
		writeHeader();
		
		for (int i = 0; i < videos.length; i++) 
		{
			FileInfo currentVideo = videos[i];
			
			System.out.print(currentVideo.getTokenizedFilename());
			sb.append("<a href=\""+currentVideo.getFile().toURI().toString()+"\">"+currentVideo.getTokenizedFilename()+"</a>");
			
			//check next entry to see if it has the same tokenized filename
			//that's our indicator to recognize multipart videos
			int consecutivePartIndex = 1;
			String nextName = getNextName(i);
			while(nextName.equals(currentVideo.getTokenizedFilename()))
			{
				System.out.print( " [" + ++consecutivePartIndex + "]");
				sb.append(" <a href=\""+getNextFile(i).toURI().toString()+"\">["+consecutivePartIndex+"]</a>");
				i++;
				nextName = getNextName(i);
			}
			
			System.out.println();
			sb.append("<br/>\n");
		}
		
		writeFooter();
		
		FileOutputStream out = new FileOutputStream(destinationFile, false);
		OutputStreamWriter sw = new OutputStreamWriter(out, Charset.forName("UTF-8"));
		sw.write(sb.toString());
		sw.close();
	}
	
	private void writeHeader()
	{
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		sb.append("<head>\n");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
		sb.append("<title>Video Index</title>\n");
		sb.append("<style type=\"text/css\">\n");
		sb.append("<!--\n");
		sb.append("a {\n");
		sb.append("	font-family: Arial, Helvetica, sans-serif;\n");
		sb.append("	font-size: 72px;\n");
		sb.append("	color: #000000;\n");
		sb.append("}\n");
		sb.append("-->\n");
		sb.append("</style>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
	}
	
	private void writeFooter() 
	{
		sb.append("</body>\n");
		sb.append("</html>\n");
	}

	private File getNextFile(int currentIndex)
	{
		if(currentIndex < videos.length-1)
		{
			return videos[currentIndex+1].getFile();
		}
		return null;
	}
	
	private String getNextName(int currentIndex)
	{
		if(currentIndex < videos.length-1)
		{
			return videos[currentIndex+1].getTokenizedFilename();
		}
		return "";
	}
}
