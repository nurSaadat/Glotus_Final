package com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.Mdnames;
import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.System.connection.ConnectionServer;
import com.example.usk.glotus_final.System.connection.Server;
import com.example.usk.glotus_final.System.Encryption.AES;
import com.example.usk.glotus_final.System.loginFiles.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        }
    public void searchItem(String textToSearch){
        ArrayList<Zayavka> newww = new ArrayList<>();
        System.out.println(textToSearch);
        for(int i=0;i<mZayavkas.size();i++){

            if(mZayavkas.get(i).getSender().contains(textToSearch) || mZayavkas.get(i).getNumber().contains(textToSearch) || mZayavkas.get(i).getRecept().contains(textToSearch)){

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
            server = new Server("http://185.209.21.191/uu/demoaes.php",null, body);
            return server.post();
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // Declaring new array. Сам потом добавь туда заявки. Посмотри сам класс. Сделай констракторы еще.
        mZayavkas = new ArrayList<>();
        //ArrayList<Orders> peopleList = new ArrayList<>();


        System.out.println(User.cred);
        process("http://185.209.21.191/test/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$orderby=Date%20desc","GET",User.getCredential(),"{}");
        String json = server.getRes();


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
            mZayavkas.add(john);
        }
      /* for(int i=0;i<10;i++){
           Zayavka a= new Zayavka("a","a","a","a","a","a","a","a","a","a");
           mZayavkas.add(a);
       }*/

        initlist(mZayavkas);


    }
    String s="";

    public void initlist(ArrayList<Zayavka> ppp){
        final ZayavkaListAdapter adapter = new ZayavkaListAdapter(this, R.layout.superviser_list_item_layout, ppp);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListView.setAdapter(adapter);
                swipeView.setRefreshing(false);
            }
        });
    }


}
