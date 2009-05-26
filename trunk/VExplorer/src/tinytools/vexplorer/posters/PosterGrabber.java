package tinytools.vexplorer.posters;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

public class PosterGrabber
{
	private IPosterFinder smallPosterFinder = new PosterHandlerAmazonSmall();
	private IPosterFinder middlePosterFinder = new PosterHandlerAmazonMid();
	
	public PosterResult downloadSmallPoster(String movieName, File targetFile) throws IOException, SAXException
	{
		// does poster already exist on disk?
		if(targetFile.exists())
		{
			System.out.println("Poster for '"+movieName+"' already found in "+targetFile.getAbsolutePath());
			return null;
		}
		
		// search for poster on the web
		PosterResult posterInfo = smallPosterFinder.findPoster(movieName);
		if(posterInfo.getPosterURL()==null)
		{
			System.err.println("No poster found for " + movieName);
		}
		else
		{
			System.out.println("Downloading poster from " + posterInfo.getPosterURL());
			URL smallPosterURL = new URL(posterInfo.getPosterURL());
			downloadImage(smallPosterURL, targetFile);
		}
		return posterInfo;
	}
	
	public PosterResult downloadMiddlePoster(String url, File targetFile) throws IOException, SAXException
	{
		// does poster already exist on disk?
		if(targetFile.exists())
		{
			System.out.println("Middle-sized poster already found in "+targetFile.getAbsolutePath());
			return null;
		}
		
		PosterResult posterInfo = middlePosterFinder.findPoster(url);
		
		if(posterInfo.getPosterURL()==null)
		{
			System.err.println("No poster found in " + url);
		}
		else
		{
			System.out.println("Downloading poster from " + posterInfo.getPosterURL());
			URL posterURL = new URL(posterInfo.getPosterURL());
			downloadImage(posterURL, targetFile);
		}
		return posterInfo;
	}
	
	private void downloadImage(URL url, File targetFile) throws IOException
	{
		BufferedImage poster = ImageIO.read(url);
		if(poster == null)
		{
			System.err.println("Error reading image from " + url.toString());
		}
		else
		{
			try
			{
				ImageIO.write(poster, "png", targetFile);
				System.out.println("Poster has been written to " + targetFile.getAbsolutePath());
			}
			catch(Exception ex)
			{
				throw new IOException("Poster could not be written to " + targetFile.getAbsolutePath(), ex);
			}
		}
	}

	public static void downloadHtmlFile(URL contentURL)
			throws MalformedURLException, IOException
	{
		InputStream is = contentURL.openStream();
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
		
		try
		{
			PosterGrabber pg = new PosterGrabber();
			pg.downloadSmallPoster("wer früher stirbt ist länger tot", new File("c:/temp/bliblablub.png"));
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
