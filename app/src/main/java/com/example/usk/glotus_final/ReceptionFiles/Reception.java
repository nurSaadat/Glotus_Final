package com.example.usk.glotus_final.ReceptionFiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usk.glotus_final.Catalog.Kontragent;
import com.example.usk.glotus_final.Catalog.Podrazd;
import com.example.usk.glotus_final.Catalog.Transport;
import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserListFiles.ZayavkaListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
* there some elements that
*
* */
public class Reception extends AppCompatActivity {
    private static final String TAG = "a";
    static int cm=0;
    public static ArrayList<String> singleAddress = new ArrayList<String>();
    public static ArrayList<String> adress = new ArrayList<String>();
    static PdfData pd;
    static Spinner upakovka;
    LinearLayout layToHide;
    Spinner docOsnov; //
    Spinner transportType;
    TextView numZakaz, date;
    TextView zakazchik, otpravitel, poluchatel;
    TextView manager,podrazdelenie;
    TextView soprDoc;
    EditText dateToFill;
    EditText vesFact, obiemFact, kolich, komentToFill;
    CheckBox gruz;
    Button save,delete;
    Button etiketka,saveBtn;
    ImageView img,img1,img2;
    RelativeLayout relativeLayout;
    TextView tv_zakazchik_nomer;
    LinearLayout hide_lay;

    boolean ch=false;

    Boolean damage=false;
    String msg= "Уважаемый клиент, обращаем Ваше внимание, что в результате приемки груза были выявлены повреждения упаковки (см. фото),\n" +
            "\n" +
            " по всем вопросам связывайтесь с Вашим менеджером Администратор тел.";
    private String trkey;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        relativeLayout=findViewById(R.id.relativeLay);

        System.out.println(ZayavkaListAdapter.item.getMenedjer());
        setContentView(R.layout.activity_reception);

        hide_lay=findViewById(R.id.hide_lay);


        layToHide=findViewById(R.id.lay_to_hide);


        zakazchik=findViewById(R.id.tv_zakazchic);
        zakazchik.setText(Kontragent.kontragent.get(ZayavkaListAdapter.item.getZakaz()));

        numZakaz=findViewById(R.id.tv_nomer);
        numZakaz.setText(ZayavkaListAdapter.item.getNumber());

        date=findViewById(R.id.tv_data);
        date.setText(ZayavkaListAdapter.item.getDate());

        dateToFill=findViewById(R.id.et_data);
        //as

        vesFact=findViewById(R.id.et_ves_fact);
        obiemFact=findViewById(R.id.et_obem_fact);
        kolich=findViewById(R.id.et_kolich);

        otpravitel=findViewById(R.id.tv_otpravitel);
        otpravitel.setText(ZayavkaListAdapter.item.getSender());

        poluchatel=findViewById(R.id.tv_poluchatel);
        poluchatel.setText(ZayavkaListAdapter.item.getRecept());

        manager=(TextView)findViewById(R.id.tv_menedzher);
        manager.setText(ZayavkaListAdapter.item.getMenedjer());

        tv_zakazchik_nomer=findViewById(R.id.tv_zakazchik_nomer);
        tv_zakazchik_nomer.setText(Kontragent.kontragentnum.get(ZayavkaListAdapter.item.getZakaz()));

        podrazdelenie=findViewById(R.id.tv_podrazdel);
        podrazdelenie.setText(Podrazd.podrazd.get(ZayavkaListAdapter.item.getPodrazd()));

        transportType=findViewById(R.id.spin_transport);
        setTransportSpinner(transportType);

        upakovka=findViewById(R.id.spin_upakovka);
        setUpakovkaSpinner(upakovka);

