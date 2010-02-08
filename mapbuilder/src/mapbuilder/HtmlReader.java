package mapbuilder;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlReader extends DefaultHandler
{
	private XMLReader htmlParser = new Parser();
	private HTMLSchema schema = new HTMLSchema();

	List<Tile> tiles = new LinkedList<Tile>();
	private File file = null;
	private int tileContainerMarker = -1;

	public HtmlReader(File f)
	{
		this.file = f;
	}

	public static void main(String args[]) throws Exception
	{
		if(args.length < 1)
		{
			System.err.println("Provide the path to the HTML file to parse.");
			System.exit(-1);
		}
		
		File f = new File(args[0]);
		HtmlReader reader = new HtmlReader(f);
		reader.parse();
	}

	public void parse() throws Exception
	{
		URI uri = file.toURI();
		URL url = uri.toURL();
		parseHtml(url);

		handleTiles();
	}

	protected void parseHtml(URL contentUrl) throws IOException, SAXException
	{
		htmlParser.setProperty(Parser.schemaProperty, schema);
		htmlParser.setContentHandler(this);
		htmlParser.parse(contentUrl.toString());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException
	{
		if (localName.equals("div"))
		{
			String id = atts.getValue("", "id");
			if (id != null && id.equalsIgnoreCase("tileContainer"))
			{
				// debug("tileContainer reached!");
				tileContainerMarker = 0;
			}
		}
		if (tileContainerMarker >= 0)
		{
			// debug(tileContainerMarker);
			tileContainerMarker++;

			if (localName.equals("img"))
			{
				String src = atts.getValue("", "src");
				if (src != null)
				{
					if (src.endsWith(".jpg")) // map images are jpeg
					{
						handleImage(atts);
						// System.out.println(src);
					}
				}
			}
		}
	}

	private void debug(String msg)
	{
		System.out.println(msg);
	}

	private void handleImage(Attributes atts)
	{
		int left = 0, top = 0, width = -1, height = -1;
		String src = null;
		String style = atts.getValue("", "style");
		if (style != null)
		{
			/* remove all whitespace characters for easier string handling */
			style = style.replace(" ", "");

			left = getStyleValue(style, "left");
			top = getStyleValue(style, "top");
			width = getStyleValue(style, "width");
			height = getStyleValue(style, "height");
		}

		src = atts.getValue("", "src");

		tiles.add(new Tile(src, top, left, width, height));
	}

	private int getStyleValue(String styleString, String styleAttributeName)
	{
		/* e.g. style="width:20px;left:100px;" */
		styleAttributeName = styleAttributeName.trim();
		int attributeStart = styleString.indexOf(styleAttributeName); // e.g.
																		// "left"
		if (attributeStart < 0)
			return -1;

		int attributeEnd = styleString.indexOf("px;", attributeStart);
		String attributeString = styleString.substring(attributeStart
				+ styleAttributeName.length() + 1, attributeEnd); // +1 for the
																	// :

		return Integer.parseInt(attributeString);
	}

	class Tile
	{
		int top, left, width, height;
		String src;

		public Tile(String src, int top, int left, int width, int height)
		{
			this.src = src;
			this.top = top;
			this.left = left;
			this.width = width;
			this.height = height;
		}

		public String toString()
		{
			return src + " " + left + "|" + top + " (" + width + ")";
		}
	}

	void handleTiles()
	{
		int minLeft = Integer.MAX_VALUE, minTop = Integer.MAX_VALUE;
		int maxLeft = Integer.MIN_VALUE, maxTop = Integer.MIN_VALUE;
		int maxWidth = Integer.MIN_VALUE, maxHeight = Integer.MIN_VALUE;

		for (Tile tile : tiles)
		{
			minLeft = Math.min(minLeft, tile.left);
			maxLeft = Math.max(maxLeft, tile.left);
			minTop = Math.min(minTop, tile.top);
			maxTop = Math.max(maxTop, tile.top);
			maxWidth = Math.max(maxWidth, tile.width);
			maxHeight = Math.max(maxHeight, tile.height);
			// debug(tile);
		}
		for (Tile tile : tiles)
		{
			tile.left -= minLeft;
			tile.top -= minTop;
			// debug(tile.toString());
		}

		int	combinedWidth = maxLeft + maxWidth - minLeft;
		int combinedHeight = maxTop + maxHeight - minTop;

//		debug(combinedWidth + " x " + combinedHeight);

		writeFile(combinedWidth, combinedHeight);
	}

	private void writeFile(int width, int height)
	{
		String htmlPath = this.file.getParent();

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();

		for (Tile tile : tiles)
		{
			try
			{
				String tileSrc = URLDecoder.decode(tile.src);
				File tileFile = new File(htmlPath, tileSrc);
				BufferedImage tileImage = ImageIO.read(tileFile);
				g.drawImage(tileImage, tile.left, tile.top, null);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			ImageIO.write(image, "png", new File(htmlPath, "mapbuilder.png") );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		g.dispose();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		if (tileContainerMarker >= 0)
		{
			tileContainerMarker--;
		}
	}

}
