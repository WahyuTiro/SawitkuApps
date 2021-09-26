package com.sawitku.fadapter;

/**
 * Created by Mohammad Iqbal on 9/7/2016.
 * Email : iqbalhood@gmail.com
 * Ini adalah fungsi setting adapter untuk menyiapkan data yang akan ditampilkan di
 * fragment
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sawitku.fmodels.SearchUser;

import java.util.ArrayList;

import com.sawitku.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MemberAdapter extends ArrayAdapter<SearchUser> {
    ArrayList<SearchUser> MemberList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public MemberAdapter(Context context, int resource, ArrayList<SearchUser> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        MemberList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvId           = (TextView)  v.findViewById(R.id.iv_id);
            holder.tvNama         = (TextView)  v.findViewById(R.id.tv_name);
            holder.tvKabupaten    = (TextView)  v.findViewById(R.id.tv_name2);
            holder.tvProvinsi     = (TextView)  v.findViewById(R.id.tv_name3);
            holder.Foto = (ImageView)v.findViewById(R.id.Foto);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.tvId.setText(MemberList.get(position).getId());
        holder.tvNama.setText(MemberList.get(position).getNama());
        String strProv = MemberList.get(position).getProvinsi();
        String strKab = MemberList.get(position).getKabupaten();
        String strFoto = MemberList.get(position).getFoto();

        if(strProv != null && !strProv.isEmpty()) {
            holder.tvProvinsi.setText(MemberList.get(position).getKabupaten());
        }else{
            holder.tvProvinsi.setText("-----");
        }

        if(strKab != null && !strKab.isEmpty()) {
            holder.tvKabupaten.setText(MemberList.get(position).getProvinsi());
        }else{
            holder.tvKabupaten.setText("-----");
        }

        if(strFoto != null && !strFoto.isEmpty()) {
            Glide.with(getContext()).load(strFoto)
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(holder.Foto);
        }


        return v;

    }


    static class ViewHolder {
        public TextView  tvId;
        public TextView  tvNama;
        public TextView  tvProvinsi;
        public TextView  tvKabupaten;
        public ImageView Foto;
    }











}
