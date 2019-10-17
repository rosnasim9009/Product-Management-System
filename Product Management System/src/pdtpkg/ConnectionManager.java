package pdtpkg;

	import java.sql.DriverManager;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.sql.Connection;

	public class ConnectionManager {

		public static void main(String[] args) throws ClassNotFoundException, SQLException {

			Class.forName("com.mysql.jdbc.Driver");  
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/product","root","");  
			
			if (con != null)
			{
				System.out.println("Connected");
			}
			else
			{
				System.out.println("Not Connected");
			}
			
			Statement statement=con.createStatement();
			
		}

	}

