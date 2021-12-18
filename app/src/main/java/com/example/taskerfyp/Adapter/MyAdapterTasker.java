package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.MapsActivityTasker;
import com.example.taskerfyp.Models.CustomerUser;
import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.TaskerUser;
import com.example.taskerfyp.R;
import com.example.taskerfyp.SendOffer;
import com.example.taskerfyp.ViewProfileCustomerByTasker;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MyAdapterTasker extends RecyclerView.Adapter<MyAdapterTasker.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    String id;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.username.setText(posts.get(position).getCurrent_user_name());
        holder.budget.setText("Budget: " + posts.get(position).getBudget() + " Rs");
        holder.deadline.setText("Deadline: " + posts.get(position).getDeadline() + " Day(s)");
        holder.prof_title.setText("Title: " + posts.get(position).getTitle());
        holder.task_description.setText("Description: \n" + posts.get(position).getDescription());
        holder.task_time.setText(posts.get(position).getTime());
        holder.task_date.setText(posts.get(position).getDate());

        holder.btnSendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = posts.get(position).getId();
                String post_id = posts.get(position).getPostId();
                Intent intent = new Intent(context, SendOffer.class);
                intent.putExtra("Post_krny_waly_ki_id", id);
                intent.putExtra("post_ki_id", post_id);
                context.startActivity(intent);
            }
        });

        String postkiid = posts.get(position).getPostId();
        String c_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference A_O_R = FirebaseDatabase.getInstance().getReference("Accepted_Offers").child(c_user);
        A_O_R.child(postkiid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.tv_offer_accepted.setVisibility(View.VISIBLE);
                    holder.btnSendOffer.setBackgroundColor(Color.LTGRAY);
                    holder.btnSendOffer.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.btnTrackingTasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = posts.get(position).getId();
                Intent intent = new Intent(context, MapsActivityTasker.class);
                intent.putExtra("post_krny_waly_ki_iddd", id);
                context.startActivity(intent);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iidd;
                iidd = posts.get(position).getId();
                Intent intent = new Intent(context, ViewProfileCustomerByTasker.class);
                intent.putExtra("Post_krny_waly_ki_id", iidd);
                context.startActivity(intent);
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iidd;
                iidd = posts.get(position).getId();
                Intent intent = new Intent(context, ViewProfileCustomerByTasker.class);
                intent.putExtra("Post_krny_waly_ki_id", iidd);
                context.startActivity(intent);
            }
        });

        FirebaseUser userC = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference onClickRef = FirebaseDatabase.getInstance().getReference("Offer_Sent")
                .child(userC.getUid()).child(posts.get(position).getPostId());
        onClickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.btnSendOffer.setText("Offer Sent");
                    holder.btnSendOffer.setBackgroundColor(Color.LTGRAY);
                    holder.btnSendOffer.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference UsersRef;
        id = posts.get(position).getId();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(id);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(holder.profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username, budget, deadline, prof_title, task_time, task_date, task_description, tv_offer_accepted;
        private CircleImageView profile_image;
        private Button btnSendOffer, btnTrackingTasker;

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
            btnTrackingTasker = itemView.findViewById(R.id.btnTrackingTasker);
            tv_offer_accepted = itemView.findViewById(R.id.tv_offer_accepted);
        }
    }
}