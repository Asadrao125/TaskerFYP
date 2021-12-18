package com.example.taskerfyp;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Offer_Detail_Complete extends AppCompatActivity {
    private TextView message, timeeeeee, dateeee;
    private Button btnbtnbtn;
    ImageView qr_placeholder;
    private TextView budget_detail, username_detail, description_detail, time_detail, title_detail, date_detail, deadline_Detail;
    String post_ki_id;
    String message_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__detail__complete);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Detail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
        qr_placeholder = findViewById(R.id.qr_placeholder);

        post_ki_id = getIntent().getStringExtra("post_ki_id");
        final String current_user_ki_id = getIntent().getStringExtra("current_user_ki_id");
        message_text = getIntent().getStringExtra("message");
        String time_time = getIntent().getStringExtra("time");
        String date_date = getIntent().getStringExtra("date");

        generateQR();

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Customer")
                .child(current_user_ki_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username_detail.setText("Name: " + dataSnapshot.child("customerUsername").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Posts")
                .child(current_user_ki_id).child(post_ki_id);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title_detail.setText("Title: " + dataSnapshot.child("title").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Offers").child(post_ki_id);
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        description_detail.setText("Description: " + ds.child("offer_description").getValue());
                        deadline_Detail.setText("Deadline: " + ds.child("offer_deadline").getValue() + " (s)");
                        budget_detail.setText("Budget: " + ds.child("offer_budget").getValue());
                        time_detail.setText("Time: " + ds.child("time").getValue());
                        date_detail.setText("Date: " + ds.child("date").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void generateQR() {
        QRGEncoder qrgEncoder = new QRGEncoder(post_ki_id, null, QRGContents.Type.TEXT, 500);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();
        Log.d("qrgencoder", "onClick: " + bitmap);
        qr_placeholder.setImageBitmap(bitmap);
    }
}