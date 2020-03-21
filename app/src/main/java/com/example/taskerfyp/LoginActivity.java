package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.MultiTapKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView tvCreateNewAccount;
    EditText edtLoginEmail, edtLoginPassword;
    RadioButton radiobtnCustomer, radiobtnTasker;
    RadioGroup radioGroup;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    DatabaseReference reference;
    int category_val;
    private FirebaseAuth.AuthStateListener firebaseAuthlistner;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Login Here");

        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        firebaseAuthlistner = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    category_val = prefs.getInt("Val", 0);
                    System.out.println("meri save id : " + category_val);
                    if (category_val == 1) {
                        Intent intent = new Intent(LoginActivity.this, CustomerWelocmeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    if (category_val == 2) {
                        Intent intent = new Intent(LoginActivity.this, TaskerWelocmeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    return;
                }
            }
        };

        initialize();

        tvCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AskActivity.class));
                finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowingUserToLogin();
            }
        });
    }

    private void initialize() {
        tvCreateNewAccount = findViewById(R.id.tvCreateNewAccount);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        radiobtnCustomer = findViewById(R.id.radiobtnCustomer);
        radiobtnTasker = findViewById(R.id.radiobtnTasker);
        loadingBar = new ProgressDialog(this);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        forgetPassword = findViewById(R.id.forgetPassword);
        mAuth = FirebaseAuth.getInstance();
    }


    @SuppressLint("ResourceType")
    private void allowingUserToLogin() {
        final String email = edtLoginEmail.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtLoginEmail.setError("Enter Email");
        } else if (TextUtils.isEmpty(password)) {
            edtLoginPassword.setError("Enter Password");
        } else if (radioGroup.getCheckedRadioButtonId() <= 0) {
            radiobtnTasker.setError("Select item please");
        } else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait while we are logging in you");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (radiobtnCustomer.isChecked()) {
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer");

                            reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                        Intent intent = new Intent(getApplicationContext(), CustomerWelocmeActivity.class);
                                        //intent.putExtra("customer_value", "C");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "This email doesnot exist in this category", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Tasker");
                            reference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        //Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                        Intent intent = new Intent(getApplicationContext(), TaskerWelocmeActivity.class);
                                        //intent.putExtra("tasker_value", "T");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "This email doesnot exist in this category", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthlistner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthlistner);
    }
}