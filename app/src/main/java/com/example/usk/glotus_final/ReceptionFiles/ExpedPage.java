package com.example.usk.glotus_final.ReceptionFiles;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
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
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserListFiles.SuperviserListItemActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExpedPage extends AppCompatActivity {
    private RelativeLayout pdf_cont;
    private ScrollView scrollView2;
    private MenuItem btn_generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exped);

        scrollView2=findViewById(R.id.scrollview);
        pdf_cont=findViewById(R.id.relLay);
        //buildText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);
        btn_generate = menu.findItem(R.id.pdf_create);
        btn_generate.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pdf_create) {
            save(pdf_cont);
        }
        return super.onOptionsItemSelected(item);
    }

    public void save (View v){
        RelativeLayout scroll = (RelativeLayout) findViewById(R.id.relLay);
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
            File imageFile = new File(mFolder,"filename"+ "_"+ System.currentTimeMillis() + ".pdf");
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
