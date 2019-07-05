import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Invoice 
{
  public void approveInvoice(Connection dbConnection,Scanner sc) throws SQLException
  {
	  String invoiceNo= "";
	  String unapproved= "unapproved";
	  Statement smt1=dbConnection.createStatement();
	  Statement smt2=dbConnection.createStatement();
	  Statement smt3=dbConnection.createStatement();
	  Statement smt4=dbConnection.createStatement();

	  //Statement smt3=dbConnection.createStatement();
	  String nullCheckQuery= "select * from invoice_details";
	  String invalidInvoiceCheckQuery= "select invoice_id from invoice_details";
	  String CheckQuery= "select * from invoice_details where status= '"+unapproved+"'";
	  ResultSet rs1=smt1.executeQuery(nullCheckQuery);
	  ResultSet rs2= smt2.executeQuery(CheckQuery);
	  ResultSet rs3= smt3.executeQuery(invalidInvoiceCheckQuery);
	  
	  boolean val1= rs1.next();
	  boolean val2= rs2.next();
	  //boolean val3= rs3.next();
	  if(val1 != false)
	  {
		  if(val2 != false)
			  displayInvoices(dbConnection);
		  else
		  {
			  System.out.println("There are no unapproved records!");
			  return;
		  }
	  }
	  else  
	  {
		  System.out.println("There are no records!!!");
		  return;
	  }
	  System.out.println("Enter the Invoice number of the invoice to be approved");
	  //if(sc.hasNext())
	    invoiceNo= sc.next();
	    String invoiceGroup= "";
	    if(rs3.next()){
	    do{
			invoiceGroup+= rs3.getString(1);
			}while(rs3.next());
	    }
	    
	    String checkApproval= "select status from invoice_details where invoice_id= '"+invoiceNo+"'";
		String approvalStatus= "";
		ResultSet rs4= smt2.executeQuery(checkApproval);
		if(rs4.next())
		  approvalStatus= rs4.getString(1);
		
	    if(invoiceGroup.contains(invoiceNo) && approvalStatus.equals("unapproved"))
	    {
	      try {			
			String query = "update invoice_details set status = ? where invoice_id = ?";
			PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
			preparedStmt.setString(1, "Approved");
			preparedStmt.setString(2, invoiceNo);
			preparedStmt.executeUpdate();
			System.out.println("Approved!!!");
			SendEmailNotification sendMail= new SendEmailNotification();
			sendMail.send("abhishek.ganesh196@gmail.com", "abisaiapuaks1996", "abhishek.ganesh196@gmail.com", "Invoice Approval", "Thank You!");
		} catch (Exception e) {
			System.out.println("Error Occured during approving invoice");
			e.printStackTrace();
			System.exit(0);
		}
	    finally{
	    	//sc.close();
	    }
	    }
	    else
	    {
	    	System.out.println("There are no unapproved invoices for the given Invoice number");
	    	return;
	    }
  }
  
  public void displayInvoices(Connection dbConnection) throws SQLException
  {
	    Statement smt=dbConnection.createStatement();
		String q="Select * from invoice_details";
		ResultSet rs=smt.executeQuery(q);

		if(rs.next()){ 
			do{
			System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6));
			}while(rs.next());
		}
		else{
			System.out.println("Record Not Found...");
		}
	  
  }
}
