package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.TaskerUser;
import com.example.taskerfyp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<TaskerUser> taskerUser;
    String ids;

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Tasker");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    holder.user_ka_nam.setText(taskerUser.get(position).getTaskerUsername());
                    Picasso.get().load(taskerUser.get(position).getImage()).placeholder(R.mipmap.ic_profile).into(holder.DP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DP = itemView.findViewById(R.id.DP);
            user_ka_nam = itemView.findViewById(R.id.user_ka_nam);
        }
    }
}