package com.example.usk.glotus_final.SuperviserApp.BluetoothService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.Image;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.PdfInfo;
import com.qoppa.android.pdf.source.ByteArrayPDFSource;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothMain extends Activity {
    TextView myLabel;
    EditText myTextbox;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    PdfInfo pdfInfo;
    Bitmap bmp;
    Bitmap bb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_main);

        Intent intent=getIntent();
        pdfInfo=(PdfInfo) intent.getExtras().getSerializable("pdfEtiketka");

        ImageView img=findViewById(R.id.imageView2);
        final String pathFile=generateBitmap();
        ImageView ii=findViewById(R.id.imageView3);

        img.setImageBitmap(getImage(pathFile));
        bmp=getViewBitmap(img);

        bb=getViewBitmap(ii);


        try {

            Button openButton = findViewById(R.id.open);
            Button sendButton = findViewById(R.id.send);
            Button closeButton = findViewById(R.id.close);

            myLabel = findViewById(R.id.label);
            myTextbox = findViewById(R.id.entry);

            openButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        findBT();
                        openBT();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        //sendData();
                        //printPhoto(pathFile);
                        //print_image(pathFile);
                        //printPhoto(bmp);
                        //print_image(bmp);
                        printImages(bb);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
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

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                myLabel.setText("Bluetooth адаптер не подключен.");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("BlueTooth Printer")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            myLabel.setText("Bluetooth подключен к "+mmDevice.getName());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            myLabel.setText("Bluetooth Включен");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData() throws IOException {
        try {
            String msg = myTextbox.getText().toString();
            msg += "\n";
            mmOutputStream.write(msg.getBytes());
            myLabel.setText("Отправлено");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Выключен.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateBitmap(){
        String bitmapPath=null;
        try{
            StandardFontTF.mAssetMgr = getAssets();

            // Load a document and get the first page
            PDFDocument pdf = new PDFDocument(pdfInfo.getPath(), null);
            PDFPage page = pdf.getPage(0);

            // Create a bitmap and canvas to draw the page into
            int width = (int)Math.ceil (page.getDisplayWidth());
            int height = (int)Math.ceil(page.getDisplayHeight());
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // Create canvas to draw into the bitmap
            Canvas c = new Canvas (bm);

            // Fill the bitmap with a white background
            Paint whiteBgnd = new Paint();
            whiteBgnd.setColor(Color.WHITE);
            whiteBgnd.setStyle(Paint.Style.FILL);
            c.drawRect(0, 0, width, height, whiteBgnd);

            // paint the page into the canvas
            page.paintPage(c);

            // Resize the bitmap

            bitmapPath=saveBitmap(bm);
            //bitmapPath=saveBitmap(bm);
            //bitmapPath=saveBitmap(bm);
            // Save the bitmap

        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmapPath;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap getImage(String path){
        Bitmap btm=BitmapFactory.decodeFile(path);
        return btm;
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

    public String saveBitmap(Bitmap bm){
        File file=null;
        try{
            File fold=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            file=new File(fold,"out.jpg");
            if (!fold.exists()) {
                fold.mkdirs();
            }
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
            outStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }



    /***
     *
     *
     * **/
    public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = { "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111" };
    public static byte[] FEED_LINE = {10};

    public void printPhoto(String path) throws IOException {
        try {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if(bmp!=null){
                byte[] command =decodeBitmap(bmp);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printPhoto(Bitmap btm) throws IOException {
        try {
            //Bitmap bmp = BitmapFactory.decodeFile(path);
            if(bmp!=null){
                byte[] command =decodeBitmap(bmp);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printNewLine() {
        try {
            mmOutputStream.write(FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] decodeBitmap(Bitmap bmp){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        System.out.print(bmpHeight+"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;
    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     *
     *
     */

    private byte[] buildPOSCommand(byte[] command, byte... args) {
        byte[] posCommand = new byte[command.length + args.length];

        System.arraycopy(command, 0, posCommand, 0, command.length);
        System.arraycopy(args, 0, posCommand, command.length, args.length);

        return posCommand;
    }

    BitSet dots;
    private void print_image(String file) throws IOException {
        File fl = new File(file);
        if (fl.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(file);
            convertBitmap(bmp);
            byte nL=(byte) (bmp.getWidth()& 0xFF);
            byte nH=(byte) ((bmp.getWidth()>>8) & 0xFF);
//            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_24);
            mmOutputStream.write(PrinterCommands.ESC_Init);
            mmOutputStream.write(PrinterCommands.ESC_Init1);

            byte[] qwer=buildPOSCommand(PrinterCommands.SELECT_BIT_IMAGE_MODE,nL,nH);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                mmOutputStream.write(qwer);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }

                        mmOutputStream.write(slice);
                    }
                }
                offset += 24;
                mmOutputStream.write(PrinterCommands.LF);
                /*mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);*/
            }
            /*mmOutputStream.write(PrinterCommands.ESC_J);
            mmOutputStream.write(PrinterCommands.GS_V_m_n);
            mmOutputStream.write(PrinterCommands.ESC_Init);*/
            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void print_image(Bitmap bmp) throws IOException {
            convertBitmap(bmp);
            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                mmOutputStream.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }

                        mmOutputStream.write(slice);
                    }
                }
                offset += 24;

                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
            }
            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_30);
    }

    public String convertBitmap(Bitmap inputBitmap) {

        int mWidth = ((inputBitmap.getWidth()+7)/8)*8;
        int mHeight = inputBitmap.getHeight()*mWidth/inputBitmap.getWidth();
        mHeight=((mHeight+7)/8)*8;
        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        String mStatus = "ok";
        return mStatus;

    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            //Log.e(TAG, e.toString());
        }
    }


    /***
     *
     *
     */
        private final byte[] INITIALIZE_PRINTER = new byte[]{0x1B,0x40};

        private final byte[] PRINT_AND_FEED_PAPER = new byte[]{0x0A};

        private final byte[] SELECT_BIT_IMAGE_MODE = new byte[]{(byte)0x1B, (byte)0x2A};
        private final byte[] SET_LINE_SPACING = new byte[]{0x1B, 0x33};

        private FileOutputStream printOutput;

        public int maxBitsWidth = 255;


        private byte[] buildPOSCmmand(byte[] command, byte... args) {
            byte[] posCommand = new byte[command.length + args.length];

            System.arraycopy(command, 0, posCommand, 0, command.length);
            System.arraycopy(args, 0, posCommand, command.length, args.length);

            return posCommand;
        }

        private BitSet getBitsImageData(Bitmap bmt) {
            int threshold = 127;
            int index = 0;
            int dimenssions = bmt.getWidth() * bmt.getHeight();
            BitSet imageBitsData = new BitSet(dimenssions);

            for (int y = 0; y < bmt.getHeight(); y++)
            {
                for (int x = 0; x < bmt.getWidth(); x++)
                {
                    int color = bmt.getPixel(x,y);
                    int  red = (color & 0x00ff0000) >> 16;
                    int  green = (color & 0x0000ff00) >> 8;
                    int  blue = color & 0x000000ff;
                    int luminance = (int)(red * 0.3 + green * 0.59 + blue * 0.11);
                    //dots[index] = (luminance < threshold);
                    imageBitsData.set(index, (luminance < threshold));
                    index++;
                }
            }

            return imageBitsData;
        }

        public void printImages(Bitmap image) throws IOException{
            try {
                BitSet imageBits = getBitsImageData(image);

                byte widthLSB = (byte)(image.getWidth() & 0xFF);
                byte widthMSB = (byte)((image.getWidth() >> 16) & 0xFF);

                // COMMANDS
                byte[] selectBitImageModeCommand = buildPOSCmmand(SELECT_BIT_IMAGE_MODE, (byte) 33, widthLSB, widthMSB);
                byte[] setLineSpacing24Dots = buildPOSCmmand(SET_LINE_SPACING, (byte) 24);
                byte[] setLineSpacing30Dots = buildPOSCmmand(SET_LINE_SPACING, (byte) 30);


                mmOutputStream.write(INITIALIZE_PRINTER);
                mmOutputStream.write(setLineSpacing24Dots);

                int offset = 0;
                while (offset < image.getHeight()) {
                    mmOutputStream.write(selectBitImageModeCommand);

                    int imageDataLineIndex = 0;
                    byte[] imageDataLine = new byte[3 * image.getWidth()];

                    for (int x = 0; x < image.getWidth(); ++x) {

                        // Remember, 24 dots = 24 bits = 3 bytes.
                        // The 'k' variable keeps track of which of those
                        // three bytes that we're currently scribbling into.
                        for (int k = 0; k < 3; ++k) {
                            byte slice = 0;

                            // A byte is 8 bits. The 'b' variable keeps track
                            // of which bit in the byte we're recording.
                            for (int b = 0; b < 8; ++b) {
                                // Calculate the y position that we're currently
                                // trying to draw. We take our offset, divide it
                                // by 8 so we're talking about the y offset in
                                // terms of bytes, add our current 'k' byte
                                // offset to that, multiple by 8 to get it in terms
                                // of bits again, and add our bit offset to it.
                                int y = (((offset / 8) + k) * 8) + b;

                                // Calculate the location of the pixel we want in the bit array.
                                // It'll be at (y * width) + x.
                                int i = (y * image.getWidth()) + x;

                                // If the image (or this stripe of the image)
                                // is shorter than 24 dots, pad with zero.
                                boolean v = false;
                                if (i < imageBits.length()) {
                                    v = imageBits.get(i);
                                }
                                // Finally, store our bit in the byte that we're currently
                                // scribbling to. Our current 'b' is actually the exact
                                // opposite of where we want it to be in the byte, so
                                // subtract it from 7, shift our bit into place in a temp
                                // byte, and OR it with the target byte to get it into there.
                                slice |= (byte) ((v ? 1 : 0) << (7 - b));
                            }

                            imageDataLine[imageDataLineIndex + k] = slice;

                            // Phew! Write the damn byte to the buffer
                            //printOutput.write(slice);
                        }

                        imageDataLineIndex += 3;
                    }

                    mmOutputStream.write(imageDataLine);
                    offset += 24;
                    mmOutputStream.write(PRINT_AND_FEED_PAPER);
                }


                mmOutputStream.write(setLineSpacing30Dots);
            } catch (IOException ex) {

            }
        }

}
