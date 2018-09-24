package com.example.usk.glotus_final.ReceptionFiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserListFiles.SuperviserListItemActivity;
import com.example.usk.glotus_final.SuperviserListFiles.ZayavkaListAdapter;
import com.example.usk.glotus_final.connection.ConnectionServer;
import com.example.usk.glotus_final.loginFiles.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    static int cm=0;
    public static ArrayList<String> singleAddress = new ArrayList<String>();
    public static ArrayList<String> adress = new ArrayList<String>();
    ConnectionServer server;
    JSONArray transport = null;
    static PdfData pd;

    LinearLayout layToHide;

    TextView numZakaz;
    TextView date;
    Spinner docOsnov;    //I will change
    EditText dateToFill;
    TextView zakazchik;
    TextView otpravitel;
    TextView poluchatel;
    EditText vesFact;
    EditText obiemFact;
    EditText kolich;
    Spinner upakovka;
    CheckBox gruz;
    EditText komentToFill;
    Button save;
    Button delete;
    TextView manager;
    TextView podrazdelenie;
    Spinner transportType;
    TextView postZakazshik;
    TextView numZakazshik;
    ImageView img;
    ImageView img1;
    ImageView img2;
    TextView soprDoc;
    Button etiketka;
    Button saveBtn;

    Boolean damage=false;
    String msg="\n" +
            " Уважаемый клиент, обращаем Ваше внимание, что в результате приемки груза были выявлены повреждения упаковки (см. фото),\n" +
            "\n" +
            " по всем вопросам связывайтесь с Вашим менеджером Администратор тел.";
    String kkuda="";
    String ootkuda="";
    String trkey="00000000-0000-0000-0000-000000000000";
    String mnkey="00000000-0000-0000-0000-000000000000";
    String pdkey="00000000-0000-0000-0000-000000000000";
    String dckey="00000000-0000-0000-0000-000000000000";
    String zkkey="00000000-0000-0000-0000-000000000000";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        layToHide= findViewById(R.id.lay_to_hide);

        img1=findViewById(R.id.iv_photo1);
        img2=findViewById(R.id.iv_photo2);

        dckey= ZayavkaListAdapter.item.getRef_key();
        zkkey=ZayavkaListAdapter.item.getZakaz();
        mnkey=ZayavkaListAdapter.item.getMenedjer();
        pdkey=ZayavkaListAdapter.item.getPodrazd();

        zakazchik=findViewById(R.id.tv_zakazchic);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                zakazchik.setText(getZakazName(zkkey));
                System.out.println("aaaaa");
            }
        });

        //
        numZakaz=findViewById(R.id.tv_nomer);
        numZakaz.setText(ZayavkaListAdapter.item.getNumber());
        //
        date=findViewById(R.id.tv_data);
        date.setText(ZayavkaListAdapter.item.getDate());
        //
        dateToFill=findViewById(R.id.et_data);
        vesFact=findViewById(R.id.et_ves_fact);
        obiemFact=findViewById(R.id.et_obem_fact);
        kolich=findViewById(R.id.et_kolich);
        //
        otpravitel=findViewById(R.id.tv_otpravitel);
        otpravitel.setText(ZayavkaListAdapter.item.getSender());
        //
        poluchatel=findViewById(R.id.tv_poluchatel);
        poluchatel.setText(ZayavkaListAdapter.item.getRecept());
        //
        manager=findViewById(R.id.tv_manager);
        manager.setText(getManagerName());
        //
        podrazdelenie=findViewById(R.id.tv_podrazdel);
        podrazdelenie.setText(ZayavkaListAdapter.item.getPodrazd());

        //Transport spinner
        transportType=findViewById(R.id.spin_transport);
        setTransportSpinner(transportType);

        //Upakovka spinner;
        upakovka=findViewById(R.id.spin_upakovka);
        setUpakovkaSpinner(upakovka);

        //
        komentToFill=findViewById(R.id.et_message_povrezhden);
        komentToFill.setText("");

        //checkBox for Gruz
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
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(Reception.this, Camera.class);
                startActivity(myIntent);
                for(int i=0;i<singleAddress.size();i++){
                    System.out.println(singleAddress.get(i).toString());
                }
            }
        });

        //saves the photos
        save=findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
                uploadingfile();
            }
        });

        //here I will change, this is delete button for gruz;
        delete=findViewById(R.id.del);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(singleAddress.size()>0){
                    singleAddress.remove(singleAddress.size()-1);
                    //singledata.remove(singledata.size()-1);
                    //foto.setText("Фото:"+singleAddress.size());
                }
            }
        });

        //soprovoditelnyi document
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
        System.out.println("submit");
        System.out.println(mnkey);
        ConnectionServer put = new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Document_ПриемНаСклад?$format=json",User.cred);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dat=dateFormat.format(date).replace('/','-').replace(' ','T');
        System.out.println(dat);
        String iz="";
        if(adress!=null){
            for (int i=0;i<adress.size();i++){
                int p=i+1;
                iz=iz+"{\"LineNumber\":\""+p+"\", \"ПутьКИзображению\":\""+adress.get(i).toString()+"\" }";
                if(i<adress.size()-1)
                    iz=iz+",";
            }
            iz="[ "+iz+" ]";

        }
        String body="{\n" +
                "    \"КоличествоФакт\": \""+kolich.getText().toString()+"\",\n" +
                "    \"ВесФакт\": \""+vesFact.getText().toString()+"\",\n" +
                "    \"ОбъемФакт\": \""+obiemFact.getText().toString()+"\",\n" +
                "    \"ГрузПоврежден\": "+damage.toString()+",\n" +
                "    \"Posted\": true,\n" +
                "    \"Заказчик_Key\": \""+zkkey+"\",\n" +
                "    \"Упаковка\": \""+upakovka.getSelectedItem().toString()+"\",\n" +
                "    \"Date\": \""+dat+"\",\n" +
                "    \"Транспорт_Key\": \""+trkey+"\",\n" +
                "    \"ДокументОснования_Key\": \""+ZayavkaListAdapter.item.getRef_key()+"\",\n" +
                "    \"Менеджер_Key\": \""+mnkey+"\",\n" +
                "    \"Отправитель\": \""+otpravitel.getText().toString()+"\",\n" +
                "    \"Подразделение_Key\": \""+pdkey+"\",\n" +
                "    \"Комментарий\": \""+komentToFill.getText().toString()+"\"\n" +
                "    \"Фото_Type\": \"application/image/jpeg\",\n" +
                "    \"Письмо\": \""+komentToFill.getText().toString()+"\",\n" +
                "    \"ПисьмоОтправлено\": false,\n"+
                //"    \"СопроводительныйДокумент\": \""+sop.getSelectedItem().toString()+"\","+
                "    \"Фото_Base64Data\": \"\""+
                "    \"Изображения\": " +iz+
                /*"       {\n" +
                "       \"LineNumber\": \"1\",\n" +
                 "       \"ПутьКИзображению\": \""+singleAddress.get(0).toString()+"\"\n" +
                "}      \n" +
                "]"+*/
                "    }";
        System.out.println(body);
        System.out.println(ZayavkaListAdapter.item.getNumber());
        System.out.println("___________________________________________");
        System.out.println(put.post(body,User.cred));
        System.out.println(ConnectionServer.status);
        if(ConnectionServer.status==-1)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Reception.this,"Ошибка с соединением с интернетом!",Toast.LENGTH_SHORT).show();

                }
            });
        else if(ConnectionServer.status==0)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Reception.this,"Не правильный логин или пароль!",Toast.LENGTH_SHORT).show();
                }
            });
        else if(ConnectionServer.status==1){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Reception.this,"Принято!",Toast.LENGTH_SHORT).show();
                }
            });
            singleAddress.clear();
            adress.clear();
            pd=new PdfData(getadr(ZayavkaListAdapter.item.getSenderadr()),getadr(ZayavkaListAdapter.item.getReceptadr()),otpravitel.getText().toString(),ZayavkaListAdapter.item.getRecept(),
                    kolich.getText().toString(),vesFact.getText().toString(),obiemFact.getText().toString(),"Машина","Супервайзер",
                    ZayavkaListAdapter.item.getNumber(),dat,"");

            Intent myIntent = new Intent(Reception.this, Etiketka.class);
            startActivity(myIntent);
        }
    }

    public void setTransportSpinner(final Spinner spinner){
        server=new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Транспорт?$format=json",User.cred);
        String json = (server.get(User.cred));

        JSONArray array = null;
        JSONObject jsonObj=null;
        List<String> list = new ArrayList<String>();
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
                    try {
                        System.out.println(transport.getJSONObject(spinner.getSelectedItemPosition()-1).getString("Description"));
                        trkey=transport.getJSONObject(spinner.getSelectedItemPosition()-1).getString("Ref_Key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setUpakovkaSpinner(Spinner spinner){
        //you must get the upakovka variables from 1C, and set the spinner
        //like a setTransportSpinner
    }

    public String getZakazName(String key){
        String json;
        ConnectionServer zkname =new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D1%8B?$format=json&$filter=Ref_Key%20eq%20guid%27"+key+"%27&$select=Description", User.cred);
        json=zkname.get(User.cred);
        JSONArray array = null;
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(json);
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String namezk="";
        for(int i = 0; i < array.length(); i++){
            try {
                namezk=array.getJSONObject(i).getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return namezk;
    }

    public String getManagerName(){
        ConnectionServer mns= new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Пользователи?$format=json&$filter=Ref_Key%20eq%20guid%27"+mnkey+"%27",User.cred);
        JSONArray array = null;
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(mns.get(User.cred));
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < array.length(); i++) {
            try {
                return array.getJSONObject(i).getString("Code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
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

    public String getadr(String mnkey) {
        ConnectionServer mns = new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Адресаты?$format=json&$filter=Ref_Key%20eq%20guid%27" + mnkey + "%27", User.cred);
        JSONArray array = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(mns.get(User.cred));
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                return array.getJSONObject(i).getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(Reception.this, SuperviserListItemActivity.class);
        startActivity(myIntent);
    }
}
