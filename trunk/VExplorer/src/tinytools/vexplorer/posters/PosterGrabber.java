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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import org.xml.sax.SAXException;

import tinytools.vexplorer.FileInfo;

public class PosterGrabber
{
	BlockingQueue<DownloadJob> downloadQueue = new LinkedBlockingQueue<DownloadJob>();
	Thread[] downloadThreads = new Thread[10];
	boolean allJobsDone = false;

	public PosterGrabber()
	{
		for (int i = 0; i < downloadThreads.length; i++)
		{
			downloadThreads[i] = new Thread(new DownloadThread(downloadQueue),
					"PosterDownloadThread-" + i);
			downloadThreads[i].start();
		}
	}

	public void waitUntilDone()
	{
		System.out.println("Waiting until download queue is empty...");
		while(downloadQueue.size()>0)
		{
			try
			{
				// workaround for advanced inter thread communication
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
		}
		System.out.println("Download queue seems to be empty.");
		allJobsDone = true;
		
		for (int i = 0; i < downloadThreads.length; i++)
		{
			downloadThreads[i].interrupt();
		}
	}

	public void downloadPosters(FileInfo currentVideo, String searchString)
			throws IOException, SAXException
	{
		DownloadJob job = new DownloadJob(currentVideo, searchString);
		try
		{
			downloadQueue.put(job);
			System.out.println("Added " + currentVideo.getDisplayedName()
					+ " to poster download queue with size="
					+ downloadQueue.size());
		}
		catch (InterruptedException e)
		{
		}
	}

	class DownloadJob
	{
		public DownloadJob(FileInfo video, String searchString)
		{
			this.video = video;
			this.searchString = searchString;
		}

		FileInfo video;
		String searchString;
	}

	class DownloadThread implements Runnable
	{
		private IPosterFinder smallPosterFinder = new PosterHandlerAmazonSmall();
		private IPosterFinder middlePosterFinder = new PosterHandlerAmazonMid();
		private BlockingQueue<DownloadJob> downloadQueue;
		public boolean running = true;
		
		public DownloadThread(BlockingQueue<DownloadJob> downloadQueue)
		{
			this.downloadQueue = downloadQueue;
		}

		public void run()
		{
			System.out.println(Thread.currentThread().getName() + " started.");

			while (running)
			{
				try
				{
					DownloadJob job = downloadQueue.take(); // blocks if queue is empty
					download(job);
				}
				catch (InterruptedException e)
				{
					System.out.println("Waiting at downloadQueue was interrupted...");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					if(allJobsDone)
						running = false;
				}
			}
		}

		private void download(DownloadJob job) throws IOException, SAXException
		{
			FileInfo currentVideo = job.video;
			String searchString = job.searchString;

			File smallPicFile = new File("./images/", currentVideo
					.getSmallPicFile());
			PosterResult smallPoster = downloadSmallPoster(searchString,
					smallPicFile);
			currentVideo.setSmallPoster(smallPoster);

			if (smallPoster != null)
			{
				File mediumPicFile = new File("./images/", currentVideo
						.getMediumPicFile());
				PosterResult mediumPoster = downloadMiddlePoster(smallPoster
						.getNextRessourceURL(), mediumPicFile);
				currentVideo.setMediumPoster(mediumPoster);

				// if(mediumPoster!=null &&
				// mediumPoster.getNextRessourceURL()!=null)
				// {
				// File largePicFile = new File("./images/",
				// currentVideo.getLargePicFile());
				// //PosterResult largePoster = grabber.do
				// }
			}
		}

		public PosterResult downloadSmallPoster(String movieName,
				File targetFile) throws IOException, SAXException
		{
			// does poster already exist on disk?
			if (targetFile.exists())
			{
				System.out.println("Poster for '" + movieName
						+ "' already found in " + targetFile.getAbsolutePath());
				return new PosterResult(); // default result, without URL
			}

			// search for poster on the web
			PosterResult posterInfo = smallPosterFinder.findPoster(movieName);
			if (posterInfo.getPosterURL() == null)
			{
				System.err.println("No poster found for " + movieName);
			}
			else
			{
				System.out.println("Downloading poster from "
						+ posterInfo.getPosterURL());
				URL smallPosterURL = new URL(posterInfo.getPosterURL());
				downloadImage(smallPosterURL, targetFile);
			}
			return posterInfo;
		}

		public PosterResult downloadMiddlePoster(String url, File targetFile)
				throws IOException, SAXException
		{
			// does poster already exist on disk?
			if (targetFile.exists())
			{
				System.out.println("Middle-sized poster already found in "
						+ targetFile.getAbsolutePath());
				return new PosterResult(); // default result, without URL
			}

			if (url == null)
				return null;

			PosterResult posterInfo = middlePosterFinder.findPoster(url);

			if (posterInfo.getPosterURL() == null)
			{
				System.err.println("No poster found in " + url);
			}
			else
			{
				System.out.println("Downloading poster from "
						+ posterInfo.getPosterURL());
				URL posterURL = new URL(posterInfo.getPosterURL());
				downloadImage(posterURL, targetFile);
			}
			return posterInfo;
		}

		private void downloadImage(URL url, File targetFile) throws IOException
		{
			BufferedImage poster = ImageIO.read(url);
			if (poster == null)
			{
				System.err
						.println("Error reading image from " + url.toString());
			}
			else
			{
				try
				{
					ImageIO.write(poster, "png", targetFile);
					System.out.println("Poster has been written to "
							+ targetFile.getAbsolutePath());
				}
				catch (Exception ex)
				{
					throw new IOException("Poster could not be written to "
							+ targetFile.getAbsolutePath(), ex);
				}
			}
		}

		/* for testing purposes */
		public void downloadHtmlFile(URL contentURL)
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
	}
}
