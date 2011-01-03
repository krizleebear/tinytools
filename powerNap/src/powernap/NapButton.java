package powernap;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

class NapButton extends JButton
{
	private static final long serialVersionUID = 7639226803169238927L;

	public NapButton(String text)
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
