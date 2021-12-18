package com.example.taskerfyp.ChatSystem;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mcontext;
    private List<Chat> mChat;
    public static final int MSG_ITEM_RIGHT = 1;
    public static final int MSG_ITEM_LEFT = 0;
    FirebaseUser fUser;

    public MessageAdapter(Context mcontext, List<Chat> mChat) {
        this.mcontext = mcontext;
        this.mChat = mChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_ITEM_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.profile_image1.setImageResource(R.mipmap.ic_profile);
        viewHolder.time.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image1;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image1 = itemView.findViewById(R.id.profile_image1);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fUser.getUid())) {
            return MSG_ITEM_RIGHT;
        } else return MSG_ITEM_LEFT;
    }
}