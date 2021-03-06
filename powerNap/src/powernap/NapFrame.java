package powernap;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NapFrame extends JFrame
{
	private static final long serialVersionUID = 1080928685148496207L;
	private static final String BACKGROUND_IMAGE = "background.png";
	
	private Container contentPane;
	private JPanel imgPanel;
	private JButton btStart;
	private JTextField tbHours;
	private JTextField tbMinutes;
	private JLayeredPane layers;
	private boolean running;

	public NapFrame(String string)
	{
		super(string);
		
		initOSX();
		
		initialize();

		addUI();

		pack();
	}

	private void initOSX()
	{
		if(Main.isMac())
		{
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0)
				{
					System.out.println("closing window means hiding on OSX...");
					setVisible(false);
				}
			});
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

	public void setRunning(boolean runningFlag)
	{
		this.running = runningFlag;
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run()
			{
				if(running)
				{
					URL url = this.getClass().getResource("powernap_button_running.gif");
			        Icon icon = new ImageIcon(url);
			        btStart.setIcon(icon);
				}
				else
				{
					URL url = this.getClass().getResource("powernap_button_start.gif");
			        Icon icon = new ImageIcon(url);
			        btStart.setIcon(icon);
				}
			}
		});
	}
	
	private void addUI()
	{
		layers = this.getLayeredPane();

		btStart = new JButton("");
		btStart.setSize(146, 38);
		btStart.setLocation(55, 182);
		btStart.setContentAreaFilled(false);
		btStart.setOpaque(false);
		btStart.setBorderPainted(false);
		
		URL url = this.getClass().getResource("powernap_button_start.gif");
        Icon icon = new ImageIcon(url);
        btStart.setIcon(icon);
		
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				buttonPressed();
			}
		});
		layers.add(btStart, JLayeredPane.PALETTE_LAYER);

		Font font = new Font("Verdana", Font.BOLD, 18);
		
		tbHours = new NapTextfield();
		tbHours.setSize(44, 36);
		tbHours.setLocation(58, 122);
		tbHours.setFont(font);
		tbHours.setText("0");
		
		tbHours.addKeyListener(textBoxKeyListener);
		layers.add(tbHours, JLayeredPane.PALETTE_LAYER);

		tbMinutes = new NapTextfield();
		tbMinutes.setSize(44, 36);
		tbMinutes.setLocation(153, 122);
		tbMinutes.setFont(font);
		tbMinutes.setText("0");
		tbMinutes.addKeyListener(textBoxKeyListener);
		layers.add(tbMinutes, JLayeredPane.PALETTE_LAYER);
	}
	
	private void buttonPressed()
	{
		if(running)
		{
			stopCountdown();
		}
		else
		{
			startCountdown();
		}
	}
	
	private KeyListener textBoxKeyListener = new KeyListener() 
	{
		public void keyPressed(KeyEvent arg0)
		{
		}
		public void keyReleased(KeyEvent arg0)
		{
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
			{
				startCountdown();
			}
		}
		public void keyTyped(KeyEvent arg0)
		{
		}
	};

	private void stopCountdown()
	{
		PowerNap.getInstance().stopCountdown();
		setRunning(false);
	}

	private void startCountdown()
	{
		try
		{
			int hours = Integer.parseInt(tbHours.getText());
			int minutes = Integer.parseInt(tbMinutes.getText());
			setRunning(true);
			PowerNap.getInstance().startCountdown(hours, minutes);
		}
		catch(NumberFormatException ex)
		{
			//ignore that exception. in future maybe color mark the input fields in some way
		}
	}

	public void setHoursAndMinutes(final int hours, final int minutes)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run()
			{
				tbHours.setText(Integer.toString(hours));
				tbMinutes.setText(Integer.toString(minutes));
			}
		});
	}

	public void setProgress(int percent, long remainingMillis)
	{
		int hours = (int) remainingMillis / 1000 / 3600;
		int remainingMinuteMillis = (int) (remainingMillis - hours*1000*3600);
		int minutes = remainingMinuteMillis/1000/60;
		
		setHoursAndMinutes(hours, minutes);
	}

	public void countdownTriggered()
	{
		setRunning(false);
	}
}
