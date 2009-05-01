package tinytools.vexplorer;

import java.awt.Image;

import org.xml.sax.ContentHandler;

public interface IPosterHandler extends ContentHandler
{
	/**
	 * Returns the url that should be opened for providing the content
	 * @return
	 */
	public String getContentUrl();
	
	public String getPosterUrl();
	
	public String getBiggerImageUrl();
}
