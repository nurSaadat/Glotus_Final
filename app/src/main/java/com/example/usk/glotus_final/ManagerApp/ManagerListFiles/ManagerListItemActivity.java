package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.usk.glotus_final.R;

public class ManagerListItemActivity extends AppCompatActivity {

    private LinearLayout pokazatZayavku, moreInfo;
    private int layoutClickCounter = 0;

    private View.OnClickListener myOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            layoutClickCounter ++;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (layoutClickCounter == 1)
                    {
                        Toast.makeText(getApplicationContext(), "One click!", Toast.LENGTH_SHORT).show();
                        moreInfo.setVisibility(moreInfo.VISIBLE);
                    } else if (layoutClickCounter == 2)
                    {
                        Toast.makeText(getApplicationContext(), "Double click!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);

            layoutClicked();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superviser_list);

        pokazatZayavku = (LinearLayout) findViewById(R.id.ll_pokazatZayavku);
        pokazatZayavku.setOnClickListener(myOnClickListener);

        moreInfo = (LinearLayout) findViewById(R.id.ll_moreInf);
        moreInfo.setVisibility(moreInfo.INVISIBLE);
    }

    private void layoutClicked()
    {
        Toast.makeText(getApplicationContext(),
                "Clickable!", Toast.LENGTH_SHORT).show();
    }
}