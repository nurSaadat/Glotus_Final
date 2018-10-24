package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.Zayavka;

public class ReceptionManagerActivity extends AppCompatActivity {

    private RelativeLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel, btn_dokumenty, btn_izmenit;
    private TextView tv_code, tv_date, tv_z_zakazchik, tv_z_pochta, tv_z_dogovor, tv_z_otprav,
                    tv_z_otkuda, tv_z_adres, tv_z_kontakt, tv_z_telefon, tv_p_poluch, tv_date_edit,
                    tv_p_kolich, tv_p_ves, tv_p_obiem, tv_f_kolich, tv_f_ves, tv_f_obiem, tv_info,
                    tv_vid, tv_dostavka, tv_stoimost, tv_status;
    private EditText et_kommentar;
    private ReceptionData recDate;


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

    private View.OnClickListener clickIzmenit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //сохраняет данные в объект
            recDate=new ReceptionData(tv_code.getText().toString(),tv_date.getText().toString(),
                    tv_z_zakazchik.getText().toString(),tv_z_pochta.getText().toString(),tv_z_dogovor.getText().toString(),
                    tv_z_otprav.getText().toString(),tv_z_otkuda.getText().toString(),tv_z_adres.getText().toString(),
                    tv_z_kontakt.getText().toString(),tv_z_telefon.getText().toString(),tv_p_poluch.getText().toString(),
                    tv_date_edit.getText().toString(),tv_p_kolich.getText().toString(),tv_p_ves.getText().toString(),
                    tv_p_obiem.getText().toString(),tv_f_kolich.getText().toString(),tv_f_ves.getText().toString(),
                    tv_f_obiem.getText().toString(),tv_info.getText().toString(),tv_vid.getText().toString(),
                    tv_dostavka.getText().toString(), tv_stoimost.getText().toString(),tv_status.getText().toString(),
                    et_kommentar.getText().toString());

            //передает их EditReceptionManagerActivity через Intent
            Intent intent=new Intent(ReceptionManagerActivity.this, EditReceptionManagerActivity.class);
            intent.putExtra("receptionData",recDate);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickDockumenty = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

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
        btn_dokumenty=findViewById(R.id.btn_dokumenty);
        btn_izmenit=findViewById(R.id.btn_izmenit);

        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);
        btn_dokumenty.setOnClickListener(clickDockumenty);

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
        tv_date_edit=findViewById(R.id.tv_date_edit);
        tv_p_kolich=findViewById(R.id.tv_p_kolich);
        tv_p_ves=findViewById(R.id.tv_p_ves);
        tv_p_obiem=findViewById(R.id.tv_p_obiem);
        tv_f_kolich=findViewById(R.id.tv_f_kolich);
        tv_f_ves=findViewById(R.id.tv_f_ves);
        tv_f_obiem=findViewById(R.id.tv_f_obiem);
        tv_info=findViewById(R.id.tv_info);
        tv_vid=findViewById(R.id.tv_vid);
        tv_dostavka=findViewById(R.id.tv_dostavka);
        tv_stoimost=findViewById(R.id.tv_stoimost);
        tv_status=findViewById(R.id.tv_status);
        et_kommentar=findViewById(R.id.et_kommentar);

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
        tv_date_edit.setText("date edit");
        tv_p_kolich.setText("plan kolich");
        tv_p_ves.setText("plan ves");
        tv_p_obiem.setText("plan obiem");
        tv_f_kolich.setText("fact kolich");
        tv_f_ves.setText("fact ves");
        tv_f_obiem.setText("fact obiem");
        tv_info.setText("info");
        tv_vid.setText("vid");
        tv_dostavka.setText("dostavka");
        tv_stoimost.setText("stoimost");
        tv_status.setText("status");
        et_kommentar.setText("komment");

        //кнопка изменить
        btn_izmenit.setOnClickListener(clickIzmenit);
    }
}
