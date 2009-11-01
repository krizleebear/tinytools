package tinytools.vexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.xml.sax.SAXException;

import tinytools.vexplorer.posters.PosterGrabber;
import tinytools.vexplorer.posters.PosterResult;

public class VExplorer
{
	private static final String DIRECTORY_IMAGES = "./images/";
	private String[] pathes = null;
	private HashMap<String, FileInfo> fileIndex = new HashMap<String, FileInfo>();
	private Properties props = new Properties();

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
		downloadPosters();
		
		// create HTML index
		generateHTML();
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

	private void downloadPosters()
	{
		PosterGrabber grabber = new PosterGrabber();
		FileInfo[] videos = fileIndex.values().toArray(new FileInfo[0]);

		for (int i = 0; i < videos.length; i++)
		{
			FileInfo currentVideo = videos[i];

			try
			{
				File smallPicFile = new File("./images/", currentVideo.getSmallPicFile());
				PosterResult smallPoster = grabber.downloadSmallPoster(currentVideo.getDisplayedName(), smallPicFile);
				currentVideo.setSmallPoster(smallPoster);
				
				if(smallPoster!=null)
				{
					File mediumPicFile = new File("./images/", currentVideo.getMediumPicFile());
					PosterResult mediumPoster = grabber.downloadMiddlePoster(smallPoster.getNextRessourceURL(), mediumPicFile);
					currentVideo.setMediumPoster(mediumPoster);
					
					if(mediumPoster!=null && mediumPoster.getNextRessourceURL()!=null)
					{
						File largePicFile = new File("./images/", currentVideo.getLargePicFile());
						//PosterResult largePoster = grabber.do
					}
				}
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
