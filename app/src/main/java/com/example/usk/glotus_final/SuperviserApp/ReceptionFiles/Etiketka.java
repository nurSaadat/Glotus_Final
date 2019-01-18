package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.BluetoothService.BluetoothMain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class Etiketka extends AppCompatActivity{
    private RelativeLayout pdf_contEt;
    private ScrollView scrollViewEt;
    private MenuItem btn_next;
    private MenuItem btn_print;
    private PdfData data;
    private File imageFile;
    private PdfInfo pdfEtiketka;

    private TextView mesta;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiketka);

        Intent intent=getIntent();
        data=(PdfData) intent.getExtras().getSerializable("pdfData");

        scrollViewEt=findViewById(R.id.scrollView3);
        pdf_contEt=findViewById(R.id.relativeLay);
        buildText(data);
    }

    public void buildText(PdfData pd){

        TextView fromCity=findViewById(R.id.otkudaField);
        TextView toCity=findViewById(R.id.kudaField);
        TextView otpr= findViewById(R.id.otpravitelField);
        TextView poluch=findViewById(R.id.poluchatelField);
        TextView kolvMest=findViewById(R.id.kolvoMestField);
        TextView ves=findViewById(R.id.vesField);
        TextView obm=findViewById(R.id.obiemField);
        TextView trans=findViewById(R.id.transportTypeField);
        TextView rasp=findViewById(R.id.raspechatalField);
        TextView numZ=findViewById(R.id.numZakaz);
        TextView dateZ=findViewById(R.id.dateZakazField);
        TextView fromCityBottom=findViewById(R.id.otkudaBottomField);
        TextView toCityBottom=findViewById(R.id.kudaBottomField);
        mesta=findViewById(R.id.mestoBottomField);
        TextView otprBottom=findViewById(R.id.otpravitelBottomField);
        TextView poluchBottom=findViewById(R.id.poluchatelBottomField);
        TextView pasrbottom=findViewById(R.id.raspBottomField);
        TextView numZbottom=findViewById(R.id.numZakazBottom);
        TextView dateBottom=findViewById(R.id.dateZakazBottom);

        fromCity.setText(pd.getFromCity());
        toCity.setText(pd.getToCity());
        otpr.setText(pd.getOtpravitel());
        poluch.setText(pd.getPoluchatel());
        kolvMest.setText(pd.getKolvoMest());
        ves.setText(pd.getVes());
        obm.setText(pd.getObiem());
        trans.setText(pd.getTypeTrans());
        rasp.setText(pd.getRasp());
        numZ.setText(pd.getNumZakaz());
        dateZ.setText(pd.getDate());
        fromCityBottom.setText(pd.getFromCity());
        toCityBottom.setText(pd.getToCity());
        mesta.setText("1 из "+ pd.getKolvoMest());
        otprBottom.setText(pd.getOtpravitel());
        poluchBottom.setText(pd.getPoluchatel());
        pasrbottom.setText(pd.getRasp());
        numZbottom.setText(pd.getNumZakaz());
        dateBottom.setText(pd.getDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);

        btn_next=menu.findItem(R.id.btn_next);
        btn_next.setVisible(true);
        btn_print=menu.findItem(R.id.btn_print);
        btn_print.setVisible(true);
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
        }

        if(id==R.id.btn_print){
            save(pdf_contEt);
            Intent myIntent = new Intent(Etiketka.this, BluetoothMain.class);
            //myIntent.putExtra("pdfEtiketka", bitmapInfo);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save (View v){
        RelativeLayout scroll = findViewById(R.id.relativeLay);
        int yy = scroll.getScrollY()+scroll.getHeight();
        int xx = scroll.getWidth();

        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();

        PrintedPdfDocument document = new PrintedPdfDocument(this,printAttrs);
        String fileName="Этикетка.pdf";
        for(int i=0; i<Integer.valueOf(data.getKolvoMest());i++) {
            mesta.setText((i+1)+" из "+ data.getKolvoMest());
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(xx, yy, i+1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            scroll.draw(page.getCanvas());
            document.finishPage(page);
        }

        try {
            File mFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            imageFile = new File(mFolder,fileName/*+ "_"+ System.currentTimeMillis() + ".pdf"*/);
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

        pdfEtiketka=new PdfInfo(imageFile,imageFile.getAbsolutePath(),fileName);
    }

    /*public void open(File imageFile){
        String rootSDCard=Environment.getExternalStorageDirectory().getAbsolutePath();

        System.out.println(imageFile.toURI().toString());
        String uri=imageFile.toURI().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(uri), "application/pdf");
        startActivity(intent);
    }*/

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(Etiketka.this, Reception.class);
        startActivity(myIntent);
    }
}
