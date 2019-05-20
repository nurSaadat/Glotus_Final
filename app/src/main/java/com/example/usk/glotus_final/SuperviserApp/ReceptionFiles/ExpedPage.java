package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;


public class ExpedPage extends AppCompatActivity  {
    private MenuItem btn_print;
    private String upak=Reception.upakovka.getSelectedItem().toString();
    private PdfData item;
    private File destinationFile;

    private final static String FONT="/assets/fonts/PTC55F.ttf";
    private final static String DESTFILE="ExpeditorFile.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exped);

        Intent intent=getIntent();
        item=(PdfData) intent.getExtras().getSerializable("pdfExped");

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
        TextView addressOtpr=findViewById(R.id.adresFill);
        TextView contactOtpr=findViewById(R.id.contacFill);           //from 1C
        TextView naimenPoluch=findViewById(R.id.naimenPoluchFill);
        TextView addressPoluch=findViewById(R.id.adressPoluchFill);
        TextView contactPoluch=findViewById(R.id.contactPoluchFill);  //from 1C
        TextView platelshik=findViewById(R.id.platelshikFill);        //from 1С
        TextView naimenGruz=findViewById(R.id.naimenGruzFill);        //from 1C
        TextView characGruz=findViewById(R.id.characterGruzFill);     //from 1C
        TextView kolvoMest=findViewById(R.id.kolvoMestFill);
        TextView upakovka=findViewById(R.id.upakovka);
        TextView ves=findViewById(R.id.ves);
        TextView obiem=findViewById(R.id.obiem);
        TextView dopUslugi=findViewById(R.id.dopUslugiFill);            //from 1C
        TextView osobOtmExp=findViewById(R.id.osobOtmExpFill);          //from 1C

        naimenOtpr.setText(item.getNamegruz());
        contactOtpr.setText(Kont.numotpr);
        contactPoluch.setText(Kont.numpoluch);
        platelshik.setText("");
        naimenGruz.setText(Kont.namegruz);
        characGruz.setText(item.getHaracgruz());
        dopUslugi.setText("Забор от клиента\n" +
                "Доставка клиенту\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
        osobOtmExp.setText("");

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
            finish();
        }

        if(id==R.id.btn_print){
            try {
                fillPDF();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            printDocument(destinationFile,2);
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillPDF() throws IOException, DocumentException {
        destinationFile=createFile();

        PdfReader reader=new PdfReader(getResources().openRawResource(R.raw.exped_src_file));
        OutputStream outputStream=new FileOutputStream(destinationFile);
        PdfStamper pdfStamper=new PdfStamper(reader,outputStream);
        AcroFields acroFields=pdfStamper.getAcroFields();

        BaseFont bf=BaseFont.createFont(FONT,"CP1251",true);
        bf.addSubsetRange(BaseFont.CHAR_RANGE_CYRILLIC);
        acroFields.addSubstitutionFont(bf);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        acroFields.setField("exp_num", item.getNumZakaz());
        acroFields.setField("date", formattedDate);
        acroFields.setField("from_city", item.getFromCity());
        acroFields.setField("dest_city", item.getToCity());
        acroFields.setField("naimen_otpr", item.getOtpravitel());
        acroFields.setField("address_otpr", item.getFromCity());
        acroFields.setField("mobnum_otpr", Kont.numotpr);
        acroFields.setField("naimen_poluch", item.getPoluchatel());
        acroFields.setField("address_poluch", item.getToCity());
        acroFields.setField("mobnum_poluch", Kont.numpoluch);
        acroFields.setField("platelshik","lola");
        acroFields.setField("naimen_gruz", Kont.namegruz);
        acroFields.setField("charac_gruz", item.getHaracgruz());
        acroFields.setField("kolvomest", item.getKolvoMest());
        acroFields.setField("upak_gruz", upak);
        acroFields.setField("ves_gruz", item.getVes());
        acroFields.setField("obiem_gruz", item.getObiem());
        acroFields.setField("dop_uslugi","Забор от клиента\n" + "Доставка клиенту\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
        acroFields.setField("otmetki_exp","");

        AcroFields fields = reader.getAcroFields();
        Set<String> fldNames = fields.getFields().keySet();

        for (String fldName : fldNames) {
            System.out.println( fldName + ": " + fields.getField( fldName ) );
        }

        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        reader.close();
        outputStream.close();
    }

    public File createFile(){
        File dir=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File newFile=new File(dir,DESTFILE);
        if(!dir.exists());
            dir.mkdirs();

        return newFile;
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
        finish();
    }
}
