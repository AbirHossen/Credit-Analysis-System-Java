import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DatabaseConnection
{
	
		Connection conn = null;
		public static Connection dbConnector()
		{
		try
		{


			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn =DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe","usn","123");
			return conn;
	
		}
		
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"Cannot connect to database.");
			return null;
		}
		
	}
}
