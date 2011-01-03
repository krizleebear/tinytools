package powernap;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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

		Font font = new Font("Verdana", Font.BOLD, 18);
		
		tbHours = new NapTextfield();
		tbHours.setSize(44, 36);
		tbHours.setLocation(58, 122);
		tbHours.setFont(font);
		tbHours.setText("0");
		layers.add(tbHours, JLayeredPane.PALETTE_LAYER);

		tbMinutes = new NapTextfield();
		tbMinutes.setSize(44, 36);
		tbMinutes.setLocation(153, 122);
		tbMinutes.setFont(font);
		tbMinutes.setText("0");
		layers.add(tbMinutes, JLayeredPane.PALETTE_LAYER);
	}
	
	class NapTextfield extends JTextField
	{
		private static final long serialVersionUID = 2797874554619229084L;

		public NapTextfield()
		{
			super();
			setBorder(javax.swing.BorderFactory.createEmptyBorder());
			setForeground(Color.WHITE);
			setOpaque(false);
			setHorizontalAlignment(JTextField.CENTER);
		}
	}
}
