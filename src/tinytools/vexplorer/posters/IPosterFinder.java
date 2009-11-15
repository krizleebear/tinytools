package tinytools.vexplorer.posters;

import java.io.IOException;

import org.xml.sax.SAXException;

public interface IPosterFinder
{
	public PosterResult findPoster(String movieName) throws IOException, SAXException;
}
