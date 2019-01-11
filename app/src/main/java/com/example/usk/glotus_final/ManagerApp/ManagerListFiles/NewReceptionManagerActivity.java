package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.content.Intent;
import android.hardware.camera2.CaptureResult;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.System.Catalog.Adress;
import com.example.usk.glotus_final.System.Catalog.Kontragent;
import com.example.usk.glotus_final.System.Catalog.KontragentNum;
import com.example.usk.glotus_final.System.Catalog.Pochta;
import com.example.usk.glotus_final.System.Catalog.Status;
import com.example.usk.glotus_final.System.loginFiles.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewReceptionManagerActivity extends AppCompatActivity {

    private RelativeLayout rlZakazchik, rlPoluchatel, rlOtpravitel;
    private Button showZakazchik, showPoluchatel, showOtpravitel,btn_dokumenty, btn_sohranit, btn_otmenit;
    private TextView tv_code,tv_date,et_z_pochta,
            //новые поля стоимости
            tv_s_kurs, tv_stavkands, tv_poryadok, tv_costtranp, tv_sebestoimost,
            tv_obshcost;
    private EditText   et_z_dogovor, et_z_otprav,et_p_adres,
            et_z_otkuda, et_z_adres, et_z_kontakt, et_z_telefon, et_p_poluch, et_date_edit,
            et_p_kolich, et_p_ves, et_p_obiem, et_f_kolich, et_f_ves, et_f_obiem, et_info,
            et_vid, et_dostavka, et_stoimost, et_status,et_kommentar,  rkuda, rotkuda;

    // новые поля стоимости
    private Spinner spin_valuta, spin_vidoplaty;

    private AutoCompleteTextView autoComplete_zakazchik, autoComplete_otkuda, autoComplete_kuda;

    private List<String> rlistkuda = new ArrayList<String>();
    private ArrayList<String> rkontr = new ArrayList<String>();
    private ArrayList<String> kontr = new ArrayList<String>();

    private ArrayAdapter<String> kudaAdapter;
    private ArrayAdapter<String> otkudaAdapter;
    private ArrayAdapter<String> zakazchikAdapter;

    private ReceptionData toReception;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_newreception);

        findView();
        setData();
        showZakazchik.setOnClickListener(clickZakaz);
        showPoluchatel.setOnClickListener(clickPoluch);
        showOtpravitel.setOnClickListener(clickOtprav);
//      btn_dokumenty.setOnClickListener(clickDocumenty);
        btn_sohranit.setOnClickListener(clickSohranit);
        btn_otmenit.setOnClickListener(clickOtmena);
        autoComplete_zakazchik.setOnItemClickListener(setP);
    }

    public void setData(){
        tv_code.setText("code");
        tv_date.setText("date");

        int i=0;
        int zk=0;
        for (Map.Entry<String, ?> entry :Kontragent.kontrpreferences.getAll().entrySet()){
            rkontr.add((String) entry.getKey());
             kontr.add((String) entry.getValue());
             i++;
        }
        zakazchikAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,kontr);
        autoComplete_zakazchik.setAdapter(zakazchikAdapter);

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
        kudaAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listkuda);
        autoComplete_kuda.setAdapter(kudaAdapter);

        otkudaAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listkuda);
        autoComplete_otkuda.setAdapter(otkudaAdapter);

        autoComplete_kuda.setText(" ");
        autoComplete_otkuda.setText(" ");
        autoComplete_zakazchik.setText(" ");

//      rkuda.setText("");
//      rotkuda.setText("");
        et_z_pochta.setText("");
