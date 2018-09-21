package com.example.usk.glotus_final.SuperviserListFiles;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.connection.ConnectionServer;
import com.example.usk.glotus_final.loginFiles.User;

public class SuperviserListActivity extends AppCompatActivity {
    // Это форма со списком заявок для супервайзера

    // Log.d are for testing
    private static final String TAG = "SuperviserListActivity";
    // List for zayavkas
    private ArrayList<Zayavka> mZayavkas;

    SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superviser_list);

        // Declaring listView
        ListView list = (ListView) findViewById(R.id.superviser_list);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //mZayavkas.add(new Zayavka("0", "Glotus"));
        Log.d(TAG, "onCreate: Started successfully");
        refresh();

        /*ZayavkaListAdapter adapter = new ZayavkaListAdapter(this, R.layout.superviser_list_item_layout, mZayavkas);
        list.setAdapter(adapter);*/
    }

    public void refresh() {
        // Declaring new array. Сам потом добавь туда заявки. Посмотри сам класс. Сделай констракторы еще.
        mZayavkas = new ArrayList<>();
        //ArrayList<Orders> peopleList = new ArrayList<>();

        ListView mListView = (ListView) findViewById(R.id.superviser_list);

        ConnectionServer server=new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7?$format=json&$filter=СтатусЗаказа%20eq%20%27Новая%27",User.cred);
        String json = (server.get(User.cred));
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
                john = new Zayavka(array.getJSONObject(i).getString("Number"),
                        array.getJSONObject(i).getString("Date"),
                        array.getJSONObject(i).getString("Отправитель"),
                        array.getJSONObject(i).getString("Получатель"),
                        array.getJSONObject(i).getString("Откуда_Key"),
                        array.getJSONObject(i).getString("Куда_Key"),
                        array.getJSONObject(i).getString("Ref_Key"),
                        array.getJSONObject(i).getString("Заказчик_Key"),
                        array.getJSONObject(i).getString("Менеджер_Key"),
                        array.getJSONObject(i).getString("Подразделение_Key"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mZayavkas.add(john);
        }

        ZayavkaListAdapter adapter = new ZayavkaListAdapter(this, R.layout.superviser_list_item_layout, mZayavkas);
        mListView.setAdapter(adapter);

        swipeView.setRefreshing(false);
    }
    String s="";
    public String getmdname(String mnkey){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

        ConnectionServer mns= new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Пользователи?$format=json&$filter=Ref_Key%20eq%20guid%27"+mnkey+"%27",User.cred);
        JSONArray array = null;
        JSONObject jsonObj=null;
        try {
            jsonObj = new JSONObject(mns.get(User.cred));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
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
}
