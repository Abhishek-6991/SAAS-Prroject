//package alti.training.product.accountspayable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class PdfRead{
	
	final static String userName = "abhishek.ganesh196@gmail.com";//change accordingly
	final static String password = "abisaiapuaks1996";//change accordingly
	
	public static void main(String args[]) throws IOException {
		 downloadAttachments();
		//Connection dbConnection = dbConnect("jdbc:mysql://localhost:3306/initiatives_db", "root", "");
		 //parsePdfAndStore(dbConnection);
		 //sendEmail();
	}
	
	private static Connection dbConnect(String db_connect_string, String db_userid, String db_password) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			// System.out.println("Connected to Database");
			return conn;
		} catch (Exception dbException) {
			System.out.println(dbException);
			System.out.println("Database Connection Failed");
			return null;
		}
	}
	
	private static void downloadAttachments(){
		  String pop3Host = "pop.gmail.com";//change accordingly
		  String mailStoreType = "pop3";	
		  ReceiveEmailWithAttachment download = new ReceiveEmailWithAttachment();
		  //call receiveEmail
		  download.receiveEmail(pop3Host, mailStoreType, userName, password);
	}
	
	/*private static void parsePdfAndStore(Connection dbConnection) throws IOException{ 
		ParsePdf parse = new ParsePdf();
	 	try{
	 		parse.readPdf(dbConnection);
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	}
	
	private static void sendEmail(){
		//SendEmailNotification sendMail = new SendEmailNotification();
		sendMail.send(userName, password, userName,"Accounts Payable","Your Invoice is approved.");
		//sendMail.sed("from","password","to", "Sub", "")
	}*/
	
	
}