package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.Kontragent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewReceptionManagerActivity extends AppCompatActivity {

    private RelativeLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel,btn_dokumenty, btn_sohranit, btn_otmenit;
    private TextView tv_code,tv_date,
            //новые поля стоимости
            tv_s_kurs, tv_stavkands, tv_poryadok, tv_costtranp, tv_sebestoimost,
            tv_obshcost;
    private EditText  et_z_pochta, et_z_dogovor, et_z_otprav,
            et_z_otkuda, et_z_adres, et_z_kontakt, et_z_telefon, et_p_poluch, et_date_edit,
            et_p_kolich, et_p_ves, et_p_obiem, et_f_kolich, et_f_ves, et_f_obiem, et_info,
            et_vid, et_dostavka, et_stoimost, et_status,et_kommentar,  rkuda, rotkuda;
    private List<String> rkontr = new ArrayList<String>();
    private Spinner et_z_zakazchik,
            // новые поля стоимости
            spin_valuta, spin_vidoplaty;
    private List<String> rlistkuda = new ArrayList<String>();


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_newreception);

        findView();
        setData();
        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);
        btn_dokumenty.setOnClickListener(clickDocumenty);
        btn_sohranit.setOnClickListener(clickSohranit);
        btn_otmenit.setOnClickListener(clickOtmena);
    }

    public void setData(){
        tv_code.setText("code");
        tv_date.setText("date");
        List<String> kontr = new ArrayList<String>();


        int i=0;
        int zk=0;
        for (Map.Entry<String, ?> entry :Kontragent.kontrpreferences.getAll().entrySet()){
            kontr.add((String) entry.getValue());
            rkontr.add((String) entry.getKey());
            if (RefKeys.ZakazKey.equals((String) entry.getKey()))
                zk=i;
            i++;
        }
        et_z_zakazchik.setAdapter( new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,kontr) );
        et_z_zakazchik.setSelection(zk);
        List<String> listkuda = new ArrayList<String>();

        int kda=0,oda=0;
        i=0;
        for (Map.Entry<String, ?> entry : Adress.adresspreferences.getAll().entrySet()) {
            System.out.println((String) entry.getValue());
            listkuda.add((String) entry.getValue());
            rlistkuda.add((String)entry.getKey());
            if (((String)entry.getKey()).equals(RefKeys.KudaKey))
                kda=i;
            if (((String)entry.getKey()).equals(RefKeys.OkudaKey))
                oda=i;
            i++;
        }


        rkuda.setText("");
        rotkuda.setText("");

        et_z_pochta.setText("");
        et_z_dogovor.setText("");
        et_z_otprav.setText("");
        et_z_otkuda.setText("");
        et_z_adres.setText("");
        et_z_kontakt.setText("");
        et_z_telefon.setText("");
        et_p_poluch.setText("");
        et_date_edit.setText("");
        et_p_kolich.setText("");
        et_p_ves.setText("");
        et_p_obiem.setText("");
        et_f_kolich.setText("");
        et_f_ves.setText("");
        et_f_obiem.setText("");
        et_info.setText("");
        et_vid.setText("");
        et_dostavka.setText("");
        et_stoimost.setText("");
        et_status.setText("");
        et_kommentar.setText("");
    }

    public void findView(){
        rlZakazchik = findViewById(R.id.rl_zakazchik);
        rlOtpravitel = findViewById(R.id.rl_otpravitel);
        rlPoluchatel = findViewById(R.id.rl_poluchatel);
        showOtpravitel = findViewById(R.id.btn_otpravitel);
        showPoluchatel = findViewById(R.id.btn_poluchatel);
        showZakazchik = findViewById(R.id.btn_zakazchik);
        btn_dokumenty = findViewById(R.id.btn_dokumenty);
        btn_sohranit = findViewById(R.id.btn_sohranit);
        btn_otmenit = findViewById(R.id.btn_otmenit);

        rkuda = findViewById(R.id.spinner_kuda);
        rotkuda = findViewById(R.id.spinner_otkuda);
        spin_valuta = findViewById(R.id.spin_valuta);
        spin_vidoplaty=findViewById(R.id.spin_vidopltaty);

        tv_code=findViewById(R.id.tv_code);
        tv_date=findViewById(R.id.tv_date);
        tv_poryadok=findViewById(R.id.tv_poryadok);
        tv_costtranp=findViewById(R.id.tv_costtranp);
        tv_sebestoimost=findViewById(R.id.tv_sebestoimost);
        tv_obshcost=findViewById(R.id.tv_obshcost);
        tv_s_kurs=findViewById(R.id.tv_s_kurs);
        tv_stavkands=findViewById(R.id.tv_stavkands);
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
}
