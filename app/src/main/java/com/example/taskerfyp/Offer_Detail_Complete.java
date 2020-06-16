package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Offer_Detail_Complete extends AppCompatActivity {
    private TextView message, timeeeeee, dateeee;
    private Button btnbtnbtn;
    private TextView budget_detail, username_detail, description_detail, time_detail, title_detail, date_detail, deadline_Detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__detail__complete);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Detail");

        message = findViewById(R.id.message_message);
        timeeeeee = findViewById(R.id.timeeeeee);
        dateeee = findViewById(R.id.dateeee);
        btnbtnbtn = findViewById(R.id.btnbtnbtn);
        budget_detail = findViewById(R.id.budget_detail);
        username_detail = findViewById(R.id.username_detail);
        description_detail = findViewById(R.id.description_detail);
        time_detail = findViewById(R.id.time_detail);
        title_detail = findViewById(R.id.title_detail);
        date_detail = findViewById(R.id.date_detail);
        deadline_Detail = findViewById(R.id.deadline_detail);

        final String post_ki_id = getIntent().getStringExtra("post_ki_id");
        final String current_user_ki_id = getIntent().getStringExtra("current_user_ki_id");
        String message_text = getIntent().getStringExtra("message");
        String time_time = getIntent().getStringExtra("time");
        String date_date = getIntent().getStringExtra("date");

        message.setText(message_text);
        timeeeeee.setText(time_time);
        dateeee.setText(date_date);

        btnbtnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewProfileCustomerByTasker.class);
                intent.putExtra("Post_krny_waly_ki_id", current_user_ki_id);
                startActivity(intent);
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("All_Posts").child(current_user_ki_id).child(post_ki_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Budget = dataSnapshot.child("budget").getValue().toString();
                    String Username = dataSnapshot.child("current_user_name").getValue().toString();
                    String Date = dataSnapshot.child("date").getValue().toString();
                    String Deadline = dataSnapshot.child("deadline").getValue().toString();
                    String Description = dataSnapshot.child("description").getValue().toString();
                    String Time = dataSnapshot.child("time").getValue().toString();
                    String title = dataSnapshot.child("title").getValue().toString();

                    budget_detail.setText("Budget: " + Budget);
                    time_detail.setText("Time: " + Time);
                    username_detail.setText("Name: " + Username);
                    date_detail.setText("Date: " + Date);
                    deadline_Detail.setText("Deadline: " + Deadline);
                    description_detail.setText("Description: " + Description);
                    title_detail.setText("Title: " + title);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}