package com.example.usk.glotus_final.Catalog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.usk.glotus_final.Encryption.AES;
import com.example.usk.glotus_final.SuperviserListFiles.SuperviserListActivity;
import com.example.usk.glotus_final.connection.Server;
import com.example.usk.glotus_final.loginFiles.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.content.Context.MODE_PRIVATE;

public class Update  {
    public static Server server;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refresh() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        getCatalogs();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCatalogs() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Адресаты");
            }
        });

        process("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%90%D0%B4%D1%80%D0%B5%D1%81%D0%B0%D1%82%D1%8B?$format=json","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
        String data=server.getRes();
        System.out.println(data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(30);
            }
        });
        JSONArray array = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Adress.preferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description")).commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Менеджер");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(40);
            }
        });


        process("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%9F%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B8?$format=json","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
        data=server.getRes();

        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Mdnames.preferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(40);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Контрагенты");
            }
        });

        process("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D1%8B?$format=json","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
        data=server.getRes();

        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Kontragent.preferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description"));
                Kontragent.preferencesnum.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("ТелефонКонтактногоЛица"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Транспорты");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(70);
            }
        });
        process("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%A2%D1%80%D0%B0%D0%BD%D1%81%D0%BF%D0%BE%D1%80%D1%82?$format=json","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
        data=server.getRes();

        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Transport.preferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        process("http://185.209.21.191/test/odata/standard.odata/Catalog_%D0%9F%D0%BE%D0%B4%D1%80%D0%B0%D0%B7%D0%B4%D0%B5%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F?$format=json","GET","Basic 0JDQtNC80LjQvdC40YHRgtGA0LDRgtC+0YA6MTIz","{}");
        data=server.getRes();

        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Podrazd.preferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(100);
            }
        });



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



    public static void check()
    {

    }
}
