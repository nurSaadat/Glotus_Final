package com.example.usk.glotus_final.loginFiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.usk.glotus_final.Catalog.Adress;
import com.example.usk.glotus_final.Catalog.Mdnames;
import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserListFiles.SuperviserListActivity;
import com.example.usk.glotus_final.connection.ConnectionServer;
import com.example.usk.glotus_final.connection.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import okio.ByteString;

public class SignInActivity extends AppCompatActivity {
    Handler mainHandler = new Handler(Looper.getMainLooper());

    private static final String TAG = "SignInActivity";

    //Creating instance of SharedPreferences in order to "remember" user's login and password
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //Defining all the elements on the page
    private EditText mEmail, mPassword;
    private Button btnLogin;
    private CheckBox mCheckbox;
    private String name = "";
    private String password = "";
    ProgressBar progressBar;
    ProgressBar progressBar2;
    ImageView iv_logo_main;
    TextView textView;

    User user;
    Server server=new Server();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.d(TAG, "onCreate: started");

        //Initializing page elements
        mEmail = (EditText) findViewById(R.id.et_login);
        mPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        mCheckbox = (CheckBox) findViewById(R.id.cb_remember_me);
        iv_logo_main =(ImageView) findViewById(R.id.iv_logo_main);
        progressBar=findViewById(R.id.progressBar);
        progressBar2=findViewById(R.id.progressBar2);
        textView=(TextView) findViewById(R.id.textView);

        textView.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);


        //create a new objects by getting the data from login form
        user=new User(mEmail.getText().toString(),
                mPassword.getText().toString());

        //connect with server

        // For saving user data
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("mydatabase", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        checkSharedPreferences();

        // Сделай метод для входа в систему loginUser()
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: login");
                name = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                loginUser(view);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(netIsAvailable()==false){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInActivity.this,"Ошибка соединения с интернетом!",Toast.LENGTH_SHORT).show();
                            progressing(true);
                        }
                    });
                }
            }
        }).start();


    }

    private void loginUser(View view){
        Log.d(TAG, "loginUser: started");

        // Короче, не трогай. Это для сохранения введенных ранее логина и пароля.
        // Проверяет чекбокс, если да, то сохраняет. Если ранее сохранял, то вытаскивает из памяти.
        // Если не то и не другое, то просто просит заполнить поля.

        // OK

        if(mCheckbox.isChecked()){
            mEditor.putString(getString(R.string.checkbox), "True");
            mEditor.commit();

            name = mEmail.getText().toString();

            mEditor.putString(getString(R.string.email), name);
            mEditor.commit();

            password = mPassword.getText().toString();

            mEditor.putString(getString(R.string.password), password);
            mEditor.commit();
        } else {
            //set a checkbox when the application starts
            mEditor.putString(getString(R.string.checkbox), "False");
            mEditor.commit();
            //save the email
            mEditor.putString(getString(R.string.email), "");
            mEditor.commit();
            //save the password
            mEditor.putString(getString(R.string.password), "");
            mEditor.commit();
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "loginUser: " + name + " " + password);

        //тут продолжи код ( что делать должен? )
        progressing(false);
        textView.setText("Вход в систему");
        progressBar2.setProgress(10);

       /* SignInActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                progressing(false);

            }});
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressing(false);
            }
        }).start();
*/
/*
       // server=new Server("http://185.209.21.191/test/odata/standard.odata",getCredential(name,password));
        server.setUrl("http://185.209.21.191/test/odata/standard.odata?$format=json");
        server.setCredential(getCredential(name,password));
        System.out.println(server.get());
        System.out.println(server.getAns());
        user.setCred(getCredential(name,password));

        check(server.getStatus());*/
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.setUrl("http://185.209.21.191/test/odata/standard.odata?$format=json");
                server.setCredential(getCredential(name,password));
                System.out.println(server.get());
                System.out.println(server.getAns());
                user.setCred(getCredential(name,password));

                check(server.getStatus());
            }
        }).start();

    }


    public void check(Integer status){
        if(status==-1)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SignInActivity.this,"Ошибка соединения с интернетом!",Toast.LENGTH_SHORT).show();
                    progressing(true);
                }
            });
        else if(status==0)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SignInActivity.this,"Не правильный логин или пароль!",Toast.LENGTH_SHORT).show();
                    progressing(true);

                }
            });
        else if(status==1) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SignInActivity.this, "Вы вошли в систему!", Toast.LENGTH_SHORT).show();

                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Авторизовано");
                    progressBar2.setProgress(20);
                }
            });
            getCatalogs();
            finish();
            Intent myIntent = new Intent(SignInActivity.this, SuperviserListActivity.class);
            startActivity(myIntent);
        }
    }
    public void getCatalogs(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("Скачивание каталог Адресаты");
            }
        });

        Server server= new Server("http://185.209.21.191/test/odata/standard.odata/Catalog_Адресаты?$format=json");
        String data=server.get();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar2.setProgress(30);
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
                Adress.adress.put(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar2.setProgress(50);
            }
        });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Скачивание каталог Менеджер");
                }
            });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar2.setProgress(70);
            }
        });


        server.setUrl("http://185.209.21.191/test/odata/standard.odata/Catalog_Пользователи?$format=json");
        data=server.get();

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
                Mdnames.mdnames.put(array.getJSONObject(i).getString("Ref_Key"),array.getJSONObject(i).getString("Description"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar2.setProgress(100);
            }
        });






    }
    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    private void checkSharedPreferences(){
        Log.d(TAG, "checkSharedPreferences: started");
        String checkbox = mPreferences.getString(getString(R.string.checkbox),"False");
        String email = mPreferences.getString(getString(R.string.email),"");
        String password = mPreferences.getString(getString(R.string.password), "");
        mEmail.setText(email);
        mPassword.setText(password);
        if(checkbox.equals("True")){
            mCheckbox.setChecked(true);
        } else {
            mCheckbox.setChecked(false);
        }
    }

    public void progressing(Boolean action){
        //in set False will show ProgressBar
        if(action==false)
        {
            mEmail.setVisibility(View.INVISIBLE);
            mPassword.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            mCheckbox.setVisibility(View.INVISIBLE);

            textView.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        //in set True will login form
        else
        {
            mCheckbox.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

            textView.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
    public String getCredential(String login,String pass){
        try {
            String usernameAndPassword =  login+ ":" + pass;
            byte[] bytes = usernameAndPassword.getBytes("UTF-8");
            String encoded = ByteString.of(bytes).base64();
            return "Basic " + encoded;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
