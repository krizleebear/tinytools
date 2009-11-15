package tinytools.vexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.xml.sax.SAXException;

import tinytools.vexplorer.posters.PosterGrabber;

public class VExplorer
{
	private static final String DIRECTORY_IMAGES = "./images/";
	private String[] pathes = null;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();
	private Properties props = new Properties();
	PosterGrabber posterGrabber = new PosterGrabber();

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException
	{
		VExplorer ve;

		if (args.length == 0)
		{
			System.out.println("Usage:");
			System.out.println();
			System.out.println("VExplorer [<path to video files>] [<path to video files>...]");
			System.out.println("No pathes defined. Try reading configuration from Config.properties");

			ve = new VExplorer(new File("Config.properties"));
			ve.start();

		}
		if (args.length >= 1)
		{
			System.out.println("Usage: VExplorer <path to video files> [<path to video files>...]");
			ve = new VExplorer(args);
			ve.start();
		}

	}

	public VExplorer(String[] pathes)
	{
		this.pathes = pathes;
	}

	public VExplorer(File configFile) throws FileNotFoundException, IOException
	{
		readConfig(configFile);
	}

	private void readConfig(File configFile) throws FileNotFoundException, IOException
	{
		FileInputStream propsIn = new FileInputStream(configFile);
		props.load(propsIn);
		propsIn.close();
		
		List<String> videoPathes = new ArrayList<String>();
		
		//get all keys starting with 'VideoPath'
		Set keys = props.keySet();
		for(Object o : keys)
		{
			String key = (String)o;
			if(key.startsWith("VideoPath"))
			{
				String path = (String) props.get(key);
				System.out.println("Adding videopath: " + path);
				videoPathes.add( path );
			}
		}
		this.pathes = videoPathes.toArray(new String[0]);
	}

	private void start()
	{
		// scan directories
		scanForFiles();

		// check if target directores exist - create them if not
		checkDirectories();
		
		// browse the web for posters
		beginDownloadPosters();
		
		// create HTML index
		generateHTML();
		
		// wait until all poster are completely downloaded
		posterGrabber.waitUntilDone();
	}

	private void checkDirectories()
	{
		// check for image directory
		File posterDirectory = new File(DIRECTORY_IMAGES);
		if (!posterDirectory.exists())
		{
			try
			{
				posterDirectory.mkdirs();
			}
			catch (Exception e1)
			{
				System.err.println(posterDirectory.getAbsolutePath()
						+ " could not be created.");
			}
		}
	}

	private void beginDownloadPosters()
	{
		FileInfo[] videos = fileIndex.values().toArray(new FileInfo[0]);
		
		/* init search hints */
		Properties hints = new Properties();
		File searchHintsFile = new File("SearchHints.properties");
		try
		{
			hints.load(new FileReader(searchHintsFile));
			System.out.println("Loaded search hints from: " + searchHintsFile.getAbsolutePath());
			
		}
		catch (IOException e1)
		{
			System.err.println("Could not read search hints from: " + searchHintsFile.getAbsolutePath());
		}
		
		/* enumerate all videos */
		for (int i = 0; i < videos.length; i++)
		{
			FileInfo currentVideo = videos[i];
			String searchString = hints.getProperty(currentVideo.getDisplayedName().toLowerCase());
			if(searchString==null)
				searchString = currentVideo.getDisplayedName();
			else
				System.out.println("Using search hint: " + searchString);
			
			try
			{
				posterGrabber.downloadPosters(currentVideo, searchString);
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
