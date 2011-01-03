package powernap;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NapFrame extends JFrame
{
	private JPanel imgPanel;
	private Container contentPane;
	public static final String BACKGROUND_IMAGE = "background.png";
	
	
	public NapFrame(String string)
	{
		super(string);
		initialize();
		
		this.pack();
	}

	private void initialize()
	{
//		com.apple.eawt

		this.setBackground(Color.BLACK);
		
		URL url = this.getClass().getResource(BACKGROUND_IMAGE);
		final ImageIcon icon = new ImageIcon(url);
		imgPanel = new JPanel()
		{
			private static final long serialVersionUID = 1608723740944835810L;
			public void paintComponent(Graphics g) 
            {
				Image image = icon.getImage();
                Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
                setSize(size);
                setLayout(null);
                g.drawImage(image, 0, 0, null);
            } 
		};
		
		contentPane = getContentPane();
		contentPane.add(imgPanel);
		imgPanel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
		
		
	}

	private static final long serialVersionUID = 1080928685148496207L;
}
