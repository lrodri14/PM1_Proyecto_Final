package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotificacionCustomAdapter extends BaseAdapter {

    private List<Notificacion> notifications;
    private LayoutInflater inflater;

    public NotificacionCustomAdapter(Context context, List<Notificacion> notifications) {
        this.notifications = notifications;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_notificacion, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_not);
            holder.nameTextView = convertView.findViewById(R.id.nameText);
            holder.subjectTextView = convertView.findViewById(R.id.AsuntoText);
            holder.userNameTextView = convertView.findViewById(R.id.UserText);
            holder.timeTextView = convertView.findViewById(R.id.timeText);
            holder.dateTextView = convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Notificacion notificacion = (Notificacion) getItem(position);
        holder.imageView.setImageResource(notificacion.getImage());
        holder.nameTextView.setText(notificacion.getName());
        holder.subjectTextView.setText(notificacion.getSubject());
        holder.userNameTextView.setText(notificacion.getUserName());
        holder.timeTextView.setText(notificacion.getTime());
        holder.dateTextView.setText(notificacion.getDate());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView subjectTextView;
        TextView userNameTextView;
        TextView timeTextView;
        TextView dateTextView;
    }
}


