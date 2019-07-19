package com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.ExpedPage;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.Reception;
import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.AutoUpdate;
import com.example.usk.glotus_final.System.Catalog.Mdnames;
import com.example.usk.glotus_final.System.Encryption.AES;
import com.example.usk.glotus_final.System.connection.Server;
import com.example.usk.glotus_final.System.loginFiles.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SuperviserListActivity extends AppCompatActivity {
    // Это форма со списком заявок для супервайзера

    // Log.d are for testing
    private static final String TAG = "SuperviserListActivity";
    // List for zayavkas
    private ArrayList<Zayavka> mZayavkas;

    SwipeRefreshLayout swipeView;
    static Server server;
    TextView etSearch;
    ListView mListView;
    int skip=0;
    int top=100;
    boolean but=false;
    String search=null;
    Button btnLoadExtra;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superviser_list);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        etSearch=findViewById(R.id.etSearch);
        mListView = (ListView) findViewById(R.id.superviser_list);
        // Declaring listView
        ListView list = (ListView) findViewById(R.id.superviser_list);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnLoadExtra = new Button(this);
        progressDialog=new ProgressDialog(SuperviserListActivity.this);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        try {
                            refresh();
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        //mZayavkas.add(new Zayavka("0", "Glotus"));
        swipeView.setRefreshing(true);
        Log.d(TAG, "onCreate: Started successfully");
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {
                    refresh();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        /*ZayavkaListAdapter adapter = new ZayavkaListAdapter(this, R.layout.superviser_list_item_layout, mZayavkas);
        list.setAdapter(adapter);*/
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("/"+s.toString()+"/");
                search=s.toString();
                if (s.toString().equals("")){
                    btnLoadExtra.setText("Показать еще");
                }
                else
                    btnLoadExtra.setText("Найти еще");
                skip=60;
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                search=s.toString();
                if (s.toString().equals(""))
                    btnLoadExtra.setText("Показать еще");
                else
                    btnLoadExtra.setText("Найти еще");

            }
        });

        }
    public void searchItem(String textToSearch){
        search=s.toString();

        final ArrayList<Zayavka> newww = new ArrayList<>();
        System.out.println(textToSearch);
        for(int i=0;i<mZayavkas.size();i++){
            if(mZayavkas.get(i).getSender().toLowerCase().contains(textToSearch.toLowerCase()) || mZayavkas.get(i).getNumber().toLowerCase().contains(textToSearch.toLowerCase()) || mZayavkas.get(i).getRecept().toLowerCase().contains(textToSearch.toLowerCase())){
                newww.add(mZayavkas.get(i));
            }

        }
        initlist(newww);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
        public static String process(String url, String way, String cred, String data) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
            String body=url+","+way+","+cred+"*---*" +data;
            System.out.println(body);
            String string = AES.aesEncryptString(body, "1234567890123456");
            body="data="+string;
            System.out.println(body);
            server = new Server("http://89.219.32.202/odata/demoaes.php",null, body);
            return server.post();
        }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String processM(String url, String way, String cred, String data) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String body=url+","+way+","+cred+"*---*" +data;
        System.out.println(body);
        String string = AES.aesEncryptString(body, "1234567890123456");
        body="data="+string;
        Log.d("aaa",body);
        System.out.println(body);
        Server server;
        server = new Server(url,User.getCredential(), data);
        return server.get();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, JSONException {
        try{
            new AutoUpdate().update();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Declaring new array. Сам потом добавь туда заявки. Посмотри сам класс. Сделай констракторы еще.
        mZayavkas = new ArrayList<>();
        //ArrayList<Orders> peopleList = new ArrayList<>();


        System.out.println(User.cred);
        process("http://89.219.32.202/glotus/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$filter=DeletionMark%20eq%20false&$filter=%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%97%D0%B0%D0%BA%D0%B0%D0%B7%D0%B0%20eq%20%D0%9F%D1%80%D0%B8%D0%BD%D1%8F%D1%82%D0%BD%D0%BE%D0%9D%D0%B0%D0%A1%D0%BA%D0%BB%D0%B0%D0%B4%D0%B5%20or%20%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%97%D0%B0%D0%BA%D0%B0%D0%B7%D0%B0%20eq%20%D0%9D%D0%BE%D0%B2%D0%B0%D1%8F%20or%20%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%97%D0%B0%D0%BA%D0%B0%D0%B7%D0%B0%20eq%20%D0%9E%D1%82%D0%BA%D0%BB%D0%BE%D0%BD%D0%B8%D1%82%D1%8C%20or%20%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%97%D0%B0%D0%BA%D0%B0%D0%B7%D0%B0%20eq%20%D0%9F%D1%80%D0%B8%D0%BD%D1%8F%D1%82%D0%BE%D0%92%D0%A0%D0%B0%D0%B1%D0%BE%D1%82%D1%83&$orderby=Ref_Key%20desc&$skip=0&$top="+top+"","GET",User.getCredential(),"{}");
        skip=60;
        String json = server.getRes();
        System.out.println(json);

        System.out.println(json);

        JSONArray array = null;
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < array.length(); i++){
            try {
                System.out.println((array.getJSONObject(i).getString("Number")+
                        array.getJSONObject(i).getString("Date")+
                        array.getJSONObject(i).getString("Отправитель")+
                        array.getJSONObject(i).getString("Получатель")+
                        array.getJSONObject(i).getString("АдресОтправителя")+
                        array.getJSONObject(i).getString("АдресПолучателя")));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Zayavka john = null;
            try {
               // String otkuda=getadr(array.getJSONObject(i).getString("Откуда_Key"));
               // String kuda=getadr(array.getJSONObject(i).getString("Куда_Key"));
                // String mened=getadr(array.getJSONObject(i).getString("Менеджер_Key"));

                



                john = new Zayavka(array.getJSONObject(i).getString("Number"),
                        array.getJSONObject(i).getString("Date"),
                        array.getJSONObject(i).getString("Отправитель"),
                        array.getJSONObject(i).getString("Получатель"),/**/
                        (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Откуда_Key")),
                        (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Куда_Key")),
                        array.getJSONObject(i).getString("Ref_Key"),
                        array.getJSONObject(i).getString("Заказчик_Key"),
                        (String) Mdnames.mdpreferences.getAll().get(array.getJSONObject(i).getString("Менеджер_Key")),
                        array.getJSONObject(i).getString("Подразделение_Key"),array.getJSONObject(i).getString("СтатусЗаказа"),
                        array.getJSONObject(i).getString("ТелефонКонтактногоЛицаПолучателя"),
                        array.getJSONObject(i).getString("ТелефонКонтактногоЛицоОтправителя"),
                        "", array.getJSONObject(i).getString("НаименованиеГруза"),""


                        );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(
                    array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятноНаСкладе") ||
                            array.getJSONObject(i).getString("СтатусЗаказа").equals("Новая")
                            ||
                            array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятоВРаботу")
                            ||
                            array.getJSONObject(i).getString("СтатусЗаказа").equals("Отклонить")

            )
            mZayavkas.add(john);
        }
      /* for(int i=0;i<10;i++){
           Zayavka a= new Zayavka("a","a","a","a","a","a","a","a","a","a");
           mZayavkas.add(a);
       }*/

        initlist(mZayavkas);


    }
    String s="";
    ZayavkaListAdapter adapter;
    public void initlist(final ArrayList<Zayavka> ppp){

        btnLoadExtra.setText("Load More...");


// Adding Load More button to lisview at bottom

        adapter = new ZayavkaListAdapter(this, R.layout.superviser_list_item_layout, ppp);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setAdapter(adapter);
                if (but==false){
                    mListView.addFooterView(btnLoadExtra);
                    but=true;}

                btnLoadExtra.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View arg0) {
                        try {

                                showmore(ppp);


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


                        adapter.notifyDataSetChanged();



                    }
                });
                swipeView.setRefreshing(false);
            }
        });

    }

    public void findbynumber(){

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showmore(final ArrayList<Zayavka> ppp) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
       //ArrayList<Orders> peopleList = new ArrayList<>();


        String ty="";
        int t=0;
        ty= "&$top="+(top+t);

        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        if (btnLoadExtra.getText().toString().equals("Найти еще")){






            String json=processM(" http://89.219.32.202/glotus/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$filter=Number%20eq%20%27"+etSearch.getText().toString().toUpperCase()+"%27","GET",User.getCredential(),"{}");


            System.out.println(json);

            System.out.println(json);

            JSONArray array = null;
            JSONObject jsonObj=null;
            try {
                jsonObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                array = jsonObj.getJSONArray("value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < array.length(); i++){
                try {
                    System.out.println((array.getJSONObject(i).getString("Number")+
                            array.getJSONObject(i).getString("Date")+
                            array.getJSONObject(i).getString("Отправитель")+
                            array.getJSONObject(i).getString("Получатель")+
                            array.getJSONObject(i).getString("АдресОтправителя")+
                            array.getJSONObject(i).getString("АдресПолучателя")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Zayavka john = null;
                try {
                    // String otkuda=getadr(array.getJSONObject(i).getString("Откуда_Key"));
                    // String kuda=getadr(array.getJSONObject(i).getString("Куда_Key"));
                    // String mened=getadr(array.getJSONObject(i).getString("Менеджер_Key"));





                    john = new Zayavka(array.getJSONObject(i).getString("Number"),
                            array.getJSONObject(i).getString("Date"),
                            array.getJSONObject(i).getString("Отправитель"),
                            array.getJSONObject(i).getString("Получатель"),/**/
                            (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Откуда_Key")),
                            (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Куда_Key")),
                            array.getJSONObject(i).getString("Ref_Key"),
                            array.getJSONObject(i).getString("Заказчик_Key"),
                            (String) Mdnames.mdpreferences.getAll().get(array.getJSONObject(i).getString("Менеджер_Key")),
                            array.getJSONObject(i).getString("Подразделение_Key"),array.getJSONObject(i).getString("СтатусЗаказа"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(
                            array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятноНаСкладе") ||
                                    array.getJSONObject(i).getString("СтатусЗаказа").equals("Новая")
                                    ||
                                    array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятоВРаботу")
                                    ||
                                    array.getJSONObject(i).getString("СтатусЗаказа").equals("Отклонить")

                    )
                        if (btnLoadExtra.getText().toString().equals("Найти еще")){
                            if(john.getSender().toLowerCase().contains(search.toLowerCase()) || john.getNumber().toLowerCase().contains(search.toLowerCase()) || john.getRecept().toLowerCase().contains(search.toLowerCase())){
                                ppp.add(john);
                            }}
                        else {
                            ppp.add(john);

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            progressDialog.dismiss();
        }
        else {

            t=0;
            ty="&$top="+top;


        final String finalTy1 = ty;
        final int finalT = t;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(User.cred);
                try {

                    process("http://89.219.32.202/glotus/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$filter=DeletionMark%20eq%20false&$filter=%D0%A1%D1%82%D0%B0%D1%82%D1%83%D1%81%D0%97%D0%B0%D0%BA%D0%B0%D0%B7%D0%B0%20eq%20%D0%9F%D1%80%D0%B8%D0%BD%D1%8F%D1%82%D0%BD%D0%BE%D0%9D%D0%B0%D0%A1%D0%BA%D0%BB%D0%B0%D0%B4%D0%B5&$filter=DeletionMark%20eq%20%D0%9D%D0%BE%D0%B2%D0%B0%D1%8F&$filter=DeletionMark%20eq%20%D0%9E%D1%82%D0%BA%D0%BB%D0%BE%D0%BD%D0%B8%D1%82%D1%8C&$filter=DeletionMark%20eq%20%D0%9F%D1%80%D0%B8%D0%BD%D1%8F%D1%82%D0%BE%D0%92%D0%A0%D0%B0%D0%B1%D0%BE%D1%82%D1%83&$orderby=Ref_Key%20desc&$skip="+skip+ finalTy1,"GET",User.getCredential(),"{}");
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                skip+=top+finalT;
                String json = server.getRes();
                System.out.println(json);

                System.out.println(json);

                JSONArray array = null;
                JSONObject jsonObj=null;
                try {
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    array = jsonObj.getJSONArray("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < array.length(); i++){
                    try {
                        System.out.println((array.getJSONObject(i).getString("Number")+
                                array.getJSONObject(i).getString("Date")+
                                array.getJSONObject(i).getString("Отправитель")+
                                array.getJSONObject(i).getString("Получатель")+
                                array.getJSONObject(i).getString("АдресОтправителя")+
                                array.getJSONObject(i).getString("АдресПолучателя")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Zayavka john = null;
                    try {
                        // String otkuda=getadr(array.getJSONObject(i).getString("Откуда_Key"));
                        // String kuda=getadr(array.getJSONObject(i).getString("Куда_Key"));
                        // String mened=getadr(array.getJSONObject(i).getString("Менеджер_Key"));





                        john = new Zayavka(array.getJSONObject(i).getString("Number"),
                                array.getJSONObject(i).getString("Date"),
                                array.getJSONObject(i).getString("Отправитель"),
                                array.getJSONObject(i).getString("Получатель"),/**/
                                (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Откуда_Key")),
                                (String) Adress.adresspreferences.getAll().get(array.getJSONObject(i).getString("Куда_Key")),
                                array.getJSONObject(i).getString("Ref_Key"),
                                array.getJSONObject(i).getString("Заказчик_Key"),
                                (String) Mdnames.mdpreferences.getAll().get(array.getJSONObject(i).getString("Менеджер_Key")),
                                array.getJSONObject(i).getString("Подразделение_Key"),array.getJSONObject(i).getString("СтатусЗаказа"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(
                                array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятноНаСкладе") ||
                                        array.getJSONObject(i).getString("СтатусЗаказа").equals("Новая")
                                        ||
                                        array.getJSONObject(i).getString("СтатусЗаказа").equals("ПринятоВРаботу")
                                        ||
                                        array.getJSONObject(i).getString("СтатусЗаказа").equals("Отклонить")

                        )
                        if (btnLoadExtra.getText().toString().equals("Найти еще")){
                            if(john.getSender().toLowerCase().contains(search.toLowerCase()) || john.getNumber().toLowerCase().contains(search.toLowerCase()) || john.getRecept().toLowerCase().contains(search.toLowerCase())){
                                ppp.add(john);
                            }}
                        else {
                            ppp.add(john);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                progressDialog.dismiss();
            }
        }).start();

      /* for(int i=0;i<10;i++){
           Zayavka a= new Zayavka("a","a","a","a","a","a","a","a","a","a");
           mZayavkas.add(a);
       }*/

    } }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
