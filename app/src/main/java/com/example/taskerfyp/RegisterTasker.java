package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
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

import com.example.taskerfyp.Models.ChatUserModel;
import com.example.taskerfyp.Models.TaskerUser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterTasker extends AppCompatActivity {
    private EditText edtTaskerUsername;
    private EditText edtTaskerPhonenumber;
    private EditText edtTaskerEmail;
    private EditText edtTaskerPassword;
    private Spinner spinnerTaskerGender;
    private Spinner spinnerTaskerProfession;
    private FirebaseAuth mAuth;
    private Button btnCreateTasker;
    DatabaseReference mRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tasker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Register Tasker");
        mTitle.setTextSize(26);

        initialize();

        btnCreateTasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTasker();
            }
        });

    }

    private void initialize() {
        edtTaskerUsername = findViewById(R.id.edtTaskerUsername);
        edtTaskerPhonenumber = findViewById(R.id.edtTaskerPhonenumber);
        edtTaskerEmail = findViewById(R.id.edtTaskerEmail);
        edtTaskerPassword = findViewById(R.id.edtTaskerPassword);
        spinnerTaskerGender = findViewById(R.id.spinnerTaskerGender);
        spinnerTaskerProfession = findViewById(R.id.spinnerTaskerProfession);
        mAuth = FirebaseAuth.getInstance();
        btnCreateTasker = findViewById(R.id.btnCreateTasker);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void createTasker() {
        final String taskerUsername = edtTaskerUsername.getText().toString().trim();
        final String taskerPhonenumber = edtTaskerPhonenumber.getText().toString().trim();
        final String taskerGender = spinnerTaskerGender.getSelectedItem().toString().trim();
        final String taskerProfession = spinnerTaskerProfession.getSelectedItem().toString().trim();
        final String taskerEmail = edtTaskerEmail.getText().toString().trim();
        final String taskerPassword = edtTaskerPassword.getText().toString().trim();

        if (TextUtils.isEmpty(taskerUsername)) {
            Toast.makeText(this, "Enter Username!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(taskerPhonenumber)) {
            Toast.makeText(this, "Enter Phonenumber!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(taskerEmail)) {
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(taskerPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        } else if (!taskerGender.equals("Male") && !taskerGender.equals("Female") && !taskerGender.equals("Other")) {
            Toast.makeText(this, "Select Gender Please!", Toast.LENGTH_SHORT).show();
        } else if (taskerProfession.equals("Select Your Profession")) {
            Toast.makeText(this, "Select Profession Please!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setMessage("Creating Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(taskerEmail, taskerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Tasker");
                        TaskerUser taskerUser = new TaskerUser(taskerUsername, taskerPhonenumber, taskerGender, taskerProfession, taskerEmail, mAuth.getCurrentUser().getUid());
                        String current_user = mAuth.getCurrentUser().getUid();
                        mRef.child(current_user).setValue(taskerUser);

                        //Saving this data for chat purpose
                        ChatUserModel chatUserModel = new ChatUserModel(taskerUsername, current_user, "tasker", taskerGender, taskerPhonenumber, taskerEmail,"");
                        DatabaseReference chatUserRefrence = FirebaseDatabase.getInstance().getReference("All_Users")
                                .child(current_user);
                        chatUserRefrence.setValue(chatUserModel);
                        //

                        Toast.makeText(RegisterTasker.this, "Account Created !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), TaskerWelocmeActivity.class));
                        finish();
                        loadingBar.dismiss();
                        edtTaskerUsername.setText("");
                        edtTaskerEmail.setText("");
                        edtTaskerPhonenumber.setText("");
                        edtTaskerPassword.setText("");

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterTasker.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}