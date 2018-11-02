package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
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
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;
import com.example.usk.glotus_final.SuperviserApp.WifiManagerService.WifiManagerClass;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExpedPage extends AppCompatActivity {
    private RelativeLayout pdf_cont;
    private ScrollView scrollView2;
    private MenuItem btn_generate;
    private MenuItem btn_ok;
    private MenuItem btn_print;
    private MenuItem btn_next;
    private String upak=Reception.upakovka.getSelectedItem().toString();
    private PdfData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exped);

        Intent intent=getIntent();
        data=(PdfData) intent.getExtras().getSerializable("pdfExped");

        scrollView2=findViewById(R.id.scrollview);
        pdf_cont=findViewById(R.id.relLay);
        buildText(data);
        save(pdf_cont);
    }

    public void buildText(PdfData item){
        TextView expedNum=findViewById(R.id.expedNum);
        TextView date=findViewById(R.id.vremyaFill);
        TextView otkuda=findViewById(R.id.marshrutFill);
        TextView kuda=findViewById(R.id.marshrutFill2);
        TextView naimenOtpr=findViewById(R.id.naimenGruzFill);
        TextView addressOtpr=findViewById(R.id.adresFill);
        TextView contactOtpr=findViewById(R.id.contacFill);           //from 1C
        TextView naimenPoluch=findViewById(R.id.naimenPoluchFill);
        TextView addressPoluch=findViewById(R.id.adressPoluchFill);
        TextView contactPoluch=findViewById(R.id.contactPoluchFill);  //from 1C
        TextView platelshik=findViewById(R.id.platelshikFill);
        TextView naimenGruz=findViewById(R.id.naimenGruzFill);        //from 1C
        TextView characGruz=findViewById(R.id.characterGruzFill);     //from 1C
        TextView kolvoMest=findViewById(R.id.kolvoMestFill);
        TextView upakovka=findViewById(R.id.upakovka);
        TextView ves=findViewById(R.id.ves);
        TextView obiem=findViewById(R.id.obiem);
        TextView dopUslugi=findViewById(R.id.dopUslugiFill);            //from 1C
        TextView osobOtmExp=findViewById(R.id.osobOtmExpFill);          //from 1C
        TextView soprDoc=findViewById(R.id.soprDocFill);                //from 1C

        expedNum.setText(item.getNumZakaz());
        date.setText(item.getDate());
        otkuda.setText(item.getFromCity());
        kuda.setText(item.getToCity());
        naimenOtpr.setText(item.getOtpravitel());
        addressOtpr.setText(item.getFromCity());
        naimenPoluch.setText(item.getPoluchatel());
        addressPoluch.setText(item.getToCity());
        platelshik.setText(item.getPoluchatel());
        kolvoMest.setText(item.getKolvoMest());
        upakovka.setText(upak);
        ves.setText(item.getVes());
        obiem.setText(item.getObiem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);
        btn_generate = menu.findItem(R.id.pdf_create);
        btn_generate.setVisible(false);

        btn_next=menu.findItem(R.id.btn_next);
        btn_next.setVisible(false);

        btn_ok=menu.findItem(R.id.btn_ok);
        btn_ok.setVisible(true);

        btn_print=menu.findItem(R.id.btn_print);
        btn_print.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.btn_ok){
            Intent myintent = new Intent(ExpedPage.this, SuperviserListActivity.class);
            startActivity(myintent);
        }
        /*if(id==R.id.btn_next){
            Intent myintent = new Intent(ExpedPage.this, SuperviserListActivity.class);
            startActivity(myintent);
        }*/

        if(id==R.id.btn_print){
            Intent myintent = new Intent(ExpedPage.this, WifiManagerClass.class);
            startActivity(myintent);
        }

        /*if (id == R.id.pdf_create) {
            save(pdf_cont);
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void save (View v){
        RelativeLayout scroll = (RelativeLayout) findViewById(R.id.relLay);
        int yy = scroll.getScrollY()+v.getHeight();
        int xx = scroll.getWidth();

        PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();
        PrintedPdfDocument document = new PrintedPdfDocument(this,printAttrs);
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(25, 25, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        scroll.draw(page.getCanvas());
        document.finishPage(page);

        File mFolder;
        File imageFile;
        try {
            mFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            imageFile = new File(mFolder,"Exped.pdf"/*+ "_"+ System.currentTimeMillis() + ".pdf"*/);
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
        Intent myIntent = new Intent(ExpedPage.this, Reception.class);
        startActivity(myIntent);
    }
}
