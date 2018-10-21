package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.Reception;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.Zayavka;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.ZayavkaListAdapter;

import java.util.ArrayList;

public class ManagerListAdapter extends ArrayAdapter <Zayavka> {

    private Context mContext;
    private int mResource;
    private int lastPosion= -1;
    private boolean b=false;

    private static class ViewHolder {
        TextView number;
        TextView date;
        TextView sender;
        TextView recept;
        TextView receptadr;
        TextView senderadr;
        TextView manager;
        ImageView iv_status;
    }


    /**
     * Deafault constructor for ManagerListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public ManagerListAdapter(@NonNull Context context, int resource, ArrayList<Zayavka> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String date=getItem(position).getDate();
        String number=getItem(position).getNumber();
        String sender=getItem(position).getSender();
        String recept=getItem(position).getRecept();
        String receptadr=getItem(position).getReceptadr();
        String senderadr=getItem(position).getSenderadr();
        String manager=getItem(position).getMenedjer();
        String podrazd=getItem(position).getPodrazd();
        String ref_key=getItem(position).getRef_key();
        String zakaz=getItem(position).getZakaz();
        String status=getItem(position).getStatus();

        final Zayavka zayavka=new Zayavka(number,date,sender,recept,senderadr,receptadr,ref_key,zakaz,manager,podrazd,status);
        final View result;

        final ManagerListAdapter.ViewHolder holder;

        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ManagerListAdapter.ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.tv_code);
            holder.date = (TextView) convertView.findViewById(R.id.tv_data);
            holder.sender = (TextView) convertView.findViewById(R.id.tv_otpravitel);
            holder.recept = (TextView) convertView.findViewById(R.id.tv_poluchatel);
            holder.senderadr = (TextView) convertView.findViewById(R.id.tv_otkuda);
            holder.receptadr = (TextView) convertView.findViewById(R.id.tv_kuda);

            holder.iv_status=(ImageView) convertView.findViewById(R.id.iv_status);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosion=position;


        holder.date.setText("     "+zayavka.getDate().split("T")[0]+"\n     "+zayavka.getDate().split("T")[1]);
        holder.number.setText(zayavka.getNumber());
        holder.sender.setText(zayavka.getSender());
        holder.recept.setText(zayavka.getRecept());
        holder.senderadr.setText(zayavka.getSenderadr());
        holder.receptadr.setText(zayavka.getReceptadr());

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("aaaa");

                if (b==false) {
                    LinearLayout linearLayout = finalConvertView.findViewById(R.id.ll_moreInf);
                    linearLayout.setVisibility(View.VISIBLE);
                    b=true;
                }
                else
                {
                    LinearLayout linearLayout = finalConvertView.findViewById(R.id.ll_moreInf);
                    linearLayout.setVisibility(View.GONE);
                    b=false;

                }

            }
        });


        return convertView;
    }
}
