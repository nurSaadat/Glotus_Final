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
import com.example.usk.glotus_final.System.Catalog.Kontragent;
import com.example.usk.glotus_final.System.DoubleClick;
import com.example.usk.glotus_final.System.DoubleClickListener;

import java.util.ArrayList;

public class ManagerListAdapter extends ArrayAdapter <Zayavka> {

    private Context mContext;
    public static Zayavka item;
    private int mResource;
    private int lastPosion= -1;
    private boolean b=false;
    private LinearLayout lastlinear;


    private static class ViewHolder {
        TextView number;
        TextView date;
        TextView sender;
        TextView recept;
        TextView receptadr;
        TextView senderadr;
        TextView manager;
        TextView iv_status;
        LinearLayout linearLayout;
        TextView zakazname;
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        b=false;

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
            holder.linearLayout=convertView.findViewById(R.id.ll_moreInf);
            holder.iv_status=(TextView) convertView.findViewById(R.id.tv_status);
            holder.zakazname=(TextView)convertView.findViewById(R.id.tv_partner);
            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }




        holder.date.setText("     "+zayavka.getDate().split("T")[0]+"\n     "+zayavka.getDate().split("T")[1]);
        holder.number.setText(zayavka.getNumber());
        holder.sender.setText(zayavka.getSender());
        holder.recept.setText(zayavka.getRecept());
        holder.senderadr.setText(zayavka.getSenderadr());
        holder.receptadr.setText(zayavka.getReceptadr());
        holder.iv_status.setText(zayavka.getStatus());

        holder.zakazname.setText((String) Kontragent.preferences.getAll().get(zayavka.getZakaz()));
        holder.linearLayout.setVisibility(View.GONE);





        convertView.setOnClickListener(new DoubleClick(new DoubleClickListener() {

            @Override
            public void onSingleClick(View view) {
                System.out.println(zayavka.getZakaz());
                System.out.println(Kontragent.preferences.getAll().get(zayavka.getZakaz()));
                System.out.println(Kontragent.preferences.getAll().toString());
                if (lastlinear!=null)
                    lastlinear.setVisibility(View.GONE);
                System.out.println("aaaa");

                if (holder.linearLayout.getVisibility()==View.VISIBLE)
                {
                    holder.linearLayout.setVisibility(View.GONE);
                }
                else
                holder.linearLayout.setVisibility(View.VISIBLE);
                lastlinear=holder.linearLayout;


            }

            @Override
            public void onDoubleClick(View view) {
                Intent myIntent = new Intent(mContext, ReceptionManagerActivity.class);
                mContext.startActivity(myIntent);

            }
        }));



        lastPosion=position;

        return convertView;
    }
}
