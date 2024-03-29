package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.ChatSystem.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileByCustomer extends AppCompatActivity {
    TextView name, phone_number, email, gender, profession;
    Button btn_call_this_tasker, btn_chat_this_tasker;
    CircleImageView dpTasker;
    String phone;
    Button btnRatings;
    String tasker_id;
    String name_tasker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_by_customer);

        String tasker_ki_profile_ki_id = getIntent().getStringExtra("tasker_ki_profile_ki_id");
        tasker_id = getIntent().getStringExtra("tasker_ki_profile_ki_id");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("View Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        profession = findViewById(R.id.profession);
        btn_call_this_tasker = findViewById(R.id.btn_call_this_tasker);
        btn_chat_this_tasker = findViewById(R.id.btn_chat_with_tasker);
        dpTasker = findViewById(R.id.dpTasker);
        btnRatings = findViewById(R.id.btnRatings);

        btnRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingsActivity.class);
                intent.putExtra("tasker_id", tasker_id);
                startActivity(intent);
            }
        });

        DatabaseReference mRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(tasker_ki_profile_ki_id);
        mRefrence.keepSynced(true);
        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name_tasker = dataSnapshot.child("taskerUsername").getValue().toString();
                    String email_id = dataSnapshot.child("taskerEmail").getValue().toString();
                    phone = dataSnapshot.child("taskerPhonenumber").getValue().toString();
                    String prof = dataSnapshot.child("taskerProfession").getValue().toString();
                    String gend = dataSnapshot.child("taskerGender").getValue().toString();

                    phone_number.setText(phone);
                    email.setText(email_id);
                    gender.setText(gend);
                    name.setText(name_tasker);
                    profession.setText(prof);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewProfileByCustomer.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(tasker_ki_profile_ki_id).child("image");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String image = dataSnapshot.getValue().toString();
                    Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(dpTasker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_call_this_tasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        btn_chat_this_tasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                String sender_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String reciever_id = getIntent().getStringExtra("tasker_ki_profile_ki_id");
                intent.putExtra("sender_id", sender_id);
                intent.putExtra("reciever_id", reciever_id);
                intent.putExtra("name", name_tasker);
                intent.putExtra("title", "tasker");
                startActivity(intent);
            }
        });
    }
}