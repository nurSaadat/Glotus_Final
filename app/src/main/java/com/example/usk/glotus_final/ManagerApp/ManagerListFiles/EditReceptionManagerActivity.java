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

public class EditReceptionManagerActivity extends AppCompatActivity {

    private RelativeLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel, btn_dokumenty, btn_sohranit, btn_otmenit;
    private TextView tv_code,tv_date;
    private EditText et_z_zakazchik, et_z_pochta, et_z_dogovor, et_z_otprav,
            et_z_otkuda, et_z_adres, et_z_kontakt, et_z_telefon, et_p_poluch, et_date_edit,
            et_p_kolich, et_p_ves, et_p_obiem, et_f_kolich, et_f_ves, et_f_obiem, et_info,
            et_vid, et_dostavka, et_stoimost, et_status,et_kommentar;


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

    private View.OnClickListener clickDocumenty = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener clickSohranit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener clickOtmena = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edreception);

        //берет данные с ReceptionManagerActivity
        Intent intent=getIntent();
        ReceptionData recpData=(ReceptionData) intent.getExtras().getSerializable("receptionData");

        rlZakazchik = findViewById(R.id.rl_zakazchik);
        rlOtpravitel = findViewById(R.id.rl_otpravitel);
        rlPoluchatel = findViewById(R.id.rl_poluchatel);
        showOtpravitel = findViewById(R.id.btn_otpravitel);
        showPoluchatel = findViewById(R.id.btn_poluchatel);
        showZakazchik = findViewById(R.id.btn_zakazchik);
        btn_dokumenty= findViewById(R.id.btn_dokumenty);
        btn_sohranit=findViewById(R.id.btn_sohranit);
        btn_otmenit=findViewById(R.id.btn_otmenit);

        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);
        btn_dokumenty.setOnClickListener(clickDocumenty);
        btn_sohranit.setOnClickListener(clickSohranit);
        btn_otmenit.setOnClickListener(clickOtmena);

        tv_code=findViewById(R.id.tv_code);
        tv_date=findViewById(R.id.tv_date);
        et_z_zakazchik=findViewById(R.id.et_z_zakazchik);
        et_z_pochta=findViewById(R.id.et_z_pochta);
        et_z_dogovor=findViewById(R.id.et_z_dogovor);
        et_z_otprav=findViewById(R.id.et_z_otprav);
        et_z_otkuda=findViewById(R.id.et_z_otkuda);
        et_z_adres=findViewById(R.id.et_z_adres);
        et_z_kontakt=findViewById(R.id.et_z_kontakt);
        et_z_telefon=findViewById(R.id.et_z_telefon);
        et_p_poluch=findViewById(R.id.et_p_poluch);
        et_date_edit=findViewById(R.id.et_date_edit);
        et_p_kolich=findViewById(R.id.et_p_kolich);
        et_p_ves=findViewById(R.id.et_p_ves);
        et_p_obiem=findViewById(R.id.et_p_obiem);
        et_f_kolich=findViewById(R.id.et_f_kolich);
        et_f_ves=findViewById(R.id.et_f_ves);
        et_f_obiem=findViewById(R.id.et_f_obiem);
        et_info=findViewById(R.id.et_info);
        et_vid=findViewById(R.id.et_vid);
        et_dostavka=findViewById(R.id.et_dostavka);
        et_stoimost=findViewById(R.id.et_stoimost);
        et_status=findViewById(R.id.et_status);
        et_kommentar=findViewById(R.id.et_kommentar);


        tv_code.setText(recpData.getCode());
        tv_date.setText(recpData.getDate());
        et_z_zakazchik.setText(recpData.getZakazchik());
        et_z_pochta.setText(recpData.getPochta());
        et_z_dogovor.setText(recpData.getDogovor());
        et_z_otprav.setText(recpData.getOtpavitel());
        et_z_otkuda.setText(recpData.getOtkuda());
        et_z_adres.setText(recpData.getAddress());
        et_z_kontakt.setText(recpData.getKontakt());
        et_z_telefon.setText(recpData.getTelefon());
        et_p_poluch.setText(recpData.getPoluchatel());
        et_date_edit.setText(recpData.getDateEdit());
        et_p_kolich.setText(recpData.getPlanKolich());
        et_p_ves.setText(recpData.getPlanVes());
        et_p_obiem.setText(recpData.getPlanObiem());
        et_f_kolich.setText(recpData.getFactKolich());
        et_f_ves.setText(recpData.getFactVes());
        et_f_obiem.setText(recpData.getFactObiem());
        et_info.setText(recpData.getInfo());
        et_vid.setText(recpData.getVid());
        et_dostavka.setText(recpData.getDostavka());
        et_stoimost.setText(recpData.getStoimost());
        et_status.setText(recpData.getStatus());
        et_kommentar.setText(recpData.getKomment());




    }
}
