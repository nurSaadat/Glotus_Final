package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.Transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditReceptionManagerActivity extends AppCompatActivity {

    private LinearLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel, btn_dokumenty, btn_sohranit, btn_otmenit;
    private TextView tv_code,tv_date;
    private MultiAutoCompleteTextView mactv_z_zakazchik, mactv_z_pochta, mactv_z_dogovor, mactv_z_otprav,
            mactv_z_otkuda, mactv_z_adres, mactv_z_kontakt, mactv_z_telefon, mactv_p_poluch, mactv_date_edit,
            mactv_info,
            mactv_vid, mactv_dostavka, mactv_stoimost, mactv_status,mactv_p_otkuda,mactv_p_adres,mactv_p_kontakt,mactv_p_telefon;
    private EditText et_p_kolich, et_p_ves, et_p_obiem, et_f_kolich, et_f_ves, et_f_obiem,et_kommentar;
    private ReceptionData returnData;
    private ReceptionData recpData;
    private Spinner mactv_kuda,mactv_otkuda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edreception);

        findView();

        //берет данные с ReceptionManagerActivity
        Intent intent=getIntent();
        recpData=(ReceptionData) intent.getExtras().getSerializable("receptionData");

        setData();

        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);
        btn_dokumenty.setOnClickListener(clickDocumenty);
        btn_otmenit.setOnClickListener(clickOtmena);

        //для "куда", надо брать данные с базы, и сохранить в лист

        List<String> list = new ArrayList<String>();
        final List<String> rlist = new ArrayList<String>();

        for (Map.Entry<String, ?> entry : Adress.adresspreferences.getAll().entrySet()) {
            System.out.println((String) entry.getValue());
            list.add((String) entry.getValue());
            rlist.add(entry.getKey());
        }
        ArrayAdapter<String> kudaAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        mactv_kuda.setAdapter(kudaAdapter);


        //для "откуда", надо брать данные с базы, и сохранить в лист

        ArrayAdapter<String> otkudaAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
        mactv_otkuda.setAdapter(otkudaAdapter);


        btn_sohranit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSohranitButtonClick();
            }
        });
    }

    //возвращает измененные данные в ReceptionManager
    public void onSohranitButtonClick(){
        returnData=new ReceptionData(tv_code.getText().toString(),tv_date.getText().toString(),
                mactv_z_zakazchik.getText().toString(),mactv_z_pochta.getText().toString(),mactv_z_dogovor.getText().toString(),
                mactv_z_otprav.getText().toString(),mactv_otkuda.getSelectedItem().toString(),mactv_z_adres.getText().toString(),
                mactv_z_kontakt.getText().toString(),mactv_z_telefon.getText().toString(),mactv_p_poluch.getText().toString(),
                mactv_date_edit.getText().toString(),mactv_kuda.getSelectedItem().toString(),et_p_kolich.getText().toString(),et_p_ves.getText().toString(),
                et_p_obiem.getText().toString(),et_f_kolich.getText().toString(),et_f_ves.getText().toString(),
                et_f_obiem.getText().toString(),mactv_info.getText().toString(),mactv_vid.getText().toString(),
                mactv_dostavka.getText().toString(), mactv_stoimost.getText().toString(),mactv_status.getText().toString(),
                et_kommentar.getText().toString(),
                mactv_p_otkuda.getText().toString(),mactv_p_adres.getText().toString(),mactv_p_kontakt.getText().toString(),
                mactv_p_telefon.getText().toString());
        Intent returnIntent=new Intent();
        returnIntent.putExtra("changedData", returnData);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void setData(){
        tv_code.setText(recpData.getCode());
        tv_date.setText(recpData.getDate());
        mactv_z_zakazchik.setText(recpData.getZakazchik());
        mactv_z_pochta.setText(recpData.getPochta());
        mactv_z_dogovor.setText(recpData.getDogovor());
        mactv_z_otprav.setText(recpData.getOtpavitel());
        mactv_z_otkuda.setText(recpData.getOtkuda());
        mactv_z_adres.setText(recpData.getAddress());
        mactv_z_kontakt.setText(recpData.getKontakt());
        mactv_z_telefon.setText(recpData.getTelefon());
        mactv_p_poluch.setText(recpData.getPoluchatel());
        mactv_date_edit.setText(recpData.getDateEdit());
        mactv_otkuda.setPrompt(recpData.getOtkuda());
        mactv_kuda.setPrompt(recpData.getKuda());
        et_p_kolich.setText(recpData.getPlanKolich());
        et_p_ves.setText(recpData.getPlanVes());
        et_p_obiem.setText(recpData.getPlanObiem());
        et_f_kolich.setText(recpData.getFactKolich());
        et_f_ves.setText(recpData.getFactVes());
        et_f_obiem.setText(recpData.getFactObiem());
        mactv_info.setText(recpData.getInfo());
        mactv_vid.setText(recpData.getVid());
        mactv_dostavka.setText(recpData.getDostavka());
        mactv_stoimost.setText(recpData.getStoimost());
        mactv_status.setText(recpData.getStatus());
        et_kommentar.setText(recpData.getKomment());


        mactv_p_otkuda.setText(recpData.getOtkuda());
        mactv_p_adres.setText(recpData.getTv_p_adres());
        mactv_p_kontakt.setText(recpData.getTv_p_kontakt());
        mactv_p_telefon.setText(recpData.getTv_p_telefon());
    }

    public void findView(){
        rlZakazchik = findViewById(R.id.rl_zakazchik);
        rlOtpravitel = findViewById(R.id.rl_otpravitel);
        rlPoluchatel = findViewById(R.id.rl_poluchatel);
        showOtpravitel = findViewById(R.id.btn_otpravitel);
        showPoluchatel = findViewById(R.id.btn_poluchatel);
        showZakazchik = findViewById(R.id.btn_zakazchik);
        btn_dokumenty= findViewById(R.id.btn_dokumenty);
        btn_sohranit=findViewById(R.id.btn_sohranit);
        btn_otmenit=findViewById(R.id.btn_otmenit);

        tv_code=findViewById(R.id.tv_code);
        tv_date=findViewById(R.id.tv_date);
        mactv_z_zakazchik=findViewById(R.id.mactv_z_zakazchik);
        mactv_z_pochta=findViewById(R.id.mactv_z_pochta);
        mactv_z_dogovor=findViewById(R.id.mactv_z_dogovor);
        mactv_z_otprav=findViewById(R.id.mactv_z_otprav);
        mactv_z_otkuda=findViewById(R.id.mactv_z_otkuda);
        mactv_z_adres=findViewById(R.id.mactv_z_adres);
        mactv_z_kontakt=findViewById(R.id.mactv_z_kontakt);
        mactv_z_telefon=findViewById(R.id.mactv_z_telefon);
        mactv_p_poluch=findViewById(R.id.mactv_p_poluch);
        mactv_date_edit=findViewById(R.id.mactv_date_edit);
        et_p_kolich=findViewById(R.id.et_p_kolich);
        et_p_ves=findViewById(R.id.et_p_ves);
        et_p_obiem=findViewById(R.id.et_p_obiem);
        et_f_kolich=findViewById(R.id.et_f_kolich);
        et_f_ves=findViewById(R.id.et_f_ves);
        et_f_obiem=findViewById(R.id.et_f_obiem);
        mactv_otkuda=findViewById(R.id.mactv_otkuda);
        mactv_kuda=findViewById(R.id.mactv_kuda);
        mactv_info=findViewById(R.id.mactv_info);
        mactv_vid=findViewById(R.id.mactv_vid);
        mactv_dostavka=findViewById(R.id.mactv_dostavka);
        mactv_stoimost=findViewById(R.id.mactv_stoimost);
        mactv_status=findViewById(R.id.mactv_status);
        et_kommentar=findViewById(R.id.et_kommentar);

        mactv_p_otkuda=findViewById(R.id.mactv_p_otkuda);
        mactv_p_adres=findViewById(R.id.mactv_p_adres);
        mactv_p_kontakt=findViewById(R.id.mactv_p_kontakt);
        mactv_p_telefon=findViewById(R.id.mactv_p_telefon);
    }

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

    private View.OnClickListener clickOtmena = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(EditReceptionManagerActivity.this,ReceptionManagerActivity.class);
            startActivity(intent);
        }
    };
}
