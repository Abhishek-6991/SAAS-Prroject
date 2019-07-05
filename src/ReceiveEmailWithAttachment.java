import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;
 
/**
 * This class is used to receive email with attachment.
 * @author codesjava
 */
public class ReceiveEmailWithAttachment 
{ 
  public static Scanner sc= new Scanner(System.in);
 public static String receiveEmail(String pop3Host,
    String mailStoreType, String userName, String password)
 { 
    //Set properties
    Properties props = new Properties();
    props.put("mail.store.protocol", "pop3");
    props.put("mail.pop3.host", pop3Host);
    props.put("mail.pop3.port", "995");
    props.put("mail.pop3.starttls.enable", "true");
 
    // Get the Session object.
    Session session = Session.getInstance(props);
 
    try {
        //Create the POP3 store object and connect to the pop store.
	Store store = session.getStore("pop3s");
	store.connect(pop3Host, userName, password);
 
	//Create the folder object and open it in your mailbox.
	Folder emailFolder = store.getFolder("INBOX");
	emailFolder.open(Folder.READ_ONLY);
 
	//Retrieve the messages from the folder object.
	
	SearchTerm searchCondition = new SearchTerm() {
		  
		  @Override public boolean match(Message message) 
		  { 
			  try 
			  { 
				  if(message.getSubject().equalsIgnoreCase("Invoice")) 
				  { return true; } 
			  } catch(MessagingException ex) 
			  { ex.printStackTrace(); } 
			  return false; }
		  };
		  
	//Message[] messages = emailFolder.getMessages();
	Message[] messages = emailFolder.search(searchCondition);
	System.out.println("Total Message" + messages.length);
 
	//Iterate the messages
	for (int i = 0; i < messages.length; i++) 
	{
	   Message message = messages[i];
	   Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
	     System.out.println("---------------------------------");  
	     System.out.println("Details of Email Message " 
                                                   + (i + 1) + " :");  
	     System.out.println("Subject: " + message.getSubject());  
	     System.out.println("From: " + message.getFrom()[0]);  
 
	     //Iterate recipients 
	     System.out.println("To: "); 
	     for(int j = 0; j < toAddress.length; j++){
	       System.out.println(toAddress[j].toString());
	     }
 
	     //Iterate multiparts
	     Multipart multipart = (Multipart) message.getContent();
	    
	   	 for(int k = 0; k < multipart.getCount(); k++)
	    	 {
	    		 BodyPart bodyPart = multipart.getBodyPart(k);  
	    		 if(bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) 
	    		 {
	    			 System.out.println("file name " +bodyPart.getFileName());
	    			 System.out.println("size " +bodyPart.getSize());
	    			 System.out.println("content type " +bodyPart.getContentType());
	    			 InputStream stream = (InputStream) bodyPart.getInputStream(); 
	    			 File targetFile = new File("D:/"+bodyPart.getFileName());
	    			 java.nio.file.Files.copy(
		    		   	  stream, 
		    		      targetFile.toPath(), 
		    		      StandardCopyOption.REPLACE_EXISTING);
		    		    //  IOUtils.closeQuietly(stream);
	    			 return "D:/"+bodyPart.getFileName();
	    		 }
//	    		 else
//	    		 {
//	    			 System.out.println("No Attachment Found!!!");
//	    			 System.exit(0);
//	    		 }
	    	 }
	     }
 
	   //close the folder and store objects
	   emailFolder.close(false);
	   store.close();
	} catch (NoSuchProviderException e) {
		e.printStackTrace();
	} catch (MessagingException e){
		e.printStackTrace();
	} catch (Exception e) {
	       e.printStackTrace();
	}
	return null;
 
    }
 
 private static Connection dbConnect(String db_connect_string, String db_userid, String db_password) 
 {
   Connection conn = null;
   try 
   {
     conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
	// System.out.println("Connected to Database");
	 return conn;
   } 
   catch (Exception dbException) 
   {
			System.out.println(dbException);
			System.out.println("Database Connection Failed");
			System.exit(0);
			return null;
    }
  }
 
 public static void main(String[] args) throws SQLException 
 {
	 // Scanner sc= new Scanner(System.in);
	  String pop3Host = "pop.gmail.com";//change accordingly
	  String mailStoreType = "pop3";	
	  Connection dbConnection = dbConnect("jdbc:mysql://localhost:3306/accounts_payable?serverTimezone=UTC", "root", "");
	  Invoice invoice= new Invoice();
	 
	  //call receiveEmail
	  String fileName= receiveEmail(pop3Host, mailStoreType, "xxxxxxxxxxxx@gmail.com", "xxxxxxxx");
	  //ParsePDF parse= new ParsePDF();
	  if(fileName != null)
	  {
	    NewPDFParser parser= new NewPDFParser();
	    try {
		    //System.out.println("from: "+from+" to: "+to);
			//parse.readPdf(dbConnection, fileName);
		      parser.parseIt(dbConnection, fileName);
		      System.out.println("Successfully stored in DB");
		  } catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	  }
	  
	  int n= 0;
	  while(true)
	  {
		System.out.println("Enter your choice\n1. Approve an Invoice 2.Display Invoices 3.Exit");
		//if(sc.hasNextInt())
		n= sc.nextInt();
		switch(n)
		{
		  case 1:
			  System.out.println("Approve Invoice");
			  invoice.approveInvoice(dbConnection,sc);
			  break;
		  case 2:
			  System.out.println("Display Invoices");
			  invoice.displayInvoices(dbConnection);
			  break;
		  case 3:
			  System.out.println("Exit");
			  System.exit(0);
		  default:
			  System.out.println("Invalid choice");
			  break;
		}
		  
	  }
 }
}