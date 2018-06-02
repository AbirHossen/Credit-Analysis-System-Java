import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.sql.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LoanVerific extends JFrame {

	JPanel contentPane;
	JLabel lblAccId = new JLabel("Enter Account No:");
	JTextField txtAccId = new JTextField();
	JLabel lblAmount = new JLabel("Enter Mortgage Amount:");
	JTextField txtAmount = new JTextField();
	JButton btnAnalyse = new JButton("Analyse");
	JButton btnClose = new JButton("Close");
	JLabel showDate = new JLabel ();
	
	Calendar cal =new GregorianCalendar ();
	
	Connection connection=null;
	
	String date;

		
	public LoanVerific() { 
		super("Loan Verification");
		setSize(650,560);
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setContentPane(new JLabel(new ImageIcon("F:\\java\\Projects\\Credit Analysis System\\Images\\LoanForm.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		
		
		
		lblAccId.setFont(new Font("Agency FB", Font.BOLD, 24));
		lblAccId.setBounds(74, 185, 144, 41);
		getContentPane().add(lblAccId);
		
		lblAmount.setFont(new Font("Agency FB",Font.BOLD, 24));
		lblAmount.setBounds(74, 285, 144, 41);
		getContentPane().add(lblAmount);
		
		txtAmount.setFont(new Font("Agency FB", Font.PLAIN, 24));
		txtAmount.setBounds(233, 285, 228, 36);
		getContentPane().add(txtAmount);
		txtAmount.setColumns(10);
		
		
		
		txtAccId.setFont(new Font("Agency FB", Font.PLAIN, 24));
		txtAccId.setBounds(233, 188, 228, 36);
		getContentPane().add(txtAccId);
		txtAccId.setColumns(10);
		

		btnClose.setFont(new Font("Agency FB", Font.BOLD, 24));
		btnClose.setBounds(364, 350, 97, 41);
		getContentPane().add(btnClose);
		

		btnAnalyse.setFont(new Font("Agency FB", Font.BOLD, 24));
		btnAnalyse.setBounds(233, 350, 109, 41);
		getContentPane().add(btnAnalyse);
		
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		month+=1;
		
		
		
		showDate.setFont(new Font("Agency FB", Font.BOLD, 30));
		showDate.setBounds(350,20, 120, 100);		
		getContentPane().add(showDate);
		showDate.setText(""+day+"/"+month+"/"+year);


		connection= DatabaseConnection.dbConnector();
		
		
		
		
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtAccId.getText().isEmpty() || txtAmount.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Enter Both ID and Amount!");
				}
				else {

					analyse();
				}
				
			}
		});
		
		
		
		
		
		
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		

	}
	
	public void analyse() {
		try
		{ 	//checks if account exist and has a pending request
			connection= DatabaseConnection.dbConnector();
			String query ="select * from Loan where Loan_Status=? and Account_No=?";
			PreparedStatement pst = (PreparedStatement) connection.prepareStatement(query);
			pst.setString(2, txtAccId.getText());
			pst.setString(1, "Pending");
			ResultSet rs=pst.executeQuery(); 

			int count =0;
			while(rs.next())
				{
					count=count+1; 
				}
			if(count==1)
				{
					
					checkTransaction();
				}
			
			else
			{
				JOptionPane.showMessageDialog(null,"No request has been made for this account");
			}
		}	
	
		catch(Exception e3)
		{
			JOptionPane.showMessageDialog(null,e3);
		}
	}
	
	
	
	public void checkTransaction() {
		try {
			//checks last 30 days transaction 
			connection= DatabaseConnection.dbConnector();
			String q1 = "Select * from transaction where Account_No=? and cdate > sysdate-30";
			PreparedStatement pst = (PreparedStatement) connection.prepareStatement(q1);
			pst.setString(1,txtAccId.getText());
			ResultSet rst =  pst.executeQuery();
			int count = 0;
			while(rst.next()) {
				count += 1;
			} 
			if(count>=8) {
				//finding ratio
				ratio();
				
				
				
				
			}
			else {
				JOptionPane.showMessageDialog(null, "User does not match required cash flow needed!");
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		
		
		
	}
	
	
	
	
	public void ratio() {
		try {//query for main balance
			String q2 = "Select * from customer_info where account_no='"+txtAccId.getText()+"'";
			PreparedStatement pst2 = (PreparedStatement) connection.prepareStatement(q2);
			ResultSet rst2 = pst2.executeQuery();
			if(rst2.next()) {

				double mBalance = rst2.getDouble("Balance");
				double mortgage = Double.parseDouble(txtAmount.getText());
				//query for loan amount
				try {

					String q3 = "Select * from loan where account_no=? and loan_status=?";
					PreparedStatement pst3 = (PreparedStatement) connection.prepareStatement(q3);
					pst3.setString(1, txtAccId.getText());
					pst3.setString(2, "Pending");
					ResultSet rst3 = pst3.executeQuery();
					
					if(rst3.next()) {
						
						
						double lAmount = rst3.getDouble("MAIN_LOAN");
						double rat = (mBalance+mortgage)/lAmount;
						JOptionPane.showMessageDialog(null, "Acid Test Result is: "+ String.format("%.2f", rat));
					}
					else {
						JOptionPane.showMessageDialog(null,"This is loan fetching scope pending status");
					}
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Something wrong!");
				}
				
			}
			else {
				JOptionPane.showMessageDialog(null, "Something Wrong! Try again!");
			}
			
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		
		
	}
	
	
	
	

	public static void main(String[] args) {
		
			
				try {
					LoanVerific frame = new LoanVerific();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
	
	 
	
}
