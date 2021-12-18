package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostDetail extends AppCompatActivity {
    DatabaseReference mRef;
    CircleImageView profileImage;
    TextView budget, deadline, taskDescription, taskDate, taskTime, profTitle, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("View Post");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profileImage = findViewById(R.id.profile);
        budget = findViewById(R.id.budget);
        deadline = findViewById(R.id.deadline);
        taskDescription = findViewById(R.id.task_description);
        taskDate = findViewById(R.id.task_date);
        taskTime = findViewById(R.id.task_time);
        profTitle = findViewById(R.id.prof_title);
        username = findViewById(R.id.username);

        mRef = FirebaseDatabase.getInstance().getReference("All_Posts");
        //mRef.keepSynced(true);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        String id = getIntent().getStringExtra("post_ki_id");
                        String usernamePost = String.valueOf(snapshot.child(id).child("current_user_name").getValue());
                        String datePost = String.valueOf(snapshot.child(id).child("date").getValue());
                        String timePost = String.valueOf(snapshot.child(id).child("time").getValue());
                        String titlePost = String.valueOf(snapshot.child(id).child("title").getValue());
                        String descriptionPost = String.valueOf(snapshot.child(id).child("description").getValue());
                        String budgetPost = String.valueOf(snapshot.child(id).child("budget").getValue());
                        String deadlinePost = String.valueOf(snapshot.child(id).child("deadline").getValue());

                        username.setText(usernamePost);
                        taskDate.setText(datePost);
                        taskTime.setText(timePost);
                        profTitle.setText("Title: "+titlePost);
                        taskDescription.setText("Description: \n" + descriptionPost);
                        budget.setText("Budget: " + budgetPost + " Rs");
                        deadline.setText("Deadline: " + deadlinePost + " Day(s)");
                        Log.d("response_kya", "onDataChange: " + snapshot.child(id).getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //////
        String offer_send_krny_waly_ki_id = getIntent().getStringExtra("offer_send_krny_waly_ki_id");
        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(FirebaseAuth.getInstance().getUid()).child("profileimage");
        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String image = dataSnapshot.getValue().toString();
                    Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //////
    }
}