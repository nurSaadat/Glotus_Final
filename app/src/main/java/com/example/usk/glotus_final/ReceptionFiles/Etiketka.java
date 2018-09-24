package com.example.usk.glotus_final.ReceptionFiles;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Etiketka extends AppCompatActivity{
    private RelativeLayout pdf_contEt;
    private ScrollView scrollViewEt;
    private MenuItem btn_generate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiketka);

        scrollViewEt=findViewById(R.id.scrollView3);
        pdf_contEt=findViewById(R.id.relativeLay);
        buildText();
    }

    public void buildText(){
        PdfData pd=Reception.pd;

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
        TextView mesta=findViewById(R.id.mestoBottomField);
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
        btn_generate = menu.findItem(R.id.pdf_create);
        btn_generate.setVisible(true);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pdf_create) {
            save(pdf_contEt);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save (View v){
        RelativeLayout scroll = (RelativeLayout) findViewById(R.id.relativeLay);
        int yy = scroll.getScrollY()+scroll.getHeight();
        int xx = scroll.getWidth();

        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        PrintedPdfDocument document = new PrintedPdfDocument(this,printAttrs);
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(xx, yy, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        scroll.draw(page.getCanvas());
        document.finishPage(page);
        try {
            File mFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File imageFile = new File(mFolder,"Этикетка"+ "_"+ System.currentTimeMillis() + ".pdf");
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
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(Etiketka.this, Reception.class);
        startActivity(myIntent);
    }
}
