package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.Command;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.Other;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.PrintPicture;
import com.example.usk.glotus_final.SuperviserApp.BluetoothPrintService.PrinterCommand;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class Etiketka extends AppCompatActivity{
    private MenuItem btn_next;
    private PdfData data;
    LinearLayout linearLayout;
    private TextView mesto;
    static TextView myLabel;

    static BluetoothAdapter mBluetoothAdapter;
    static BluetoothSocket mmSocket;
    static BluetoothDevice mmDevice;
    static Set<BluetoothDevice> pairedDevices;

    static OutputStream mmOutputStream;
    static InputStream mmInputStream;
    static Thread workerThread;

    static byte[] readBuffer;
    static int readBufferPosition;
    static volatile boolean stopWorker;

    Bitmap bmp;
    Button coonect,send,off;
    Spinner bluetoothDevices;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiketka);

        Intent intent=getIntent();
        data=(PdfData) intent.getExtras().getSerializable("pdfData");
        buildText(data);
        final int kolvoMest=Integer.parseInt(data.getKolvoMest());

        try {
            initialBLuetooth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            setBoundedDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            myLabel = findViewById(R.id.label);

            coonect.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        connectBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    save();
                    /*try {
                        for(int i=0; i<kolvoMest;i++){
                            String mest=String.valueOf(i+1);
                            mesto.setText(mest+" из "+data.getKolvoMest());
                            bmp=getViewBitmap(linearLayout);
                            Print_BMP(bmp);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }*/
                }
            });

            off.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        closeBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }

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

        linearLayout=findViewById(R.id.linLay);
        coonect = findViewById(R.id.open);
        send = findViewById(R.id.send);
        off = findViewById(R.id.close);
        bluetoothDevices=findViewById(R.id.bluetoothDevices);

        gorodOtkuda.setText(pd.getFromCity());
        gorodKuda.setText(pd.getToCity());
        imya_otprav.setText(pd.getOtpravitel());
        imya_otprav1.setText(pd.getOtpravitel());
        imya_poluch.setText(pd.getPoluchatel());
        imya_poluch1.setText(pd.getPoluchatel());
        kolichMest.setText(pd.getKolvoMest());
        ves.setText(pd.getVes());
        obiem.setText(pd.getObiem());
        raspechatal.setText(pd.getRasp());
        idAndDate.setText(pd.getNumZakaz()+" от "+pd.getDate());
        gorodOtkuda1.setText(pd.getFromCity());
        gorodKuda1.setText(pd.getToCity());
        mesto.setText("1 из "+pd.getKolvoMest());
        raspechatal1.setText(pd.getRasp());
        idAndDate1.setText(pd.getNumZakaz()+" от "+pd.getDate());
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
        }
        return super.onOptionsItemSelected(item);
    }

    void initialBLuetooth() throws Exception{
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null) {
            myLabel.setText("Bluetooth адаптер не подключен.");
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    void setBoundedDevices() throws Exception{
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        String [] devices=new String[pairedDevices.size()];

        int i=0;
        for(BluetoothDevice dev:pairedDevices){
            devices[i]=dev.getName();
            i++;
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,devices);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bluetoothDevices.setAdapter(arrayAdapter);

        bluetoothDevices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals(adapterView.getSelectedItem().toString())) {
                            mmDevice = device;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void connectBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

        if (!mmSocket.isConnected()){
            myLabel.setText(mmDevice.getName()+" не активировано");
        }

        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        myLabel.setText("Bluetooth подключен к "+mmDevice.getName());
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(
                                            readBuffer, 0,
                                            encodedBytes, 0,
                                            encodedBytes.length
                                    );

                                    // specify US-ASCII encoding
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    // tell the user data were sent to bluetooth printer device
                                    handler.post(new Runnable() {
                                        public void run() {
                                            myLabel.setText(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth не подключен к устроиству.");
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

    void Print_BMP(Bitmap mBitmap) throws IOException {

        int nMode = 0;
        int nPaperWidth = 384;

        if(mBitmap != null)
        {
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode,12);

            mmOutputStream.write(Command.ESC_Init);
            mmOutputStream.write(Command.LF);
            mmOutputStream.write(data);
            mmOutputStream.write(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            mmOutputStream.write(PrinterCommand.POS_Set_Cut(1));
            mmOutputStream.write(Command.FEED_LINE);
            mmOutputStream.write(Command.FEED_LINE);
            mmOutputStream.write(PrinterCommand.POS_Set_PrtInit());
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(Etiketka.this, SuperviserListActivity.class);
        startActivity(myIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save (){
        Bitmap bitmap=getViewBitmap(linearLayout);
        int width = ((140 + 7) / 8) * 8;
        int height = bitmap.getHeight() * width / bitmap.getWidth();
        height = ((height + 7) / 8) * 8;


        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);



        //bitmap = Bitmap.createScaledBitmap(bitmap, 384, bitmap.getHeight(), true);



        Bitmap bm =getViewBitmap(linearLayout);
        bm=getResizedBitmap(bm,width,height);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bm, 0, 0 , new Paint(Paint.FILTER_BITMAP_FLAG));


        document.finishPage(page);
        // write the document content
        String targetPdf = "/test.pdf";

        File mFolder = null;
        File imageFile = null;
        String fileName="ttt.pdf";
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

            Toast.makeText(this,"Результат сохранен"+imageFile.getPath().toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "При сохранении возникла ошибка", Toast.LENGTH_LONG).show();
        }
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(imageFile), null);



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


    /*
    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);



        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            btn_convert.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }*/

}
