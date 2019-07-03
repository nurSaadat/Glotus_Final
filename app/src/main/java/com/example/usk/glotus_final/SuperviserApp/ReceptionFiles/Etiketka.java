package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.Other;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class Etiketka extends AppCompatActivity{
    private MenuItem btn_next;
    private PdfData data;
    private LinearLayout firstPageLayout;
    private LinearLayout firstPartLayout;
    private LinearLayout secondPartLayout;
    private TextView mesto;
    private int kolvoMest;
    private PrintedPdfDocument document;

    RelativeLayout relativeLayout;
    Button send;
    File imageFile;

    private static String formattedDate;

    private final static String FONT="/assets/fonts/PTC55F.ttf";
    private final static String DESTFILE="EtiketkaFile.pdf";
    private final static String DESTFILE1="EtiketkaFile1.pdf";
    private final static String DESTFILE2="EtiketkaFile2.pdf";
    private final static String DESTFILE3="EtiketkaFile3.pdf";
    private final static String DESTFILE4="EtiketkaFile4.pdf";


    private File destFile;
    private static File destFile1;
    private File destFile2;
    private File destFile3;
    private File destFile4;

    private float pdfWidth;
    private float pdfHeight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiketka);

        Intent intent=getIntent();
        data=(PdfData) intent.getExtras().getSerializable("pdfData");
        buildText(data);
        kolvoMest=Integer.parseInt(data.getKolvoMest());
        setDateFormat();

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createPDF();
                printDocument(imageFile,kolvoMest+1);
                //createPDF();
                //printDocument(imageFile,kolvoMest+1);
