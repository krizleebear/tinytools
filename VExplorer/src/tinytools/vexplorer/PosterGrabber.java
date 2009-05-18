package tinytools.vexplorer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;

import org.ccil.cowan.tagsoup.HTMLScanner;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class PosterGrabber
{
	private String searchURLFormat = "http://www.amazon.de/s/ref=nb_ss_w?__mk_de_DE=%C5M%C5Z%D5%D1&url=search-alias%3Ddvd&field-keywords=";

	private XMLReader htmlParser = new Parser();
	private HTMLSchema schema = new HTMLSchema();

	private File targetDirectory;
	
	public PosterGrabber(File targetDirectory)
	{
		this.targetDirectory = targetDirectory;
	}

	public void searchForPoster(String keyword) throws IOException,
			SAXException
	{
		keyword = escapeKeyword(keyword);
		
		final String expectedFileName = keyword+".png";
		File poster = findPosterOnDisk(expectedFileName);
		
		if(poster!=null)
		{
			System.out.println("Poster for '"+keyword+"' already found in "+poster.getAbsolutePath());
			return;
		}
		
//		keyword = escapeKeyword(keyword);

		IPosterHandler smallPicHandler = new PosterHandlerAmazonSmall(keyword);
		System.out.println(smallPicHandler.getContentUrl());

//		writeContentToFile(smallPicHandler);

		htmlParser.setProperty(Parser.schemaProperty, schema);
		htmlParser.setContentHandler(smallPicHandler);

		htmlParser.parse(smallPicHandler.getContentUrl());
		
		String smallposterURL = smallPicHandler.getPosterUrl();
		if(smallposterURL==null)
		{
			System.err.println("No poster found for keyword " + keyword);
		}
		else
		{
			System.out.println("Found small poster for '"+keyword+"': " + smallposterURL);
			System.out.println("Loading poster...");
			
			BufferedImage smallposter = readImage(smallposterURL);
			
			//String[] supportedFormats = ImageIO.getWriterFormatNames();
			if(smallposter == null)
			{
				System.err.println("Error reading image from " + smallposterURL);
			}
			else
			{
				System.out.println("Poster has been successfully read.");
				File targetFile = new java.io.File(targetDirectory, expectedFileName);
				try
				{
					ImageIO.write(smallposter, "png", targetFile);
					System.out.println("Poster has been written to " + targetFile.getAbsolutePath());
				}
				catch(Exception ex)
				{
					System.err.println("Poster could not be written to " + targetFile.getAbsolutePath());
				}
			}
		}
	}
	
	private File findPosterOnDisk(final String expectedFileName)
	{
		File[] files = targetDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name)
			{
				if(name.equals(expectedFileName))
					return true;
				return false;
			}});
		if(files!=null && files.length>0)
			return files[0];
		return null;
	}

	private BufferedImage readImage(String url) throws MalformedURLException, IOException
	{
		if(url==null)
			return null;
		
		return ImageIO.read(new URL(url));
	}
	
	private String escapeKeyword(String keyword)
	{
		StringBuilder sb = new StringBuilder();
		char[] chars = keyword.toCharArray();
		
		for(char c : chars)
		{
			if	(
					(c >= '!' && c <= ',') 
				||	(c >= ':' && c <= '@')
				||	(c >= '[' && c <= '^')
				||	(c == '`')
				||	(c > 'z')
				)
			{
				sb.append('%');
				sb.append( Integer.toHexString((int)c).toUpperCase() );
			}
			else
			{
				sb.append(c);
			}
		}
		
		return sb.toString(); 
	}
	
	private void writeContentToFile(IPosterHandler smallPicHandler)
			throws MalformedURLException, IOException
	{
		URL url = new URL(smallPicHandler.getContentUrl());
		
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String thisLine;
		java.io.FileWriter fw = new FileWriter("C:/temp/test.html");
		java.io.PrintWriter pw = new PrintWriter(fw);
		while ((thisLine = br.readLine()) != null)
		{
			pw.println(thisLine);
		}
		pw.close();
	}

	public static void main(String[] args)
	{
		File posterDirectory = new File("./images/");
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
		
		PosterGrabber pg = new PosterGrabber(posterDirectory);
		try
		{
			pg.searchForPoster("Kirschblüten");
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
