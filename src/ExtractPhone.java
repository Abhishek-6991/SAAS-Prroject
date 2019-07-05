import java.io.*;  
import org.apache.pdfbox.pdmodel.*;  
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.ArrayList;
import java.util.regex.*;  
  
public class ExtractPhone 
{  
  public static void main(String[] args)throws IOException 
  {  
    File fileName = new File("D:/Java/Project_One/Acushnet.pdf");  
    PDDocument doc = PDDocument.load(fileName);  
    ArrayList<String> invoices=new ArrayList<String>();
  
    StringBuilder sb = new StringBuilder();            
    PDFTextStripper stripper = new PDFTextStripper();  
  
 
    sb.append(stripper.getText(doc));  
  
    Pattern p = Pattern.compile("\\sInvoice No\\s\\n\\d\\d\\d\\d\\d\\d\\d\\d\\d\\s");    
    Matcher m = p.matcher(sb);  
    while (m.find())
      invoices.add(m.group().substring(11));
    
    System.out.print(invoices);
    if (doc != null)   
      doc.close();  
              
    System.out.println("\nInvoice Number is extracted");  
  }  
}