package com.example.taskerfyp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;

    public MyAdapter(Context c, ArrayList<Post> p) {
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.username.setText(posts.get(position).getCurrent_user_name());
        holder.budget.setText("Budget: " + posts.get(position).getBudget() + " Rs");
        holder.deadline.setText("Deadline: " + posts.get(position).getDeadline() + " day(s)");
        holder.prof_title.setText("Title: " + posts.get(position).getTitle());
        holder.task_description.setText("Description: \n" + posts.get(position).getDescription());
        holder.task_time.setText(posts.get(position).getTime());
        holder.task_date.setText(posts.get(position).getDate());

        Picasso.get().load(posts.get(position).getImage()).placeholder(R.mipmap.ic_profile).into(holder.profile_image);
        holder.btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = posts.get(position).getPostId();
                Intent intent = new Intent(context, EditPostCustomer.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        holder.btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        context);
                builder.setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            //do something
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("All_Posts")
                                        .child(user.getUid())
                                        .child(posts.get(position).getPostId());
                                refrence.removeValue();
                                Toast.makeText(context, "Post Deleted !", Toast.LENGTH_SHORT).show();

                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Current_UID_Post_Counter")
                                        .child(user.getUid()).child("Count");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String value = String.valueOf(dataSnapshot.getValue());
                                        // Toast.makeText(context, ""+value, Toast.LENGTH_SHORT).show();
                                        int n = Integer.parseInt(value);
                                        //Toast.makeText(context, "" + n, Toast.LENGTH_SHORT).show();
                                        int newN = n - 1;
                                        String vvvv = String.valueOf(newN);
                                        //ref.setValue(vvvv);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username, budget, deadline, prof_title, task_time, task_date, task_description;
        CircleImageView profile_image;
        Button btnEditPost, btnDeletePost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile);
            username = itemView.findViewById(R.id.username);
            budget = itemView.findViewById(R.id.budget);
            deadline = itemView.findViewById(R.id.deadline);
            prof_title = itemView.findViewById(R.id.prof_title);
            task_time = itemView.findViewById(R.id.task_time);
            task_date = itemView.findViewById(R.id.task_date);
            task_description = itemView.findViewById(R.id.task_description);
            btnEditPost = itemView.findViewById(R.id.btnEditPost);
            btnDeletePost = itemView.findViewById(R.id.btnDeletePost);
        }
    }
}