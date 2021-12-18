package com.example.taskerfyp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.MapsActivityTasker;
import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.SendMessage;
import com.example.taskerfyp.Offer_Detail_Complete;
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
    public void onBindViewHolder(@NonNull Notification_Tasker_Adapeter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.notification_message.setText(sendMessage.get(position).getMessage());
        holder.message_date.setText(sendMessage.get(position).getTime());
        holder.message_time.setText(sendMessage.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_ki_id = sendMessage.get(position).getPost_id();
                String current_user_ki_id = sendMessage.get(position).getCurrent_user_id();
                String message = sendMessage.get(position).getMessage();
                String time = sendMessage.get(position).getTime();
                String date = sendMessage.get(position).getDate();

                Intent intent = new Intent(context, Offer_Detail_Complete.class);
                intent.putExtra("post_ki_id", post_ki_id);
                intent.putExtra("current_user_ki_id", current_user_ki_id);
                intent.putExtra("message", message);
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                context.startActivity(intent);

            }
        });
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