        komentToFill=findViewById(R.id.et_message_povrezhden);
        gruz=findViewById(R.id.cb_gruz_povrezhden);
        gruz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_cb(view);
            }
        });

        //it it takes a photo of gruz
        img=findViewById(R.id.iw_camera_icon);
        img.setOnClickListener(new View.OnClickListener(){
            public String TAG;

            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(Reception.this, Camera.class);
                startActivityForResult(myIntent,);
                ch=true;

            }

        });
        if(ch==true) {
            if (singleAddress.get(singleAddress.size() - 1) != null) {
                System.out.println(singleAddress.get(singleAddress.size() - 1).toString());
                String path = singleAddress.get(singleAddress.size() - 1).toString();

                File imgFile = new File(path);
                if (imgFile.exists()) {
                    System.out.println("+++++++++++++++");
                    FileInputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(singleAddress.get(singleAddress.size() - 1).toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d(TAG, bitmap.toString());
                    ImageView imageView = new ImageView(Reception.this);
                    imageView.setImageBitmap(bitmap);
                    imageView.setMaxWidth(10);
                    imageView.setMaxHeight(10);
                    hide_lay.addView(imageView);

                }
                ch = false;
            }
            System.out.println("------------------------------------");
        }
        //saves the photos
        save=findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //network
            }
        });

        //here I will change, this is delete button for gruz;
        delete=findViewById(R.id.del);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(singleAddress.size()>0){
                    singleAddress.remove(singleAddress.size()-1);
                }
            }
        });

        soprDoc=findViewById(R.id.sopDoc);
        soprDoc.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent=new Intent(Reception.this, ExpedPage.class);
                startActivity(myIntent);
            }
        });
        etiketka=findViewById(R.id.btn_etiketka);
        etiketka.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent=new Intent(Reception.this, Etiketka.class);
                startActivity(myIntent);
            }
        });
        saveBtn=findViewById(R.id.btn_otpr);
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                posting();
            }
        });
    }

    public void uploadingfile(){
        for(int i=0;i<singleAddress.size();i++){
            System.out.println(singleAddress.get(i).toString());
            String encodedImage="";
            try {
                FileInputStream inputStream = new FileInputStream(singleAddress.get(i).toString());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                encodedImage=imagetobase64(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            upload(encodedImage);
        }
        for (int i=0;i<adress.size();i++){
            System.out.println(adress.get(i).toString());
        }
    }

    public void upload(String encodedImage) {
        String url=("https://promo.serveo.net/uploaded.php");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        encodedImage=encodedImage.replace("/","%2F").replace("+","%2B");
        RequestBody body = RequestBody.create(mediaType, "image="+encodedImage+"&number=18"+ZayavkaListAdapter.item.getNumber().toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    adress.add(response.body().string().replace("<","").replace("\\","\\\\").replace("/","\\\\"));
                } else {
                    System.out.println(response.body().string());
                }
            }
        });
    }

    public String imagetobase64(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    //here some mistakes, look throw
    public void posting(){

            Intent myIntent = new Intent(Reception.this, Etiketka.class);
            startActivity(myIntent);

    }

    public void setTransportSpinner(final Spinner spinner){
        List<String> list = new ArrayList<String>();
        final List<String> rlist = new ArrayList<String>();

        /*
        server=new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Транспорт?$format=json",User.cred);
        String json = (server.get(User.cred));

        JSONArray array = null;
        JSONObject jsonObj=null;

        try {
            jsonObj = new JSONObject(json);
            array = jsonObj.getJSONArray("value");
            list.add("Выберите:");
            array = jsonObj.getJSONArray("value");
            for(int i = 0; i < array.length(); i++) {
                try {
                    list.add(array.getJSONObject(i).getString("Description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            transport=array;
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        list.add("Выберите:");
        for (Map.Entry<String, String> entry : Transport.transport.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            list.add(entry.getValue());
            rlist.add(entry.getKey());
        }




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setSelection(i);
                System.out.println(spinner.getSelectedItem());
                System.out.println(spinner.getSelectedItemPosition());
                if(spinner.getSelectedItemPosition()-1>=0)
                        trkey=rlist.get(spinner.getSelectedItemPosition()-1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setUpakovkaSpinner(final Spinner spinner){
        //you must get the upakovka variables from 1C, and set the spinner
        //like a setTransportSpinner
        final List<String> list = new ArrayList<String>();
        list.add("Выберите:");
        list.add("Коробка");
        list.add("Ящик");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setSelection(i);
                System.out.println(spinner.getSelectedItem());
                System.out.println(spinner.getSelectedItemPosition());
                if(spinner.getSelectedItemPosition()-1>=0)
                    trkey=list.get(spinner.getSelectedItemPosition()-1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void check_cb(View view){
        if(gruz.isChecked()){
            System.out.println("damaged ");
            damage=true;
            komentToFill.setText(msg);
            layToHide.setVisibility(View.VISIBLE);
        }
        else{
            damage=false;
            komentToFill.setText("");
            layToHide.setVisibility(View.GONE);
        }
        System.out.println(damage.toString());
    }


/*
    @Override
    public void onBackPressed() {
        finish();
        Intent myIntent = new Intent(Reception.this, SuperviserListItemActivity.class);
        startActivity(myIntent);
    }*/
}
