import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class NewPDFParser 
{
	public void parseIt(Connection dbConnection, String fileName) throws Exception, IOException 
	{
	  PopulateDatabase populateDB= new PopulateDatabase();
	  String invoiceNo= "";
	  String invoiceDate= "";
	  String customerPo= "";
	  String address=  "";
	  String amount= "";
	  
	  File file = new File(fileName); 
	  PDDocument document = PDDocument.load(file);
	  PDFTextStripper pdfStripper = new PDFTextStripper();
	   
	  int pageCounter= 1;  
	  int invoiceCount= 1;
	  //for(int j= 0; j<13;j++)
	  while(pageCounter < 13)
	  {
		pdfStripper.setStartPage(pageCounter);
		pdfStripper.setEndPage(pageCounter);
		String pages = pdfStripper.getText(document);
		if(pages.contains("Invoice No") && pages.contains("Net Order Total"))
		{
			String[] lines = pages.split("\r\n|\r|\n");
			System.out.println("------------------INVOICE "+invoiceCount+"----------------------");
			invoiceCount++;
			for(int i= 0; i < lines.length;i++)
			{
				switch(lines[i])
				{
	    	    	case "Invoice No":
	    		  //System.out.println("Invoice Number: "+lines[++i]);
	    	    		invoiceNo= lines[++i];
	    	    		break;
	    	      
	    	    	case "Invoice Date":
		    		  //System.out.println("Invoice Date: "+lines[++i]);
	    	    		invoiceDate= lines[++i];
	    	    		break;
		    	      
	    	    	case "Customer P.O.":
		    		  //System.out.println("Customer P.O.: "+lines[++i]);
	    	    		customerPo= lines[++i];
	    	    		break;
		    	      
	    	    	case "Sold To":
//		    		  System.out.println("Address: "+lines[++i]+" ");
//		    		  System.out.print(lines[++i]);
//		    		  System.out.println(lines[++i]);
		    		  address= lines[++i]+" "+lines[++i]+" "+lines[++i];
		    	      break;
		    	      
	    	    	case "Total Invoice":
	    	    	  if(invoiceCount == 4)
	    	    	    i= i+3;
	    	    	  else
	    	    	    i= i+2;
		    		  //System.out.println("Total Amount: "+lines[++i]);
	    	    	  amount= lines[++i];
		    	      break;
		    	      
	    	    	case "CREDIT":
		    		  //System.out.println("Total Amount: "+lines[++i]);
	    	    		amount= lines[++i];
	    	    		break;
				}
			}
	    //
	    //System.out.println("\ninvoice num: "+invoiceNo+"\ninvoice date: "
	    					//+invoiceDate+"\ncustomer po: "+customerPo
	    					//+"\naddress: "+address+"\namount: "+amount);
	    System.out.println("Calling DB store");
	    populateDB.storeValuesInDatabase(dbConnection, invoiceNo,invoiceDate,customerPo,address,amount);
	    //Thread.sleep(3000);
	    }
		pageCounter++;
	  }
	}
}
