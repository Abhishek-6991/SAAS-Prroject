import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PopulateDatabase 
{
  public void storeValuesInDatabase(Connection dbConnection,String invoiceNumber,
		  String invoiceDate,String customerPo,String address,String amount) throws SQLException
  {
	  Scanner sc= new Scanner(System.in);
	  String invoiceNo = invoiceNumber;
		
		String query = " insert into invoice_details (invoice_id,"
				+ "invoice_date, customer_po, address, amount, status)" + " values (?, ?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
		preparedStmt.setObject(1, invoiceNumber);
		preparedStmt.setObject(2, invoiceDate);
		preparedStmt.setObject(3, customerPo);
		preparedStmt.setObject(4, address);
		preparedStmt.setObject(5, amount);
		preparedStmt.setString(6, "unapproved");
		preparedStmt.execute();
	}
}
	  

