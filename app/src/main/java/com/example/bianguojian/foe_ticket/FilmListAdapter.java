package com.example.bianguojian.foe_ticket;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/6/12.
 */

public class FilmListAdapter extends BaseAdapter {
    private Context context;
    private List<Film> list;

    public FilmListAdapter(Context context, List<Film> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView;
        ViewHolder viewHolder;

        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.film_item, null);
            viewHolder = new ViewHolder();
            viewHolder.filmPic = (ImageView)convertView.findViewById(R.id.ImageViewFPic);
            viewHolder.filmName = (TextView)convertView.findViewById(R.id.TextViewFName);
            viewHolder.filmIntroduce = (TextView)convertView.findViewById(R.id.TextViewFIntroduce);
            viewHolder.filmActor = (TextView)convertView.findViewById(R.id.TextViewFActor);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Film film = list.get(position);
        viewHolder.filmPic.setImageBitmap(film.getFilmBitmap());
        viewHolder.filmName.setText(film.getFilmName());
        viewHolder.filmIntroduce.setText(film.getFilmIntroduce());
        viewHolder.filmActor.setText(film.getFilmActor());
        return convertView;
    }

    private class ViewHolder {
        public ImageView filmPic;
        public TextView filmName;
        public TextView filmIntroduce;
        public TextView filmActor;
    }
}

