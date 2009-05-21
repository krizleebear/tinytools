package tinytools.vexplorer.posters;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class PosterHandlerAmazonSmall extends PosterHandlerBase
{
	private static final String contentBaseUrl = "http://www.amazon.de/s/ref=nb_ss_w?__mk_de_DE=%C5M%C5Z%D5%D1&url=search-alias%3Ddvd&field-keywords=";
	
	public PosterHandlerAmazonSmall()
	{
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
	}

	public void endDocument() throws SAXException
	{
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException
	{
	}

	public void endPrefixMapping(String prefix) throws SAXException
	{
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
	}

	public void processingInstruction(String target, String data)
			throws SAXException
	{
	}

	public void setDocumentLocator(Locator locator)
	{
	}

	public void skippedEntity(String name) throws SAXException
	{
	}

	public void startDocument() throws SAXException
	{
	}
	
	boolean atCorrectTable = false;
	String smallImageUrl = null;
	String biggerImageHref = null;
	boolean finished = false;

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException
	{
		if(finished)
			return;
		
		if(localName == "td")
		{
//			System.out.println(atts.getValue("class"));
			String cssClass = atts.getValue("class");
			if(cssClass != null && cssClass.equalsIgnoreCase("imageColumn"))
			{
				atCorrectTable = true;
//				System.out.println(localName);
			}
		}
		
		if(atCorrectTable)
		{
			if(localName == "a")
			{
				this.biggerImageHref = atts.getValue("href");
			}
			if(localName == "img")
			{
				this.smallImageUrl = atts.getValue("src");
//				System.out.println(smallImageUrl);
				
				atCorrectTable = false;
				finished = true; //stick with the first poster found
			}
		}
		
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
	}

	public PosterResult findPoster(String movieName) throws IOException, SAXException
	{
		PosterResult result = new PosterResult();
		String escapedMovieName = escapeKeyword(movieName);
		
		URL htmlURL = new URL (contentBaseUrl + escapedMovieName + "&x=0&y=0");
		System.out.println("Searching for poster: " + htmlURL.toString());
		parseHtml(htmlURL);
		
		result.setPosterURL(smallImageUrl);
		result.setNextRessourceURL(biggerImageHref);
		return result;
	}
}
