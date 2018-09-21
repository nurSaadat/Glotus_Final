package com.example.usk.glotus_final.SuperviserListFiles;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.ReceptionFiles.Reception;
import com.example.usk.glotus_final.connection.ConnectionServer;
import com.example.usk.glotus_final.loginFiles.User;

public class ZayavkaListAdapter extends ArrayAdapter<Zayavka> {

    private static final String TAG = "ZayavkaListAdapter";
    public static Zayavka item;

    private Context mContext;
    int mResource;
    private int lastPosion= -1;

    private static class ViewHolder {
        TextView number;
        TextView date;
        TextView sender;
        TextView recept;
        TextView receptadr;
        TextView senderadr;
        TextView manager;
    }

    /**
     * Default constructor for
     * @param context
     * @param resource
     * @param objects
     */
    public ZayavkaListAdapter(@NonNull Context context, int resource, @NonNull List<Zayavka> objects) {
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

        final Zayavka zayavka=new Zayavka(number,date,sender,recept,senderadr,receptadr,ref_key,zakaz,manager,podrazd);
        final View result;

        ViewHolder holder;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.tv_code);
            holder.date = (TextView) convertView.findViewById(R.id.tv_data);
            holder.sender = (TextView) convertView.findViewById(R.id.tv_otpravitel);
            holder.recept = (TextView) convertView.findViewById(R.id.tv_poluchatel);
            holder.senderadr = (TextView) convertView.findViewById(R.id.tv_otkuda);
            holder.receptadr = (TextView) convertView.findViewById(R.id.tv_kuda);
            holder.manager=(TextView) convertView.findViewById(R.id.tv_manager);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosion=position;

        holder.date.setText(zayavka.getDate());
        holder.number.setText(zayavka.getNumber());
        holder.sender.setText(zayavka.getSender());
        holder.recept.setText(zayavka.getRecept());
        holder.senderadr.setText(getadr(zayavka.getSenderadr()));
        holder.receptadr.setText(getadr(zayavka.getReceptadr()));
        holder.manager.setText(zayavka.getMenedjer());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(zayavka.getNumber());
                System.out.println(zayavka.getRef_key());
                System.out.println("--------------------");
                item = zayavka;
                Intent myIntent = new Intent(mContext, Reception.class);
                mContext.startActivity(myIntent);
            }
        });

        return convertView;
    }

    public String getadr(String mnkey) {
        ConnectionServer mns = new ConnectionServer("http://185.209.21.191/test/odata/standard.odata/Catalog_Адресаты?$format=json&$filter=Ref_Key%20eq%20guid%27" + mnkey + "%27", User.cred);
        JSONArray array = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(mns.get(User.cred));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                return array.getJSONObject(i).getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
