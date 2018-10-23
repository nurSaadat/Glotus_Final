package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.Zayavka;

public class ReceptionManagerActivity extends AppCompatActivity {

    private RelativeLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel;
    private TextView tv_code,tv_date,tv_z_zakazchik,tv_z_pochta,tv_z_dogovor,tv_z_otprav,tv_z_otkuda,tv_z_adres,tv_z_kontakt,tv_z_telefon,tv_p_poluch;


    private View.OnClickListener clickZakaz = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(rlZakazchik.getVisibility()==View.VISIBLE)
                rlZakazchik.setVisibility(rlZakazchik.GONE);
            else
                rlZakazchik.setVisibility(rlZakazchik.VISIBLE);

        }
    };

    private View.OnClickListener clickOtprav = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(rlOtpravitel.getVisibility()==View.VISIBLE)
                rlOtpravitel.setVisibility(View.GONE);
            else
            rlOtpravitel.setVisibility(rlOtpravitel.VISIBLE);

        }
    };

    private View.OnClickListener clickPoluch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (rlPoluchatel.getVisibility()==View.VISIBLE)
                rlPoluchatel.setVisibility(View.GONE);
            else
                rlPoluchatel.setVisibility(rlPoluchatel.VISIBLE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_reception);

        rlZakazchik = findViewById(R.id.rl_zakazchik);
        rlOtpravitel = findViewById(R.id.rl_otpravitel);
        rlPoluchatel = findViewById(R.id.rl_poluchatel);
        showOtpravitel = findViewById(R.id.btn_otpravitel);
        showPoluchatel = findViewById(R.id.btn_poluchatel);
        showZakazchik = findViewById(R.id.btn_zakazchik);

        /*rlZakazchik.setVisibility(rlZakazchik.GONE);
        rlOtpravitel.setVisibility(rlOtpravitel.GONE);
        rlPoluchatel.setVisibility(rlPoluchatel.GONE);
*/
        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);

        tv_code=findViewById(R.id.tv_code);
        tv_date=findViewById(R.id.tv_date);
        tv_z_zakazchik=findViewById(R.id.tv_z_zakazchik);
        tv_z_pochta=findViewById(R.id.tv_z_pochta);
        tv_z_dogovor=findViewById(R.id.tv_z_dogovor);
        tv_z_otprav=findViewById(R.id.tv_z_otprav);
        tv_z_otkuda=findViewById(R.id.tv_z_otkuda);
        tv_z_adres=findViewById(R.id.tv_z_adres);
        tv_z_kontakt=findViewById(R.id.tv_z_kontakt);
        tv_z_telefon=findViewById(R.id.tv_z_telefon);
        tv_p_poluch=findViewById(R.id.tv_p_poluch);

        ManagerZayavka zayavka=ManagerListAdapter.item;

        tv_code.setText(zayavka.getNumber());
        tv_date.setText(zayavka.getDate());
        tv_z_zakazchik.setText(zayavka.getZakaz());
        tv_z_pochta.setText("//Nado dobavit'");
        tv_z_dogovor.setText(zayavka.getNumber()+" ss");
        tv_z_otprav.setText(zayavka.getSender());
        tv_z_otkuda.setText(zayavka.getSenderadr());
        tv_z_adres.setText("//kakoi adress?");
        tv_z_kontakt.setText("//kontaktnoe liso");
        tv_z_telefon.setText("//nomer otpravitelya");
        tv_p_poluch.setText(zayavka.getRecept());






    }
}