//                try {
//                    fillPdfLayer1();
//                    fillPdfLayer2();
//                    //lol();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                }
//
//                printDocument(destFile1,kolvoMest+1);
            }
        });
    }

    public void buildText(PdfData pd){
        TextView gorodOtkuda=findViewById(R.id.gorod_otkuda);
        TextView gorodKuda=findViewById(R.id.gorod_kuda);
        TextView imya_otprav=findViewById(R.id.imya_otprav);
        TextView imya_poluch=findViewById(R.id.imya_poluch);
        TextView kolichMest=findViewById(R.id.kolich);
        TextView ves=findViewById(R.id.ves);
        TextView obiem=findViewById(R.id.obiem);
        TextView raspechatal=findViewById(R.id.admin);
        TextView idAndDate=findViewById(R.id.date);
        TextView gorodOtkuda1=findViewById(R.id.gorod_otkuda1);
        TextView gorodKuda1=findViewById(R.id.gorod_kuda1);
        TextView imya_otprav1=findViewById(R.id.imya_otprav1);
        TextView imya_poluch1=findViewById(R.id.imya_poluch1);
        mesto=findViewById(R.id.mesto);
        TextView raspechatal1=findViewById(R.id.admin1);
        TextView idAndDate1=findViewById(R.id.date1);

        relativeLayout=findViewById(R.id.relative1);
        firstPageLayout=findViewById(R.id.mainLay);
        firstPartLayout=findViewById(R.id.linLay);
        secondPartLayout=findViewById(R.id.linLay1);
        send = findViewById(R.id.send);

        gorodOtkuda.setText(pd.getFromCity());
        gorodKuda.setText(pd.getToCity());
        imya_otprav.setText(pd.getOtpravitel());
        imya_otprav1.setText(pd.getOtpravitel());
        imya_poluch.setText(pd.getPoluchatel());
        imya_poluch1.setText(pd.getPoluchatel());
        kolichMest.setText(pd.getKolvoMest());
        ves.setText(pd.getVes());
        obiem.setText(pd.getObiem());
        raspechatal.setText(Admin.name);
        idAndDate.setText(pd.getNumZakaz()+" от "+formattedDate);
        gorodOtkuda1.setText(pd.getFromCity());
        gorodKuda1.setText(pd.getToCity());
        mesto.setText("1 из "+pd.getKolvoMest());
        raspechatal1.setText(Admin.name);
        idAndDate1.setText(pd.getNumZakaz()+" от "+formattedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);

        btn_next=menu.findItem(R.id.btn_next);
        btn_next.setVisible(true);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.btn_next){
            Intent myIntent = new Intent(Etiketka.this, ExpedPage.class);
            myIntent.putExtra("pdfExped",data);
            startActivity(myIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDateFormat(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c);
    }

    /*
        STARTING CREATING BITMAP WITH HELP OF ITEXT
     */
    public void fillPdfLayer1() throws IOException, DocumentException {
        destFile = createFile(DESTFILE);

        PdfReader reader = new PdfReader(getResources().openRawResource(R.raw.etiketsrc));
        OutputStream outputStream = new FileOutputStream(destFile);
        PdfStamper pdfStamper = new PdfStamper(reader, outputStream);
        AcroFields acroFields = pdfStamper.getAcroFields();

        setAcroFields(acroFields, reader,0);

        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        reader.close();
        outputStream.close();
    }

    public void fillPdfLayer2() throws IOException, DocumentException {
        destFile1=createFile(DESTFILE1);
        File dir=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String path=dir+"/"+DESTFILE;

        System.out.println("//////////////////////////"+path);

        PdfReader reader=new PdfReader(path);
        OutputStream outputStream=new FileOutputStream(destFile1);
        PdfStamper pdfStamper=new PdfStamper(reader,outputStream);

        /*CREATE PAGES BY KOLVO MEST*/
        generatePdfPages(reader,pdfStamper);

        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        reader.close();
        outputStream.close();
    }

    public void setAcroFields(AcroFields acroFields,PdfReader reader, int i) throws IOException, DocumentException {
        BaseFont bf=BaseFont.createFont(FONT,"CP1251",true);
        bf.addSubsetRange(BaseFont.CHAR_RANGE_CYRILLIC);
        acroFields.addSubstitutionFont(bf);

        acroFields.setField("otkuda",data.getFromCity());
        acroFields.setField("kuda",data.getToCity());
        acroFields.setField("otpravitel",data.getOtpravitel());
        acroFields.setField("poluchatel",data.getPoluchatel());
        acroFields.setField("kolvo",data.getKolvoMest());
        acroFields.setField("ves",data.getVes());
        acroFields.setField("obiem",data.getObiem());
        acroFields.setField("raspechatal",Admin.name);
        acroFields.setField("numdog",data.getNumZakaz());
        acroFields.setField("date",formattedDate);
        acroFields.setField("kolvoiz",String.valueOf(i+1));

        AcroFields fields = reader.getAcroFields();
        Set<String> fldNames = fields.getFields().keySet();

        for (String fldName : fldNames) {
            System.out.println( fldName + ": " + fields.getField( fldName ) );
        }
    }

    public void generatePdfPages(PdfReader reader,PdfStamper pdfStamper){
        for(int i=0; i<kolvoMest-1;i++) {
            int pages = reader.getNumberOfPages();
            pdfStamper.insertPage(pages + 1, reader.getPageSizeWithRotation(1));
            pdfStamper.replacePage(reader, 2, pages + 1);
        }
    }

    public File createFile(String dest){
        File dir=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File newFile=new File(dir,dest);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        return newFile;
    }

    public void lol() throws IOException, DocumentException {
        for(int i=0;i<kolvoMest-1;i++) {

            destFile2=createFile(DESTFILE2);

            PdfReader reader1=new PdfReader(getResources().openRawResource(R.raw.etiketsrc));
            OutputStream outputStream=new FileOutputStream(destFile2);
            PdfStamper stamper=new PdfStamper(reader1,outputStream);

            AcroFields acroFields =stamper.getAcroFields();
            setAcroFields(acroFields,reader1,i+1);

            stamper.close();
            reader1.close();
            outputStream.close();

            File dir=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            String path=dir+"/"+DESTFILE2;
            destFile3=createFile(DESTFILE3);

            PdfReader reader2=new PdfReader(path);
            OutputStream outputStream1=new FileOutputStream(destFile3);
            PdfStamper stamper1=new PdfStamper(reader2,outputStream1);

            Document doc=new Document();
            PdfCopy copy=new PdfSmartCopy(doc,new FileOutputStream(destFile1));

            PdfImportedPage importedPage=copy.getImportedPage(reader2,2);
            copy.addPage(importedPage);

            stamper1.close();
            reader2.close();
            outputStream1.close();
        }
    }


    public void fillPdfLayer3() throws IOException, DocumentException {
        File dir=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String path=dir+"/"+DESTFILE1;

        System.out.println("//////////////////////////"+path);

        PdfReader reader=new PdfReader(path);
        OutputStream outputStream=new FileOutputStream(destFile2);
        PdfStamper pdfStamper=new PdfStamper(reader,outputStream);

        //generatePdfPages(reader,pdfStamper);

        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        reader.close();
        outputStream.close();
    }

    public void clonePages(PdfReader reader,File dest) throws IOException, DocumentException {
        Document doc=new Document();
        PdfCopy copy=new PdfSmartCopy(doc,new FileOutputStream(dest));
        doc.open();

        int n=reader.getNumberOfPages();

        for(int i=1; i<=n;i++) {
            PdfImportedPage importedPage = copy.getImportedPage(reader, i);
            for (int j = 0; j < 2; j++) {
                copy.addPage(importedPage);
            }
        }
    }

    /*
        ENDING CREATING BITMAP WITH HELP OF ITEXT
     */

    /*
        STARTING GET PDF FROM LAYOUT WITH DENSITY
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPDF () {
        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        document = new PrintedPdfDocument(this,printAttrs);


        for(int i=0;i<kolvoMest+1;i++) {
            mesto.setText(i+" из "+kolvoMest);
            Bitmap bitmap=getViewBitmapWithDensity(firstPartLayout);
            Bitmap secondPart=getViewBitmapWithDensity(secondPartLayout);
            //Bitmap rszBitmap = resizeBitmap(bitmap, width, height);
            //Bitmap rszBitmap2 = resizeBitmap(secondPart, width, height);
            //Bitmap rszBitmap = get_Resized_Bitmap(bitmap, width, height);
            //Bitmap rszBitmap2 = get_Resized_Bitmap(secondPart, width, height);

            Bitmap rszBitmap = bitmap;
            Bitmap rszBitmap2 = secondPart;

            int xx = rszBitmap.getWidth();
            int yy = rszBitmap.getHeight();
            int secXX = rszBitmap2.getWidth();
            int secYY = rszBitmap2.getHeight();

            createCanvas(rszBitmap, rszBitmap2, xx, yy, secXX, secYY, i);
        }
        saveOnDevice(document);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getViewBitmapWithDensity(View v) {
        Bitmap bitmap = Bitmap.createBitmap(
                1100,
                1100,
                Bitmap.Config.RGBA_F16
        );

        bitmap.setDensity(1300);
        Canvas canvas = new Canvas(bitmap);

        v.draw(canvas);
        return bitmap;
    }

    /*
        ENDING GET PDF FROM LAYOUT WITH DENSITY
     */


    /*
        STARTING GET PDF FROM LAYOUT
     */

    public void createPDFfromLayout(){
        int width=280;
        int height=200;

        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        document = new PrintedPdfDocument(this,printAttrs);

        for(int i=0;i<kolvoMest+1;i++) {
            mesto.setText(i+" из "+kolvoMest);
            Bitmap bitmap=getViewBitmap(firstPartLayout);
            Bitmap secondPart=getViewBitmap(secondPartLayout);
            Bitmap rszBitmap = resizeBitmap(bitmap, width, height);
            Bitmap rszBitmap2 = resizeBitmap(secondPart, width, height);

            int xx = rszBitmap.getWidth();
            int yy = rszBitmap.getHeight();
            int secXX = rszBitmap2.getWidth();
            int secYY = rszBitmap2.getHeight();

            createCanvas(rszBitmap, rszBitmap2, xx, yy, secXX, secYY, i);

        }

        saveOnDevice(document);
    }

    public Bitmap getViewBitmap(View v){
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int newWidth,int newHeight){
        Bitmap resizedBitmap;
        resizedBitmap = Other.resizeImage(bitmap, newWidth, newHeight);
        return resizedBitmap;
    }

    /*
        ENDING GET PDF FROM LAYOUT
     */

    public void createCanvas(Bitmap btm, Bitmap btm1, int width,int height, int width1, int height1, int i){
        if(i==0){
            android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(width, height, i + 1).create();
            android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas=page.getCanvas();
            canvas.drawBitmap(btm,0,0,null);
            document.finishPage(page);
        }else if(i>0){
            android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(width1, height1, i + 1).create();
            android.graphics.pdf.PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas=page.getCanvas();
            canvas.drawBitmap(btm1,0,0,null);
            document.finishPage(page);
        }
    }

    public void saveOnDevice(PrintedPdfDocument document){
        File mFolder;
        String fileName="Etiketka.pdf";
        try {
            mFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            imageFile = new File(mFolder,fileName);
            if (!mFolder.exists()) {
                mFolder.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(imageFile);
            document.writeTo(out);
            document.close();
            out.close();
            Toast.makeText(this,"Результат сохранен", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "При сохранении возникла ошибка", Toast.LENGTH_LONG).show();
        }
    }

    public void printDocument(File file,int totalPage){
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(file,totalPage), null);
    }

    public Bitmap get_Resized_Bitmap(Bitmap bmp, int newHeight, int newWidth) {
        Bitmap newBitmap= Bitmap.createScaledBitmap(bmp, newHeight, newWidth, true);
        return newBitmap ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888,true);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(resizedBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        return resizedBitmap;
    }
}