//      et_z_dogovor.setText("");
        et_z_otprav.setText("");
        et_z_otkuda.setText("");
        et_z_adres.setText("");
        et_z_kontakt.setText("");
        et_z_telefon.setText("");
        et_p_poluch.setText("");
        et_date_edit.setText("");
        //et_p_kolich.setText("");
        et_p_ves.setText("");
        et_p_obiem.setText("");
        //et_f_kolich.setText("");
        //et_f_ves.setText("");
        /*et_f_obiem.setText("");
        et_info.setText("");
        et_vid.setText("");
        et_dostavka.setText("");
//      et_stoimost.setText("");
        et_status.setText("");
        et_kommentar.setText("");
        et_p_adres.setText("");*/

        //новые поля
        tv_s_kurs.setText("");
        tv_stavkands.setText("");
        tv_poryadok.setText("");
        tv_costtranp.setText("");
        tv_sebestoimost.setText("");
        tv_obshcost.setText("");

        //spinner data..
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

        tv_code=findViewById(R.id.tv_code);
        tv_date=findViewById(R.id.tv_date);
        tv_poryadok=findViewById(R.id.tv_poryadok);
        tv_costtranp=findViewById(R.id.tv_costtranp);
        tv_sebestoimost=findViewById(R.id.tv_sebestoimost);
        tv_obshcost=findViewById(R.id.tv_obshcost);
        tv_s_kurs=findViewById(R.id.tv_s_kurs);
        tv_stavkands=findViewById(R.id.tv_stavkands);
        autoComplete_zakazchik=findViewById(R.id.autoComplete_zakazchik);
        autoComplete_otkuda=findViewById(R.id.autoComplete_otkuda);
        autoComplete_kuda=findViewById(R.id.autoComplete_kuda);

        et_z_pochta=findViewById(R.id.et_z_pochta);

        et_z_otprav=findViewById(R.id.et_z_otprav);
        et_z_otkuda=findViewById(R.id.et_z_otkuda);
        et_z_adres=findViewById(R.id.et_z_adres);
        et_z_kontakt=findViewById(R.id.et_z_kontakt);
        et_z_telefon=findViewById(R.id.et_z_telefon);
        et_p_poluch=findViewById(R.id.et_p_poluch);
        et_p_adres=findViewById(R.id.et_p_adres);
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
//      et_stoimost=findViewById(R.id.et_stoimost);
        et_status=findViewById(R.id.et_status);
        et_kommentar=findViewById(R.id.et_kommentar);

        tv_s_kurs=findViewById(R.id.tv_s_kurs);
        tv_stavkands=findViewById(R.id.tv_stavkands);
        tv_poryadok=findViewById(R.id.tv_poryadok);
        tv_costtranp=findViewById(R.id.tv_costtranp);
        tv_sebestoimost=findViewById(R.id.tv_sebestoimost);
        tv_obshcost=findViewById(R.id.tv_obshcost);

        spin_valuta=findViewById(R.id.spin_valuta);
        spin_vidoplaty=findViewById(R.id.spin_vidopltaty);
    }


    private AdapterView.OnItemClickListener setP = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            et_z_pochta.setText((CharSequence) Pochta.pochtakontr.getAll().get(rkontr.get(kontr.indexOf(autoComplete_zakazchik.getText().toString()))));
            System.out.println(rkontr.get(kontr.indexOf(autoComplete_zakazchik.getText().toString())));
        }

    };

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
            toReception=new ReceptionData(tv_code.getText().toString(),tv_date.getText().toString(),
                    autoComplete_zakazchik.getText().toString(),et_z_pochta.getText().toString(),
                    et_z_otprav.getText().toString(),autoComplete_otkuda.getText().toString(),et_z_adres.getText().toString(),
                    et_z_kontakt.getText().toString(),et_z_telefon.getText().toString(),et_p_poluch.getText().toString(),
                    et_date_edit.getText().toString(),autoComplete_kuda.getText().toString(),et_p_kolich.getText().toString(),
                    et_p_ves.getText().toString(), et_p_obiem.getText().toString(),
                    "obiem",
                    "kolichestvo",
                    "ves",
                    et_info.getText().toString(),
                    et_vid.getText().toString(),
                    et_dostavka.getText().toString(),
                    "stoimost",
                    et_status.getText().toString(),
                    et_kommentar.getText().toString(),
                    et_z_otkuda.getText().toString(),
                    et_z_adres.getText().toString(),
                    et_z_kontakt.getText().toString(),
                    et_z_telefon.getText().toString());


            Intent intent=new Intent(NewReceptionManagerActivity.this, ReceptionManagerActivity.class);
            intent.putExtra("toReceptionData",toReception);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickOtmena = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*String data="{\"АдресПолучателя\":\""+et_p_adres.getText().toString()+"\"," +
                    "\"ВидПеревозки\":\""+mactv_vid.getSelectedItem().toString()+"\","+
                    "\"ВесФакт\":\""+et_f_ves.getText().toString()+"\"," +
                    "\"Date\":\""+ mactv_date_edit.getText().toString()+"\"," +
                    "\"АдресОтправителя\":\""+mactv_z_adres.getText().toString()+"\"," +
                    "\"ВесПлан\":\""+et_p_ves.getText().toString()+"\","+
                    "\"КоличествоПлан\":\""+et_p_kolich.getText().toString()+"\","+
                    "\"КоличествоФакт\":\""+et_f_kolich.getText().toString()+"\","+
                    "\"Комментарий\":\""+et_kommentar.getText().toString()+"\","+
                    "\"КонтактноеЛицоОтправителя\":\""+mactv_z_kontakt.getText().toString()+"\","+
                    "\"КонтактноеЛицоПолучатель\":\""+mactv_p_kontakt.getText().toString()+"\","+
                    "\"Куда_Key\":\""+rlistkuda.get(kudaAdapter.getPosition(autoComplete_otkuda.getText().toString()))+"\","+
                    "\"НаименованиеГруза\":\""+mactv_info.getText().toString()+"\","+
                    "\"ОбъемПлан\":\""+et_p_obiem.getText().toString()+"\","+
                    "\"ОбъемФакт\":\""+et_f_obiem.getText().toString()+"\","+
                    "\"Откуда_Key\":\""+rlistkuda.get(otkudaAdapter.getPosition(autoComplete_otkuda.getText().toString()))+"\","+
                    "\"Отправитель\":\""+mactv_z_otprav.getText().toString()+"\","+
                    "\"Получатель\":\""+mactv_p_poluch.getText().toString()+"\","+
                    "\"ТелефонКонтактногоЛицаПолучателя\":\""+mactv_p_telefon.getText().toString()+"\","+
                    "\"ТелефонКонтактногоЛицоОтправителя\":\""+mactv_z_telefon.getText().toString()+"\","+
                    "\"СтатусЗаказа\":\""+Status.statusy[mactv_status.getSelectedItemPosition()]+"\","+
                    "\"НомерДоговора\":\""+mactv_z_dogovor.getText().toString()+"\""+
                    "\"Заказчик_Key\":\""+rkontr.get(zakazchikAdapter.getPosition(autoComplete_zakazchik.getText().toString()))+"\","+
                    "\"ЦенаПеревозки\":\""+mactv_stoimost.getText().toString()+"\""+
                    "}";
            System.out.println(data);
            RefKeys.Status=Status.statusy[mactv_status.getSelectedItemPosition()];
            RefKeys.ZakazKey=rkontr.get(zakazchikAdapter.getPosition(autoComplete_zakazchik.getText().toString()));
            RefKeys.KudaKey=rlistkuda.get(kudaAdapter.getPosition(autoComplete_kuda.getText().toString()));
            RefKeys.OkudaKey=rlistkuda.get(kudaAdapter.getPosition(autoComplete_otkuda.getText().toString()));
            RefKeys.vidpr=mactv_vid.getSelectedItem().toString();


            String res = process("http://185.209.23.53/InfoBase/odata/standard.odata/Document_%D0%97%D0%B0%D0%BA%D0%B0%D0%B7(guid\'" + RefKeys.Ref_Key + "\')?$format=json", "PATCH", User.getCredential(),
                    data);
            System.out.println(res);*/


        }
    };
}
