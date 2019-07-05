import java.io.File;  
import java.io.IOException;  
  
import java.io.File;  
import java.io.IOException;  
  
import org.apache.pdfbox.pdmodel.PDDocument;  
import org.apache.pdfbox.text.PDFTextStripper;  
  
public class PDFReader {  
      
    public static void main(String[] args)throws IOException {  
          
        //Loading an existing document  
          File file = new File("D:/Java/Project_One/Acushnet.pdf");  
          PDDocument doc = PDDocument.load(file);  
      
    //Instantiate PDFTextStripper class  
          PDFTextStripper pdfStripper = new PDFTextStripper();  
  
    //Retrieving text from PDF document  
          String text = pdfStripper.getText(doc);  
          System.out.println("Text in PDF\n---------------------------------");  
          System.out.println(text);  
  
    //Closing the document  
    doc.close();  
    }  
} 