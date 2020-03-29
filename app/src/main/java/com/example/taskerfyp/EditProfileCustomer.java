package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class EditProfileCustomer extends AppCompatActivity {
    CircleImageView CustomerEditProfileImage;
    EditText CustomerEditProfileUsername, CustomerEditProfilePhone, CustomerEditProfileEmail, CustomerEditProfilePassword;
    Spinner CustomerEditProfileGender;
    Button CustomerEditProfileUpdateButton;
    DatabaseReference CustomerEditProfileRefrence;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Profile");

        initilise();
        settingDataInEditText();

        CustomerEditProfileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }

    public void initilise() {
        CustomerEditProfileImage = findViewById(R.id.CustomerEditProfileImage);
        CustomerEditProfileUsername = findViewById(R.id.CustomerEditProfileUsername);
        CustomerEditProfilePhone = findViewById(R.id.CustomerEditProfilePhone);
        CustomerEditProfileEmail = findViewById(R.id.CustomerEditProfileEmail);
        CustomerEditProfilePassword = findViewById(R.id.CustomerEditProfilePassword);
        CustomerEditProfileGender = findViewById(R.id.CustomerEditProfileGender);
        CustomerEditProfileUpdateButton = findViewById(R.id.CustomerEditProfileUpdateButton);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CustomerEditProfileRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentUser.getUid());
    }

    private void settingDataInEditText() {
        CustomerEditProfileRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (currentUser != null) {
                    CustomerEditProfileUsername.setText(dataSnapshot.child("customerUsername").getValue().toString());
                    CustomerEditProfilePhone.setText(dataSnapshot.child("customerPhonenumber").getValue().toString());
                    CustomerEditProfileEmail.setText(currentUser.getEmail());
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(CustomerEditProfileImage);
                    } else {
                        Toast.makeText(EditProfileCustomer.this, "You Havn't Updated Your Profile Image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileCustomer.this, "No Logged In User !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile() {
        String name = CustomerEditProfileUsername.getText().toString().trim();
        String phone = CustomerEditProfilePhone.getText().toString().trim();
        String email = CustomerEditProfileEmail.getText().toString().trim();
        String password = CustomerEditProfilePassword.getText().toString().trim();
        String gender = CustomerEditProfileGender.getSelectedItem().toString().trim();

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
        } else {
            CustomerEditProfileRefrence.child("customerUsername").setValue(name);
            CustomerEditProfileRefrence.child("customerPhonenumber").setValue(phone);
            CustomerEditProfileRefrence.child("customerGender").setValue(gender);
            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }
    }
}