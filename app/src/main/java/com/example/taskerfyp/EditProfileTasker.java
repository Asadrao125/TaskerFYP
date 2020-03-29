package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditProfileTasker extends AppCompatActivity {
    CircleImageView TaskerEditProfileImage;
    EditText TaskerEditProfileUsername, TaskerEditProfilePhone, TaskerEditProfileEmail, TaskerEditProfilePassword;
    Spinner TaskerEditProfileGender, TaskerEditProfileProfession;
    Button TaskerEditProfileUpdateButton;
    DatabaseReference TaskerEditProfileRefrence;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_tasker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Profile");

        initilise();
        settingDataInEditText();
    }

    public void initilise() {
        TaskerEditProfileImage = findViewById(R.id.CustomerEditProfileImage);
        TaskerEditProfileUsername = findViewById(R.id.CustomerEditProfileUsername);
        TaskerEditProfilePhone = findViewById(R.id.CustomerEditProfilePhone);
        TaskerEditProfileEmail = findViewById(R.id.CustomerEditProfileEmail);
        TaskerEditProfilePassword = findViewById(R.id.CustomerEditProfilePassword);
        TaskerEditProfileGender = findViewById(R.id.CustomerEditProfileGender);
        TaskerEditProfileProfession = findViewById(R.id.CustomerEditProfileProfession);
        TaskerEditProfileUpdateButton = findViewById(R.id.CustomerEditProfileUpdateButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TaskerEditProfileRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(currentUser.getUid());
    }

    private void settingDataInEditText() {
        TaskerEditProfileRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (currentUser != null) {
                    TaskerEditProfileUsername.setText(dataSnapshot.child("taskerUsername").getValue().toString());
                    TaskerEditProfilePhone.setText(dataSnapshot.child("taskerPhonenumber").getValue().toString());
                    TaskerEditProfileEmail.setText(currentUser.getEmail());
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(TaskerEditProfileImage);
                    } else {
                        Toast.makeText(EditProfileTasker.this, "You Havn't Updated Your Profile Image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileTasker.this, "No Logged In User !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile() {
        String name = TaskerEditProfileUsername.getText().toString().trim();
        String phone = TaskerEditProfilePhone.getText().toString().trim();
        String email = TaskerEditProfileEmail.getText().toString().trim();
        String password = TaskerEditProfilePassword.getText().toString().trim();
        String gender = TaskerEditProfileGender.getSelectedItem().toString().trim();
        String profession = TaskerEditProfileProfession.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter Phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("Select Your Gender")) {
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
        } else if (profession.equals("Select Your Profession")) {
            Toast.makeText(this, "Select Profession", Toast.LENGTH_SHORT).show();
        } else {
            TaskerEditProfileRefrence.child("taskerUsername").setValue(name);
            TaskerEditProfileRefrence.child("taskerPhonenumber").setValue(phone);
            TaskerEditProfileRefrence.child("taskerGender").setValue(gender);
            TaskerEditProfileRefrence.child("taskerProfession").setValue(profession);
            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }
    }
}