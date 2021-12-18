package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.ChatSystem.Inbox;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.BuildConfig;
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

public class TaskerWelocmeActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    Button btnViewPost, btnDeleteAccount, btnNotification, btnInviteFriends, btnHelp, btnLogoutTasker, btnViewprofile;
    CircleImageView imgProfile;
    Button btnInboxTasker;
    TextView tv_job_title;
    FirebaseUser currentFirebaseUser;
    DatabaseReference mRef;
    DatabaseReference mRef2;
    DatabaseReference UsersRef;
    int Image_Request_Code = 7;
    String downloadUrl;
    StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("image");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasker_welocme);

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("Val", 2);//2 for tasker
        editor.apply();

        final TextView mTitle = findViewById(R.id.toolbar_title);
        imgProfile = findViewById(R.id.imgProfile);
        btnViewPost = findViewById(R.id.btnViewPost);
        btnNotification = findViewById(R.id.btnNotification);
        btnInviteFriends = findViewById(R.id.btnInviteFriends);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnHelp = findViewById(R.id.btnHelp);
        btnLogoutTasker = findViewById(R.id.btnLogoutTasker);
        btnViewprofile = findViewById(R.id.btnViewProfile);
        tv_job_title = findViewById(R.id.tv_job_title);
        btnInboxTasker = findViewById(R.id.btnInboxTasker);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Image_Request_Code);
            }
        });


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(currentFirebaseUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mTitle.setText(dataSnapshot.child("taskerUsername").getValue().toString());
                    tv_job_title.setText(dataSnapshot.child("taskerProfession").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Tasker").child(currentFirebaseUser.getUid());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("image")) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.mipmap.ic_profile).into(imgProfile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnInboxTasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Inbox.class));
            }
        });

        btnViewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewProfile_of_Tasker.class));
            }
        });

        btnViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tasker_View_Post.class));
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewMessageByTasker.class));
            }
        });

        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIntent();
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeleteAccountTasker.class));
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View helpView = inflater.inflate(R.layout.help_alert_dialog, null);
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TaskerWelocmeActivity.this)
                        .setView(helpView)
                        .setCancelable(false)
                        .create();
                Button btnOk = helpView.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        btnLogoutTasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

                SharedPreferences spreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor spreferencesEditor = spreferences.edit();
                spreferencesEditor.remove("tasker"); //we are removing tasker value by key
                spreferencesEditor.commit();

                Toast.makeText(TaskerWelocmeActivity.this, "Logout Successfully !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shareIntent() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tasker");
            String shareMessage = "Let me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.toString();
        }
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
                            DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference("All_Users");
                            mRef2.child(currentFirebaseUser.getUid()).child("profileimage").setValue(downloadUrl);
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