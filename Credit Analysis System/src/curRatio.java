/**
 * this class is about the details of bank statement!
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class curRatio extends JFrame{
	
	// declaration of Labels
	
	JLabel labelBankDetails = new JLabel("Bank Detail's");
	JLabel lblRatio = new JLabel("Banks Current Ratio   :");

	JLabel ratio = new JLabel("");

	JLabel labelDate = new JLabel("Date:");
	JButton btCancel = new JButton("Cancel");
	

	// declaration of calendar
	
	Calendar cal=new GregorianCalendar();
	Connection connection=null;

	/**
	 * here is the constructor and all the declarations 
	 * here the frame is fixed and not resizable
	 * the background image path is declared
	 */

	
	public curRatio() {
	
		
		setBounds(100, 100, 800, 800);
		
		setTitle("Bank Detail's");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);
		setContentPane(new JLabel(new ImageIcon("F:\\java\\Projects\\Credit Analysis System\\Images\\BankDetails.jpg")));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		

		
		labelBankDetails.setFont(new Font("Agency FB", Font.BOLD, 50));
		labelBankDetails.setBounds(525, 0, 328, 61);
		getContentPane().add(labelBankDetails);
		
		
		lblRatio.setFont(new Font("Agency FB", Font.BOLD, 30));
		lblRatio.setBounds(35, 223, 300, 49);
		getContentPane().add(lblRatio);
		
		
	
		
		ratio.setFont(new Font("Agency FB", Font.BOLD, 30));
		ratio.setBounds(334, 223, 300, 49);
		getContentPane().add(ratio);
		
		
		
		
		btCancel.setFont(new Font("Agency FB", Font.BOLD, 24));
		btCancel.setBounds(623, 658, 159, 36);
		getContentPane().add(btCancel);
		
		
		
		
		
		

		
	// button action	
		connection= DatabaseConnection.dbConnector();
		showCurrentRatio();
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				dispose();
				
				
			}
		});
	}
	
	public void showCurrentRatio()
	{
		try
		{
			
			connection = DatabaseConnection.dbConnector();
			String q1 = "Select sum(balance) from customer_info";
			PreparedStatement pst = (PreparedStatement) connection.prepareStatement(q1);
			ResultSet rs = pst.executeQuery();
			double sumBalance,sumLoan;
			if(rs.next()) {
				sumBalance = rs.getDouble("sum(balance)");
				
				try {
					String q2 = "Select sum(main_loan) from loan";
					PreparedStatement pst2 = (PreparedStatement) connection.prepareStatement(q2);
					ResultSet rs2 = pst2.executeQuery();
					if(rs2.next()) {
						sumLoan = rs2.getDouble("sum(main_loan)");
						double cR = sumBalance/sumLoan;
						String cRs = String.valueOf(cR);
						ratio.setText(cRs);
					}
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
			
		}
			
		catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,e);
				}
	}
	
	
	
	
	
}
