import java.io.*;  
import org.apache.pdfbox.pdmodel.*;  
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.ArrayList;
import java.util.regex.*;  
  
public class ExtractPhoneBackup
{  
  public static void main(String[] args)throws IOException 
  {  
    // PDF file from the phone numbers are extracted  
    File fileName = new File("D:/Java/Project_One/Acushnet.pdf");  
    PDDocument doc = PDDocument.load(fileName);  
    ArrayList<String> invoices=new ArrayList<String>();
    // StringBuilder to store the extracted text  
    StringBuilder sb = new StringBuilder();            
    PDFTextStripper stripper = new PDFTextStripper();  
  
    // Add text to the StringBuilder from the PDF 
    sb.append(stripper.getText(doc));  
  
    // Regex-> The Pattern refers to the format you are looking for. In our example,we are looking for   
    //numbers with 10 digits with atleast one surrounding white spaces on both ends.  
    Pattern p = Pattern.compile("\\sInvoice No\\s\\n\\d\\d\\d\\d\\d\\d\\d\\d\\d\\s");  
  
    // Matcher refers to the actual text where the pattern will be found  
           Matcher m = p.matcher(sb);  
    while (m.find())
    {  
    //group() method refers to the next number that follows the pattern we have specified.  
               //System.out.println(m.group());
               invoices.add(m.group().substring(11));
      }  
      System.out.print(invoices.get(0));
      if (doc != null) {  
            doc.close();  
               }  
               System.out.println("\nInvoice Number is extracted");  
        }  
}