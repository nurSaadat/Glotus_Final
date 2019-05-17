package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.Other;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.qoppa.android.pdfProcess.PDFFont;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;


public class ExpedPage extends AppCompatActivity  {
    private RelativeLayout pdf_cont;
    private ScrollView scrollView2;
    private MenuItem btn_print;
    private String upak=Reception.upakovka.getSelectedItem().toString();
    private PdfData item;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exped);

        Intent intent=getIntent();
        item=(PdfData) intent.getExtras().getSerializable("pdfExped");

        pdf_cont=findViewById(R.id.relLay);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        buildText(item);
    }

    public void buildText(PdfData item){
        TextView expedNum=findViewById(R.id.expedNum);
        TextView date=findViewById(R.id.vremyaFill);
        TextView otkuda=findViewById(R.id.marshrutFill);
        TextView kuda=findViewById(R.id.marshrutFill2);
        TextView naimOtpr=findViewById(R.id.naimenovanieFill);

        TextView naimenOtpr=findViewById(R.id.naimenGruzFill);         //from 1C
        naimenOtpr.setText(item.getNamegruz());

        TextView addressOtpr=findViewById(R.id.adresFill);

        TextView contactOtpr=findViewById(R.id.contacFill);           //from 1C
        contactOtpr.setText(Kont.numotpr);

        TextView naimenPoluch=findViewById(R.id.naimenPoluchFill);
        TextView addressPoluch=findViewById(R.id.adressPoluchFill);

        TextView contactPoluch=findViewById(R.id.contactPoluchFill);  //from 1C
        contactPoluch.setText(Kont.numpoluch);

        TextView platelshik=findViewById(R.id.platelshikFill);        //from 1С
        platelshik.setText("");

        TextView naimenGruz=findViewById(R.id.naimenGruzFill);        //from 1C
        naimenGruz.setText(Kont.namegruz);

        TextView characGruz=findViewById(R.id.characterGruzFill);     //from 1C
        characGruz.setText(item.getHaracgruz());

        TextView kolvoMest=findViewById(R.id.kolvoMestFill);
        TextView upakovka=findViewById(R.id.upakovka);
        TextView ves=findViewById(R.id.ves);
        TextView obiem=findViewById(R.id.obiem);

        TextView dopUslugi=findViewById(R.id.dopUslugiFill);            //from 1C
        dopUslugi.setText("Забор от клиента\n" +
                "Доставка клиенту\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
        TextView osobOtmExp=findViewById(R.id.osobOtmExpFill);          //from 1C
        osobOtmExp.setText("");
        //TextView soprDoc=findViewById(R.id.soprDocFill);                //from 1C

        expedNum.setText(item.getNumZakaz());
        date.setText(item.getDate());
        otkuda.setText(item.getFromCity());
        kuda.setText(item.getToCity());
        naimOtpr.setText(item.getOtpravitel());
        addressOtpr.setText(item.getFromCity());
        naimenPoluch.setText(item.getPoluchatel());
        addressPoluch.setText(item.getToCity());
        kolvoMest.setText(item.getKolvoMest());
        upakovka.setText(upak);
        ves.setText(item.getVes());
        obiem.setText(item.getObiem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);

        btn_print=menu.findItem(R.id.btn_print);
        btn_print.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.btn_next){
            Intent myintent = new Intent(ExpedPage.this, SuperviserListActivity.class);
            startActivity(myintent);
        }

        if(id==R.id.btn_print){
            //createPDF(pdf_cont);
            try {
                fillPDF();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            printDocument(imageFile,1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createPDF (View v){
        //ScrollView scroll = findViewById(R.id.scrollview);
        RelativeLayout rel=findViewById(R.id.relLay);
        int yy = v.getScrollY()+v.getHeight();
        int xx = v.getWidth();


        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        PrintedPdfDocument document = new PrintedPdfDocument(this,printAttrs);
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(xx, yy, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        rel.draw(page.getCanvas());
        document.finishPage(page);

        saveOnDevice(document);
    }

    public void fillPDF() throws IOException, DocumentException {
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        imageFile=new File(dir,"Ex.pdf");

        PdfReader reader=new PdfReader(dir+"/Exp.pdf");
        OutputStream outputStream=new FileOutputStream(imageFile);
        BaseFont bf= (BaseFont) BaseFont.createFont("/assets/fonts/arial.ttf","CP1251",BaseFont.EMBEDDED);
        PdfStamper pdfStamper=new PdfStamper(reader,outputStream);
        AcroFields acroFields=pdfStamper.getAcroFields();
        bf.addSubsetRange(BaseFont.CHAR_RANGE_CYRILLIC);
        acroFields.addSubstitutionFont(bf);

        acroFields.setField("exp_num",item.getNumZakaz());
        acroFields.setField("date", item.getDate());
        acroFields.setField("from_city",item.getFromCity());
        acroFields.setField("dest_city",item.getToCity());
        acroFields.setField("naimen_otpr",item.getOtpravitel());
        acroFields.setField("address_otpr",item.getFromCity());
        acroFields.setField("mobnum_otpr","нет данных");
        acroFields.setField("naimen_poluch",item.getPoluchatel());
        acroFields.setField("address_poluch",item.getToCity());
        acroFields.setField("mobnum_poluch","нет данных");
        acroFields.setField("platelshik","нет данных");
        acroFields.setField("naimen_gruz","нет данных");
        acroFields.setField("charac_gruz","нет данных");
        acroFields.setField("kolvomest",item.getKolvoMest());
        acroFields.setField("upak_gruz",upak);
        acroFields.setField("ves_gruz",item.getVes());
        acroFields.setField("obiem_gruz",item.getObiem());
        acroFields.setField("dop_uslugi","нет данных");
        acroFields.setField("otmetki_exp","нет данных");


        AcroFields fields = reader.getAcroFields();
        Set<String> fldNames = fields.getFields().keySet();

        for (String fldName : fldNames) {
            System.out.println( fldName + ": " + fields.getField( fldName ) );
        }
        pdfStamper.close();
        outputStream.close();
        reader.close();
    }


    public void saveOnDevice(PdfDocument document){
        File mFolder;
        String fileName="Exped.pdf";
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

    public void printDocument(File file, int totalPage){
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(file,totalPage), null);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ExpedPage.this, SuperviserListActivity.class);
        startActivity(myIntent);
    }
}
