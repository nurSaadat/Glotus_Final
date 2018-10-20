package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.example.usk.glotus_final.System.Catalog.Kontragent;
import com.example.usk.glotus_final.System.Catalog.Podrazd;
import com.example.usk.glotus_final.System.Catalog.Transport;
import com.example.usk.glotus_final.System.Encryption.AES;
import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.ZayavkaListAdapter;
import com.example.usk.glotus_final.System.connection.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    Spinner soprDocument; //
    Spinner transportType;
    static TextView foto_kol;
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
    static Integer fotokol=0;

    boolean ch=false;

    Boolean damage=false;
    String msg= "Уважаемый клиент, обращаем Ваше внимание, что в результате приемки груза были выявлены повреждения упаковки (см. фото),\n" +
            "\n" +
            " по всем вопросам связывайтесь с Вашим менеджером Администратор тел.";
    private String trkey="00000000-0000-0000-0000-000000000000";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        relativeLayout=findViewById(R.id.relativeLay);

        System.out.println(ZayavkaListAdapter.item.getMenedjer());
        setContentView(R.layout.activity_reception);

        hide_lay=findViewById(R.id.hide_lay);

        layToHide=findViewById(R.id.lay_to_hide);
        foto_kol=findViewById(R.id.foto_kol);

        zakazchik=findViewById(R.id.tv_zakazchic);
        zakazchik.setText((String)Kontragent.preferences.getAll().get(ZayavkaListAdapter.item.getZakaz()));

        numZakaz=findViewById(R.id.tv_nomer);
        numZakaz.setText(ZayavkaListAdapter.item.getNumber());

        date=findViewById(R.id.tv_data);
        date.setText(ZayavkaListAdapter.item.getDate());

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
        tv_zakazchik_nomer.setText((String)Kontragent.preferencesnum.getAll().get(ZayavkaListAdapter.item.getZakaz()));

        podrazdelenie=findViewById(R.id.tv_podrazdel);
        podrazdelenie.setText((String)Podrazd.preferences.getAll().get(ZayavkaListAdapter.item.getPodrazd()));

        soprDocument=findViewById(R.id.spinner_soprDoc);
        String[] itemsForSop=new String[]{"Транспортная накладная","Товарно-транспортная накладная",
                "Универсально-передаточный документ","Счет фактура","Накладная",
                "Расходная накладная","INVOICE","другое"};
        ArrayAdapter<String> adapterForSop=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, itemsForSop);
        adapterForSop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soprDocument.setAdapter(adapterForSop);

        transportType=findViewById(R.id.spin_transport);
        setTransportSpinner(transportType);

        upakovka=findViewById(R.id.spin_upakovka);
        String[] items=new String[]{"Без упаковки","Ящик","Паллет","Короб","Мешок","Другое"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upakovka.setAdapter(adapter);

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
                startActivity(myIntent);


            }

        });

        //here I will change, this is delete button for gruz;
        delete=findViewById(R.id.del);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(singleAddress.size()>0){
                    singleAddress.remove(singleAddress.size()-1);
                    foto_kol.setText(String.valueOf(singleAddress.size()));
                }
            }
        });

        /*etiketka=findViewById(R.id.btn_etiketka);
        etiketka.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myIntent=new Intent(Reception.this, Etiketka.class);
                startActivity(myIntent);
            }
        });*/

        saveBtn=findViewById(R.id.btn_otpr);
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view){
                try {
                    try {
                        posting();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String process(String url, String way, String cred, String data) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String body=url+","+way+","+cred+"*---*" +data;
        System.out.println(body);
        String string = AES.aesEncryptString(body, "1234567890123456");
        body="data="+string;
        Log.d("aaa",body);
        System.out.println(body);
        Server server;
        server = new Server("http://185.209.21.191/uu/demoaes.php",null, body);
        return server.post();
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
            upload(encodedImage,i+1);
        }
        for (int i=0;i<adress.size();i++){
            System.out.println(adress.get(i).toString());
        }
    }

    public void upload(String encodedImage, int i) {
        encodedImage=encodedImage.replace("/","%2F").replace("+","%2B");
        String body = "image="+encodedImage+"&key="+ZayavkaListAdapter.item.getNumber().toString()+"&iter="+i;
        Server sr= new Server("http://185.209.21.191/uu/uploaded.php",null,body);
        sr.post();
        adress.add(sr.getRes().replace("<","").replace("\\","\\\\").replace("/","\\\\"));
        System.out.println(sr.getRes());

    }

    public String imagetobase64(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    //here some mistakes, look throw
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void posting() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, JSONException {
        final ProgressDialog progressDialog = new ProgressDialog(Reception.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        uploadingfile();

        String images="\"Изображения\" : [";
        for(int i=0;i<adress.size();i++) {
            System.out.println(adress.get(i));
            int c=i+1;
            images += "{\"LineNumber\":  \"" + c + "\", \"ПутьКИзображению\": \"" + adress.get(i) + "\"}";
            if (i!=adress.size() - 1)
                images+=",";
        }
        images+="]";
        System.out.println(images);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime()).replace("_","T");


       String body="{\n" +
                "    \"КоличествоФакт\": \""+kolich.getText().toString()+"\",\n" +
                "    \"ВесФакт\": \""+vesFact.getText().toString()+"\",\n" +
                "    \"ОбъемФакт\": \""+obiemFact.getText().toString()+"\",\n" +
                "    \"ГрузПоврежден\": "+damage.toString()+",\n" +
                "    \"Posted\": true,\n" +
                "    \"Заказчик_Key\": \""+ZayavkaListAdapter.item.getZakaz()+"\",\n" +
                "    \"Упаковка\": \""+upakovka.getSelectedItem().toString()+"\",\n" +
                "    \"Date\": \""+timeStamp+"\"    ,\n" +
                "    \"Транспорт_Key\": \""+trkey+"\",\n" +
                "    \"ДокументОснования_Key\": \""+ZayavkaListAdapter.item.getRef_key()+"\",\n" +
              //      "    \"Менеджер_Key\": \""+ZayavkaListAdapter.item.getMenedjer()+"\",\n" +
                "    \"Отправитель\": \""+ZayavkaListAdapter.item.getSender()+"\",\n" +
                "    \"Подразделение_Key\": \""+ZayavkaListAdapter.item.getPodrazd()+"\",\n" +
             //   "    \"Комментарий\": \""+comment.getText().toString()+"\"\n" +
                "    \"Фото_Type\": \"application/image/jpeg\",\n" +
                "    \"Письмо\": \""+komentToFill.getText().toString()+"\",\n" +
                "    \"ПисьмоОтправлено\": false,\n"+
                "    \"СопроводительныйДокумент\": \""+soprDocument.getSelectedItem().toString()+"\","+
                     images+
                " }";
       Log.d("aa",body);

            String res=process("http://185.209.21.191/test/odata/standard.odata/Document_%D0%9F%D1%80%D0%B8%D0%B5%D0%BC%D0%9D%D0%B0%D0%A1%D0%BA%D0%BB%D0%B0%D0%B4?$format=json","POST","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz",body);
        System.out.println(res);
        JSONArray array = null;
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObj.getString("Ref_Key").toString());

            res=process("http://185.209.21.191/test/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7(guid\'"+ZayavkaListAdapter.item.getRef_key()+"\')?$format=json","PATCH","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz",
                    "{\"ДокументПриемГруза_Key\": \""+jsonObj.getString("Ref_Key").toString()+"\",\"СтатусЗаказа\": \"ПринятноНаСкладе\",\"ВесФакт\":"+vesFact.getText().toString()+",\"ОбъемФакт\": "+obiemFact.getText().toString()+",\"КоличествоФакт\": "+kolich.getText().toString()+"}");

        System.out.println(res);
            pd=new PdfData(ZayavkaListAdapter.item.getSenderadr(),ZayavkaListAdapter.item.getReceptadr(),ZayavkaListAdapter.item.getRecept(),ZayavkaListAdapter.item.getSender(),kolich.getText().toString(),vesFact.getText().toString(),
                    obiemFact.getText().toString(),"AUTO","RASP", ZayavkaListAdapter.item.getNumber().toString(),ZayavkaListAdapter.item.getDate().toString(),
                    " ");


            Intent myIntent = new Intent(Reception.this, Etiketka.class);
            startActivity(myIntent);

    }

    public void setTransportSpinner(final Spinner spinner){
        List<String> list = new ArrayList<String>();
        final List<String> rlist = new ArrayList<String>();

        list.add("Выберите:");
        for (Map.Entry<String, ?> entry : Transport.preferences.getAll().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            list.add((String) entry.getValue());
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
