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

public class EditProfileTasker extends AppCompatActivity {
    CircleImageView TaskerEditProfileImage;
    EditText TaskerEditProfileUsername, TaskerEditProfilePhone, TaskerEditProfileEmail;
    //EditText TaskerEditProfilePassword;
    Spinner TaskerEditProfileGender, TaskerEditProfileProfession;
    Button TaskerEditProfileUpdateButton;
    DatabaseReference TaskerEditProfileRefrence;
    FirebaseUser currentUser;
    FirebaseUser currentFirebaseUser;
    DatabaseReference mRef;
    int Image_Request_Code = 7;
    String downloadUrl;
    StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("image");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_tasker);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(currentFirebaseUser.getUid());

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
        TaskerEditProfileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        TaskerEditProfileImage.setOnClickListener(new View.OnClickListener() {
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
        TaskerEditProfileImage = findViewById(R.id.CustomerEditProfileImage);
        TaskerEditProfileUsername = findViewById(R.id.CustomerEditProfileUsername);
        TaskerEditProfilePhone = findViewById(R.id.CustomerEditProfilePhone);
        TaskerEditProfileEmail = findViewById(R.id.CustomerEditProfileEmail);
        //TaskerEditProfilePassword = findViewById(R.id.CustomerEditProfilePassword);
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
                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(TaskerEditProfileImage);
                    }
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
        //String password = TaskerEditProfilePassword.getText().toString().trim();
        String gender = TaskerEditProfileGender.getSelectedItem().toString().trim();
        String profession = TaskerEditProfileProfession.getSelectedItem().toString().trim();

        if (!gender.equals("Select Your Gender")) {
            TaskerEditProfileRefrence.child("taskerGender").setValue(gender);
        }
        if (!profession.equals("Select Your Profession")) {
            TaskerEditProfileRefrence.child("taskerProfession").setValue(profession);
        }

        TaskerEditProfileRefrence.child("taskerUsername").setValue(name);
        TaskerEditProfileRefrence.child("taskerPhonenumber").setValue(phone);
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

                final StorageReference filePath = UserProfileImageRef.child(".jpg");

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
                            downloadUrl = downUri.toString();
                            mRef.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
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