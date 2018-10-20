package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.Zayavka;

import java.util.ArrayList;

public class ManagerListActivity extends AppCompatActivity {

    // Удачи, ребят!
    private ArrayList<Zayavka> mZayavkas;
    private ImageButton dobavitZayavku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);

        ListView list = (ListView) findViewById(R.id.manager_list);

        Zayavka z1 = new Zayavka();
        mZayavkas = new ArrayList<>();

        ManagerListAdapter adapter = new ManagerListAdapter(this, R.layout.activity_manager_list, mZayavkas);
        list.setAdapter(adapter);

        dobavitZayavku = (ImageButton) findViewById(R.id.btn_dobavit_zayavku);
        dobavitZayavku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Добавить заявку работает", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
