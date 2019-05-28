package com.hello;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.io.IOException;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
	private static final byte[] USER = "Hello".getBytes();
	private static final byte[] OWNER = "World".getBytes();
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) throws IOException, java.io.IOException {
      
    	
    	    final String RESULT1
    	            = "./target/Listing_12_09_EncryptionPdf_encryption.pdf";
    	    final String RESULT2
    	            = "./target/Listing_12_09_EncryptionPdf_encryption_decrypted.pdf";
    	    final String RESULT3
    	            = "./target/Listing_12_09_EncryptionPdf_encryption_encrypted.pdf";

    createPdf(RESULT1);  
    decryptPdf(RESULT1, RESULT2);
    encryptPdf(RESULT2, RESULT3);

    	    
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    public void createPdf(String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setStandardEncryption(USER, OWNER, EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.STANDARD_ENCRYPTION_128));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Hello World"));
        doc.close();
    }
    
    public void decryptPdf(String src, String dest) throws IOException, java.io.IOException {
        PdfReader reader = new PdfReader(src, new ReaderProperties().setPassword(OWNER));
        PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(dest));
        pdfDoc.close();
        reader.close();
    }

    public void encryptPdf(String src, String dest) throws IOException, java.io.IOException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties()
                .setStandardEncryption(USER, OWNER,
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), writer);
        pdfDoc.close();
        writer.close();
    }
    
}
