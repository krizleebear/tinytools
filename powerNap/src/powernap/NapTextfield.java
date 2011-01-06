package powernap;

import java.awt.Color;

import javax.swing.JTextField;

public 	class NapTextfield extends JTextField
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
