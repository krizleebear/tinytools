package tinytools.vexplorer.posters;

import java.io.IOException;
import java.net.URL;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class PosterHandlerBase implements IPosterFinder, ContentHandler
{
	private XMLReader htmlParser = new Parser();
	private HTMLSchema schema = new HTMLSchema();
	
	protected void parseHtml(URL contentUrl) throws IOException, SAXException
	{
		htmlParser.setProperty(Parser.schemaProperty, schema);
		htmlParser.setContentHandler(this);
		htmlParser.parse(contentUrl.toString());
	}
	
	public static final String escapeKeyword(String keyword)
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
			else if(c==' ')
			{
				sb.append('+');
			}
			else
			{
				sb.append(c);
			}
		}
		
		return sb.toString(); 
	}
}
