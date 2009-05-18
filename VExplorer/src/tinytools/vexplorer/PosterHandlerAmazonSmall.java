package tinytools.vexplorer;

import java.awt.Image;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class PosterHandlerAmazonSmall implements IPosterHandler
{
	private static final String contentUrl = "http://www.amazon.de/s/ref=nb_ss_w?__mk_de_DE=%C5M%C5Z%D5%D1&url=search-alias%3Ddvd&field-keywords=";
//	private static final String contentUrl = "http://localhost:8080/s/ref=nb_ss_w?__mk_de_DE=%C5M%C5Z%D5%D1&url=search-alias%3Ddvd&field-keywords=";
	private String keyword;

	public PosterHandlerAmazonSmall(String keyword)
	{
		this.keyword = keyword;
	}
	
	public String getContentUrl()
	{
		return contentUrl + keyword + "&x=0&y=0";
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void endElement(String uri, String localName, String name)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void endPrefixMapping(String prefix) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void processingInstruction(String target, String data)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void setDocumentLocator(Locator locator)
	{
		// TODO Auto-generated method stub

	}

	public void skippedEntity(String name) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	public String getBiggerImageUrl()
	{
		return biggerImageHref;
	}

	public String getPosterUrl()
	{
		return smallImageUrl;
	}

}
