package tinytools.vexplorer.posters;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class PosterHandlerAmazonMid extends PosterHandlerBase
{
	public PosterHandlerAmazonMid()
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
		finished = false;
	}
	
	boolean atCorrectTable = false;
	String midImageUrl = null;
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
			String id = atts.getValue("id");
			if(id != null && id.equalsIgnoreCase("prodImageCell"))
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
				this.midImageUrl = atts.getValue("src");
				
				atCorrectTable = false;
				finished = true; //stick with the first poster found
			}
		}
		
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
	}

	public PosterResult findPoster(String url) throws IOException, SAXException
	{
		midImageUrl = null;
		biggerImageHref = null;
		PosterResult result = new PosterResult();
		
		URL htmlURL = new URL (url);
		System.out.println("Searching for poster: " + htmlURL.toString());
		parseHtml(htmlURL);
		
		result.setPosterURL(midImageUrl);
		result.setNextRessourceURL(biggerImageHref);
		return result;
	}
}
