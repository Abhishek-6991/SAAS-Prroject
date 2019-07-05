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

//		System.out.println("1. Approve 2.Display 3.Exit");
//		System.out.println("Do You Want to Approve ? ");
//		int choice = sc.nextInt();
//		switch (choice) {
//		case 1:
//			updateStatus(dbConnection, invoiceNo);
//			break;
//		case 2:
//			Statement smt=dbConnection.createStatement();
//			String q="Select * from invoice_details where invoice_id= '"+invoiceNo+"'";
//			ResultSet rs=smt.executeQuery(q);
//	
//			if(rs.next()){ 
//				do{
//				System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6));
//				}while(rs.next());
//			}
//			else{
//				System.out.println("Record Not Found...");
//			}
//			break;
//		default:
//			System.out.println("Unapproved");
//			System.exit(0);
//		}
//  }
//  
//  private static void updateStatus(Connection dbConnection, String invoiceNumber) throws SQLException {
//		try {
//			System.out.println(invoiceNumber);
//			
//			String query = "update invoice_details set status = ? where invoice_id = ?";
//			PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
//			preparedStmt.setString(1, "Approved");
//			preparedStmt.setString(2, invoiceNumber);
//			preparedStmt.executeUpdate();
//			System.out.println("Approved!!!");
//			SendEmailNotification sendMail= new SendEmailNotification();
//			//System.out.println("usernameeee: "+userName+"pwdddd: "+password+"tooo:: "+to);
//			sendMail.send("abhishek.ganesh196@gmail.com", "abisaiapuaks1996", "abhishek.ganesh196@gmail.com", "Invoice Approval", "Thank You!");
//		} catch (Exception e) {
//			System.out.println("Error Occured during approving invoice");
//			e.printStackTrace();
//			System.exit(0);
//		}
	}
}
	  

