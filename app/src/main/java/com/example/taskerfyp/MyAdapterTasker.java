package com.example.taskerfyp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterTasker extends RecyclerView.Adapter<MyAdapterTasker.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;

    public MyAdapterTasker(Context c, ArrayList<Post> p) {
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public MyAdapterTasker.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item_tasker, parent, false);
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

        holder.btnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ID: " + posts.get(position).getId(), Toast.LENGTH_SHORT).show();
                String id = posts.get(position).getId();
                Intent intent = new Intent(context, SendOffer.class);
                intent.putExtra("Post_krny_waly_ki_id", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username, budget, deadline, prof_title, task_time, task_date, task_description;
        private CircleImageView profile_image;
        private Button btnSendOffer;

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
            btnSendOffer = itemView.findViewById(R.id.btnSendOffer);
        }
    }
}