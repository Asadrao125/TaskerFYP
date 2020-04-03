package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.SendMessage;
import com.example.taskerfyp.R;

import java.util.ArrayList;

public class Notification_Tasker_Adapeter extends RecyclerView.Adapter<Notification_Tasker_Adapeter.MyViewHolder> {
    Context context;
    ArrayList<SendMessage> sendMessage;

    public Notification_Tasker_Adapeter(Context c, ArrayList<SendMessage> message) {
        context = c;
        sendMessage = message;
    }

    @NonNull
    @Override
    public Notification_Tasker_Adapeter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notifications_tasker, parent, false);
        return new Notification_Tasker_Adapeter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_Tasker_Adapeter.MyViewHolder holder, int position) {
        holder.notification_message.setText(sendMessage.get(position).getMessage());
        holder.message_date.setText(sendMessage.get(position).getTime());
        holder.message_time.setText(sendMessage.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return sendMessage.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView notification_message, message_time, message_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_message = itemView.findViewById(R.id.notification_message_tv);
            message_time = itemView.findViewById(R.id.message_time);
            message_date = itemView.findViewById(R.id.message_date);
        }
    }
}