package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.ChatSystem.Inbox;
import com.example.taskerfyp.ChatSystem.MessageActivity;
import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.TaskerUser;
import com.example.taskerfyp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<TaskerUser> taskerUser;

    public UserAdapter(Context c, ArrayList<TaskerUser> tU) {
        context = c;
        taskerUser = tU;
    }


    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new UserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //////
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child("Tasker");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.user_ka_nam.setText(taskerUser.get(position).getTaskerUsername());
                holder.job_title.setText(taskerUser.get(position).getTaskerProfession());
                Picasso.get().load(taskerUser.get(position).getImage()).placeholder(R.mipmap.ic_profile).into(holder.DP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //////


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + taskerUser.get(position).getTaskerUsername(), Toast.LENGTH_SHORT).show();

                String sender_id = FirebaseAuth.getInstance().getUid();
                String reciever_id = taskerUser.get(position).getTaskerID();

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("sender_id", sender_id);
                intent.putExtra("reciever_id", reciever_id);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return taskerUser.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user_ka_nam;
        private CircleImageView DP;
        private TextView job_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DP = itemView.findViewById(R.id.DP);
            user_ka_nam = itemView.findViewById(R.id.user_ka_nam);
            job_title = itemView.findViewById(R.id.job_title);
        }
    }
}