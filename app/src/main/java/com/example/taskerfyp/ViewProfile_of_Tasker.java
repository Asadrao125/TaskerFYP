package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile_of_Tasker extends AppCompatActivity {
    TextView name, phone_number, email, gender, profession;
    CircleImageView dpTasker;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_of__tasker);

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
        dpTasker = findViewById(R.id.dpTasker);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(user.getUid());
        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name_tasker = dataSnapshot.child("taskerUsername").getValue().toString();
                    String email_id = dataSnapshot.child("taskerEmail").getValue().toString();
                    phone = dataSnapshot.child("taskerPhonenumber").getValue().toString();
                    String prof = dataSnapshot.child("taskerProfession").getValue().toString();
                    String gend = dataSnapshot.child("taskerGender").getValue().toString();
                    phone_number.setText(phone);
                    email.setText(email_id);
                    gender.setText(gend);
                    name.setText(name_tasker);
                    profession.setText(prof);

                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(dpTasker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewProfile_of_Tasker.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}