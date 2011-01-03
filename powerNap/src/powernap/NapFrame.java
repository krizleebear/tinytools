package powernap;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NapFrame extends JFrame
{
	private static final long serialVersionUID = 1080928685148496207L;
	private JPanel imgPanel;
	private Container contentPane;

	public static final String BACKGROUND_IMAGE = "background.png";
	private JButton btStart;
	private JTextField tbHours;
	private JTextField tbMinutes;
	private JLayeredPane layers;

	class TransparentButton extends JButton
	{
		private static final long serialVersionUID = 7639226803169238927L;

		public TransparentButton(String text)
		{
			super(text);
			setOpaque(false);
		}

		public void paint(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.1f));
			super.paint(g2);
			g2.dispose();
		}
	}

	public NapFrame(String string)
	{
		super(string);

		initialize();
		addUI();

		this.pack();
	}

	private void initialize()
	{
		this.setBackground(Color.BLACK);

		URL url = this.getClass().getResource(BACKGROUND_IMAGE);
		final ImageIcon icon = new ImageIcon(url);
		imgPanel = new JPanel() {
			private static final long serialVersionUID = 1608723740944835810L;

			public void paintComponent(Graphics g)
			{
				Image image = icon.getImage();
				Dimension size = new Dimension(image.getWidth(null), image
						.getHeight(null));
				setPreferredSize(size);
				setMinimumSize(size);
				setMaximumSize(size);
				setSize(size);
				setLayout(null);
				g.drawImage(image, 0, 0, null);
			}
		};

		contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.add(imgPanel);
		imgPanel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
	}

	private void addUI()
	{
		layers = this.getLayeredPane();

		btStart = new TransparentButton("");
		btStart.setSize(146, 38);
		btStart.setLocation(55, 182);
		btStart.setContentAreaFilled(false);
		btStart.setOpaque(false);
		btStart.setBorderPainted(false);
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("button pressed");
				System.out.println(tbHours.getText());
			}
		});
		layers.add(btStart, JLayeredPane.PALETTE_LAYER);

		tbHours = new JTextField();
		tbHours.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		tbHours.setForeground(Color.WHITE);
		tbHours.setOpaque(false);
		tbHours.setSize(50, 36);
		tbHours.setLocation(55, 122);
		layers.add(tbHours, JLayeredPane.PALETTE_LAYER);

		tbMinutes = new JTextField();
		tbMinutes.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		tbMinutes.setForeground(Color.WHITE);
		tbMinutes.setOpaque(false);
		tbMinutes.setSize(50, 36);
		tbMinutes.setLocation(150, 122);
		layers.add(tbMinutes, JLayeredPane.PALETTE_LAYER);
	}
}
