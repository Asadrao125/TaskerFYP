package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
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

public class CustomerWelocmeActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnAddPost, btnViewPost, btnDeleteAccount, btnThemes, btnInviteFriends, btnHelp, btnEditProfile;
    /* TextView tvName;*/
    FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welocme);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("Val", 1);//1 for customer
        editor.apply();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        /* mTitle.setText("Home Customer");*/

        btnAddPost = findViewById(R.id.btnAddPost);
        btnViewPost = findViewById(R.id.btnViewPost);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnThemes = findViewById(R.id.btnThemes);
        btnInviteFriends = findViewById(R.id.btnInviteFriends);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        /*tvName = findViewById(R.id.tvName);*/

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentFirebaseUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTitle.setText("Welcome " + dataSnapshot.child("customerUsername").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "Add Post", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "View Post", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        btnThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "Themes", Toast.LENGTH_SHORT).show();
            }
        });

        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "Invite Friends", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerWelocmeActivity.this, "Delete Account", Toast.LENGTH_SHORT).show();
            }
        });
    }
}