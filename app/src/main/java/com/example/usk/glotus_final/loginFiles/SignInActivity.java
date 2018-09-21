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
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserListFiles.SuperviserListActivity;
import com.example.usk.glotus_final.connection.ConnectionServer;

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

    User user;
    ConnectionServer server;
    static int status;
    public static String json="";
    public static boolean pr=false;


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
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //create a new objects by getting the data from login form
        user=new User(mEmail.getText().toString(),
                mPassword.getText().toString());

        //connect with server
        server=new ConnectionServer("http://185.209.21.191/test/odata/standard.odata?$format=json",
                user.getCredential());

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
        SignInActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                progressing(false);

            }});
        update();
        System.out.println("signing");
        System.out.println(user.getCredential());
        System.out.println("-----------");

        json=server.get(user.getCredential());
        System.out.println(json);

        status=ConnectionServer.status;
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
            pr = true;
            finish();
            Intent myIntent = new Intent(SignInActivity.this, SuperviserListActivity.class);
            startActivity(myIntent);
        }
        //

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

    public void update(){
        user.setUsername(mEmail.getText().toString());
        user.setPassword(mPassword.getText().toString());
        server.setUrl("http://185.209.21.191/test/odata/standard.odata?$format=json");
        server.setCredential(user.getCredential());
    }

    public void progressing(Boolean action){
        //in set False will show ProgressBar
        if(action==false)
        {
            mEmail.setVisibility(View.INVISIBLE);
            mPassword.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        //in set True will login form
        else
        {
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
