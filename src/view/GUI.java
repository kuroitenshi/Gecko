package view;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GUI extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnNewButton;
	private JLabel lblSomething;
	
	
	public GUI() 
	{
		setTitle("Gecko");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("Test");
		btnNewButton.setBounds(10, 32, 99, 23);
		contentPane.add(btnNewButton);
		
		lblSomething = new JLabel("Something");
		lblSomething.setBounds(168, 36, 143, 14);
		contentPane.add(lblSomething);
	}
	
	/*Sample Action Listener for buttons*/
	public void setNewButtonActionListener(ActionListener l)
	{
		btnNewButton.addActionListener(l);
	
	}
	public JLabel getLblSomething() 
	{
		return lblSomething;
	}

	public void setLblSomething(String lblSomething) 
	{
		this.lblSomething.setText(lblSomething);
	}

}
