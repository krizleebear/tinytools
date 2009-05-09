package tinytools.vexplorer;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.xml.sax.SAXException;

public class VExplorer 
{
	private static final String DIRECTORY_IMAGES = "./images/";
	private String[] pathes = null;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 1)
		{
			System.out.println("Usage: VExplorer <path to video files> [<path to video files>...]");
			System.exit(1);
		}
		
		VExplorer ve = new VExplorer(args);
		ve.start();
	}

	public VExplorer(String[] pathes)
	{
		this.pathes = pathes;
	}
	
	private void start() 
	{
		// scan directories
		scanForFiles();
		
		// create HTML index
		generateHTML();
		
		// check if target directores exist - create them if not
		checkDirectories();
		
		// browse the web for posters
		downloadPosters();
	}

	private void checkDirectories()
	{
		// check for image directory
		File posterDirectory = new File(DIRECTORY_IMAGES);
		if(!posterDirectory.exists())
		{
			try
			{
				posterDirectory.mkdirs();
			}
			catch (Exception e1)
			{
				System.err.println(posterDirectory.getAbsolutePath() + " could not be created.");
			}
		}
	}

	private void downloadPosters()
	{
		PosterGrabber grabber = new PosterGrabber(new File(DIRECTORY_IMAGES));
		FileInfo[] videos = fileIndex.values().toArray(new FileInfo[0]);
		
		for (int i = 0; i < videos.length; i++) 
		{
			FileInfo currentVideo = videos[i];
		
			try
			{
				grabber.searchForPoster(currentVideo.getDisplayedName());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (SAXException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void generateHTML()
	{
		HtmlGenerator generator = new HtmlGenerator(fileIndex);
		try 
		{
			generator.generate("index.html");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void scanForFiles()
	{
		for (int i = 0; i < pathes.length; i++)
		{
			FileScanner scanner = new FileScanner(pathes[i], fileIndex);
			try 
			{
				scanner.indexFiles();
			} 
			catch (IOException e) 
			{
				System.err.println("Error scanning path " + pathes[i]);
			}
		}
	}

}
