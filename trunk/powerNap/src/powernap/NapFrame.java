package powernap;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import powernap.Countdown.CountdownListener;

public class NapFrame extends JFrame implements CountdownListener
{
	private static final long serialVersionUID = 1080928685148496207L;
	private JPanel imgPanel;
	private Container contentPane;
	Countdown countdown = new Countdown();

	public static final String BACKGROUND_IMAGE = "background.png";
	private JButton btStart;
	private JTextField tbHours;
	private JTextField tbMinutes;
	private JLayeredPane layers;

	public NapFrame(String string)
	{
		super(string);
		
		countdown.setCountdownListener(this);

		initialize();
		addUI();
		
		initOSX();

		this.pack();
	}

	private void initOSX()
	{
		if(Main.IS_MAC)
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", Main.APP_NAME);  
		}
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

		btStart = new NapButton("");
		btStart.setSize(146, 38);
		btStart.setLocation(55, 182);
		btStart.setContentAreaFilled(false);
		btStart.setOpaque(false);
		btStart.setBorderPainted(false);
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				int hours = Integer.parseInt(tbHours.getText());
				int minutes = Integer.parseInt(tbMinutes.getText());
				countdown.startCountdown(hours, minutes, 0);
				System.out.println("button pressed");
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

	public void countdownTriggered()
	{
		System.out.println("countdown triggered!");
		if(Main.IS_MAC)
		{
			try
			{
				System.out.println("sending this mac to sleep...");
				String[] cmd = { "osascript", "-e",	"tell application \"System Events\" to sleep" };

				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
				int returnCode = p.exitValue();
				System.out.println("return code: " + returnCode);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void remainingMillis(long remainingMillis)
	{
		System.out.println(remainingMillis/1000 + " seconds remaining");		
	}
}