package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileCustomer extends AppCompatActivity {
    CircleImageView CustomerEditProfileImage;
    EditText CustomerEditProfileUsername, CustomerEditProfilePhone, CustomerEditProfileEmail;
    Spinner CustomerEditProfileGender;
    Button CustomerEditProfileUpdateButton;
    DatabaseReference CustomerEditProfileRefrence;
    int Image_Request_Code = 7;
    FirebaseUser currentFirebaseUser;
    DatabaseReference mRef;
    StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initilise();
        settingDataInEditText();

        CustomerEditProfileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentFirebaseUser.getUid());

        CustomerEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Image_Request_Code);
            }
        });
    }

    public void initilise() {
        CustomerEditProfileImage = findViewById(R.id.CustomerEditProfileImage);
        CustomerEditProfileUsername = findViewById(R.id.CustomerEditProfileUsername);
        CustomerEditProfilePhone = findViewById(R.id.CustomerEditProfilePhone);
        CustomerEditProfileEmail = findViewById(R.id.CustomerEditProfileEmail);
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
                    }
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
        String gender = CustomerEditProfileGender.getSelectedItem().toString().trim();

        if (gender.equals("Male")) {
            CustomerEditProfileRefrence.child("customerGender").setValue(gender);
        }
        if (gender.equals("Female")) {
            CustomerEditProfileRefrence.child("customerGender").setValue(gender);
        }

        CustomerEditProfileRefrence.child("customerUsername").setValue(name);
        CustomerEditProfileRefrence.child("customerPhonenumber").setValue(phone);
        Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentFirebaseUser + ".jpg");

                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            final String downloadUrl = downUri.toString();
                            mRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Profile Image Updated!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}