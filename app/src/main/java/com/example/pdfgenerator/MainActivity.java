package com.example.pdfgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.itextpdf.text.Font.NORMAL;

public class MainActivity extends AppCompatActivity {
    private Button btnCreatePdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreatePdf = findViewById(R.id.btn_create_pdf);

        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btnCreatePdf.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPDFFile(Common.getAppPath(MainActivity.this) + "test.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                });

    }

    private void createPDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try{
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //Open to write
            document.open();
            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Bukenya Lukman");
            document.addCreator("Bukenya Lukman C");

            //Font size
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("font/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

            Font titleFont = new Font(fontName, 36.0f, NORMAL,BaseColor.BLACK);
            addNewitem(document, "Order Details", Element.ALIGN_CENTER,titleFont);

            // Add more
            Font orderNumberFont = new Font(fontName,fontSize, Font.NORMAL,colorAccent);
            addNewitem(document, "Order No",Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont = new Font(fontName,valueFontSize, Font.NORMAL,BaseColor.BLACK);
            addNewitem(document, "#717171",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewitem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont);
            addNewitem(document,"14/06/2020",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);

            addNewitem(document,"Account Name",Element.ALIGN_LEFT,orderNumberFont);
            addNewitem(document,"Bukenya Lukman",Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document);
            addLineSpace(document);
            addNewitem(document,"Product Details",Element.ALIGN_CENTER,titleFont);
            addLineSeperator(document);


            //Item
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont);

            addLineSeperator(document);

            //Item 2
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont);

            addLineSeperator(document);


            //Total 1
            addLineSpace(document);
            addLineSpace(document);
            addNewItemWithLeftAndRight(document,"Total","24000.0",titleFont,orderNumberValueFont);

            document.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            printPDF();



        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PDFDocumentAdapter(MainActivity.this, Common.getAppPath(MainActivity.this)+ "test_pdf.pdf" );
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
        }catch(Exception ignored){

        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textRightFont,Font textLeftFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft,textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight,textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
        
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewitem(Document document, String text, int align,Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}