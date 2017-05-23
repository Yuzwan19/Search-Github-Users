package com.yuzwan.githubapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuzwan.githubapi.R;
import com.yuzwan.githubapi.model.UserModel;

import java.util.ArrayList;

/**
 * Created by user on 23/05/2017.
 */

public class UserAdapter extends BaseAdapter {
    // params
    ArrayList listItem;
    Activity activity;

    public UserAdapter(Activity activity, ArrayList listItem){
        this.activity = activity;
        this.listItem = listItem;
    }

    //method ini akan menentukan seberapa banyak data yang akan ditampilkan didalam ListView
    //listItem.size() == jumlah data dalam object List yang ada
    @Override
    public int getCount() {
        return listItem.size();
    }

    //method ini untuk mengakses per-item objek dalam list
    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //method ini yang akan menampilkan baris per baris dari item yang ada di ListView
    //dengan menggunakan pattern ViewHolder untuk optimasi performa dari ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list, null);
            holder.txtUser = (TextView)view.findViewById(R.id.txt_user);
            holder.imgUser = (ImageView)view.findViewById(R.id.img_user);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        UserModel user = (UserModel)getItem(position);
        holder.txtUser.setText(user.getUsername());

        Picasso.with(activity).load(user.getImage()).placeholder(android.R.color.darker_gray)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgUser);

        return view;
    }

    static class ViewHolder{
        ImageView imgUser;
        TextView txtUser;
    }
}




