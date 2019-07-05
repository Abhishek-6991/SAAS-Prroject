//package alti.training.product.accountspayable;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class ParsePDF extends ReceiveEmailWithAttachment {
	protected void readPdf(Connection dbConnection, String fileName) throws IOException, SQLException {
		PDDocument document = null;
		Scanner sc = new Scanner(System.in);
		try {
			document = PDDocument.load(new File("D:\\Project_Target\\" + fileName));
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			//Rectangle orderNumber = new Rectangle(0, 150, 140, 10);
			Rectangle invoiceNumber = new Rectangle(0, 130, 140, 10);
			Rectangle invoiceDate = new Rectangle(100, 130, 140, 10);
			Rectangle customerPo = new Rectangle(250, 150, 180, 10);
			Rectangle address = new Rectangle(0, 170, 200, 50);
			Rectangle totalInvoice = new Rectangle(550, 400, 200, 50);
			
			//stripper.addRegion("Order Number", orderNumber);
			stripper.addRegion("Invoice Number", invoiceNumber);
			stripper.addRegion("Customer PO", customerPo);
			stripper.addRegion("Invoice Date", invoiceDate);
			stripper.addRegion("Sold To", address);
			stripper.addRegion("Total Invoice", totalInvoice);
			
			PDPage firstPage = document.getPage(0);
			stripper.extractRegions(firstPage);

			ArrayList<String> list = new ArrayList<String>();
			//list.add(stripper.getTextForRegion("Order Number"));
			list.add(stripper.getTextForRegion("Invoice Number"));
			list.add(stripper.getTextForRegion("Invoice Date"));
			list.add(stripper.getTextForRegion("Customer PO"));
			list.add(stripper.getTextForRegion("Sold To"));
			list.add(stripper.getTextForRegion("Total Invoice"));
			
			String invoiceNo = list.get(0);
		
			String query = " insert into invoice_details (invoice_id,"
					+ "invoice_date, customer_po, address, amount, status)" + " values (?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
			preparedStmt.setObject(1, list.get(0));
			preparedStmt.setObject(2, list.get(1));
			preparedStmt.setObject(3, list.get(2));
			preparedStmt.setObject(4, list.get(3));
			preparedStmt.setObject(5, list.get(4));
			preparedStmt.setString(6, "unapproved");
			preparedStmt.execute();

			System.out.println("1. Approve 2.Display 3.Exit");
			System.out.println("Do You Want to Approve ? ");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				updateStatus(dbConnection, invoiceNo);
				break;
			case 2:
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
				break;
			default:
				System.out.println("Unapproved");
				System.exit(0);
			}
		} catch (Exception pdfException) {
			System.out.println("Error Occured");
			System.out.println(pdfException);
			System.exit(0);

		} finally {
			if (document != null) {
				document.close();
			}
			sc.close();
			dbConnection.close();
		}
	}

	private static void updateStatus(Connection dbConnection, String invoiceNumber) throws SQLException {
		try {
			System.out.println(invoiceNumber);
			
			String query = "update invoice_details set status = ? where invoice_id = ?";
			PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
			preparedStmt.setString(1, "Approved");
			preparedStmt.setString(2, invoiceNumber);
			preparedStmt.executeUpdate();
			System.out.println("Approved!!!");
			SendEmailNotification sendMail= new SendEmailNotification();
			//System.out.println("usernameeee: "+userName+"pwdddd: "+password+"tooo:: "+to);
			sendMail.send("abhishek.ganesh196@gmail.com", "abisaiapuaks1996", "abhishek.ganesh196@gmail.com", "Invoice Approval", "Thank You!");
		} catch (Exception e) {
			System.out.println("Error Occured during approving invoice");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
