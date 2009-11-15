package tinytools.vexplorer.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class AboutPanel extends JEditorPane implements HyperlinkListener
{
	private static final long serialVersionUID = -3999836340511463860L;

	public AboutPanel()
	{
		this.setEditable(false);
		this.addHyperlinkListener(this);

		try
		{
			this.setPage("file:About.html");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			if (Desktop.isDesktopSupported())
			{
				Desktop desktop = Desktop.getDesktop();
				try
				{
					desktop.browse(new URI(e.getURL().toString()));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
