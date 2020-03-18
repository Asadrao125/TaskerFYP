package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterTasker extends AppCompatActivity {
    private EditText edtTaskerUsername;
    private EditText edtTaskerPhonenumber;
    private EditText edtTaskerEmail;
    private EditText edtTaskerPassword;
    private Spinner spinnerTaskerGender;
    private Spinner spinnerTaskerProfession;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private Button btnCreateTasker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_tasker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Tasker");
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
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        btnCreateTasker = findViewById(R.id.btnCreateTasker);
    }

    private void createTasker() {
        final String taskerUsername = edtTaskerUsername.getText().toString().trim();
        final String taskerPhonenumber = edtTaskerPhonenumber.getText().toString().trim();
        final String taskerEmail = edtTaskerEmail.getText().toString().trim();
        final String taskerPassword = edtTaskerPassword.getText().toString().trim();
        final String taskerGender = spinnerTaskerGender.getSelectedItem().toString().trim();
        final String taskerProfession = spinnerTaskerProfession.getSelectedItem().toString().trim();

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
            loadingBar.setTitle("Authenticating");
            loadingBar.setMessage("Please Wait While We Are Authenticating You.");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(taskerEmail, taskerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String current_user_id;
                        DatabaseReference taskerRef;
                        current_user_id = mAuth.getCurrentUser().getUid();
                        taskerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Tasker").child(current_user_id);

                        loadingBar.dismiss();
                        loadingBar.setTitle("Creating Account");
                        loadingBar.setMessage("Please Wait While We Are Creating Your Account");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(false);

                        HashMap taskerMap = new HashMap();
                        taskerMap.put("taskerUsername", taskerUsername);
                        taskerMap.put("taskerPhonenumber", taskerPhonenumber);
                        taskerMap.put("taskerGender", taskerGender);
                        taskerMap.put("taskerProfession", taskerProfession);
                        taskerMap.put("email", taskerEmail);

                        taskerRef.updateChildren(taskerMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterTasker.this, "Account Created Succesfully.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    startActivity(new Intent(getApplicationContext(), TaskerWelocmeActivity.class));
                                    finish();
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(RegisterTasker.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterTasker.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}