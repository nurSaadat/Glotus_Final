package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.Zayavka;
import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.Mdnames;
import com.example.usk.glotus_final.System.Encryption.AES;
import com.example.usk.glotus_final.System.connection.Server;
import com.example.usk.glotus_final.System.loginFiles.SignInActivity;

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

public class ManagerListActivity extends AppCompatActivity {

    // Удачи, ребят!

    private ImageButton dobavitZayavku;
    static Server server;
    SwipeRefreshLayout swipeView;
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        dobavitZayavku = (ImageButton) findViewById(R.id.btn_dobavit_zayavku);
        dobavitZayavku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Добавить заявку работает", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(ManagerListActivity.this, ReceptionManagerActivity.class);
                startActivity(myIntent);
            }
        });
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
        swipeView.setRefreshing(true);
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
        final ListView list = (ListView) findViewById(R.id.manager_list);
        ArrayList<Zayavka> mZayavkas;
        mZayavkas = new ArrayList<>();


        process("http://185.209.21.191/test/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$orderby=Date%20desc","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
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
                        (String) Adress.preferences.getAll().get(array.getJSONObject(i).getString("Откуда_Key")),
                        (String) Adress.preferences.getAll().get(array.getJSONObject(i).getString("Куда_Key")),
                        array.getJSONObject(i).getString("Ref_Key"),
                        array.getJSONObject(i).getString("Заказчик_Key"),
                        (String) Mdnames.preferences.getAll().get(array.getJSONObject(i).getString("Менеджер_Key")),
                        array.getJSONObject(i).getString("Подразделение_Key"),array.getJSONObject(i).getString("СтатусЗаказа"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mZayavkas.add(john);



    }
       final ManagerListAdapter adapter = new ManagerListAdapter(this, R.layout.manager_list_item_layout, mZayavkas);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
                swipeView.setRefreshing(false);
            }
        });




}

}
