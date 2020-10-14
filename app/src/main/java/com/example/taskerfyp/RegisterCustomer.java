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

import com.example.taskerfyp.Models.ChatUserModel;
import com.example.taskerfyp.Models.CustomerUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterCustomer extends AppCompatActivity {
    private EditText edtCustomerUsername;
    private EditText edtCustomerPhonenumber;
    private EditText edtCustomerEmail;
    private EditText edtCustomerPassword;
    private Spinner spinnerCustomerGender;
    private Button btnCreateCustomer;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Register Customer");
        mTitle.setTextSize(24);

        initialize();

        btnCreateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCustomer();
            }
        });
    }

    private void initialize() {
        edtCustomerUsername = findViewById(R.id.edtCustomerUsername);
        edtCustomerPhonenumber = findViewById(R.id.edtCustomerPhonenumber);
        edtCustomerEmail = findViewById(R.id.edtCustomerEmail);
        edtCustomerPassword = findViewById(R.id.edtCustomerPassword);
        spinnerCustomerGender = findViewById(R.id.spinnerCustomerGender);
        btnCreateCustomer = findViewById(R.id.btnCreateCustomer);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void createCustomer() {
        final String customerUsername = edtCustomerUsername.getText().toString().trim();
        final String customerPhonenumber = edtCustomerPhonenumber.getText().toString().trim();
        final String customerEmail = edtCustomerEmail.getText().toString().trim();
        String customerPassword = edtCustomerPassword.getText().toString().trim();
        final String customerGender = spinnerCustomerGender.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(customerUsername)) {
            Toast.makeText(this, "Enter Username!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(customerPhonenumber)) {
            Toast.makeText(this, "Enter Phonenumber!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(customerEmail)) {
            Toast.makeText(this, "Enter Email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(customerPassword)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        } else if (!customerGender.equals("Male") && !customerGender.equals("Female") && !customerGender.equals("Other")) {
            Toast.makeText(this, "Select Gender Please!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Authenticating");
            loadingBar.setMessage("Please Wait While We Are Authenticating You.");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(customerEmail, customerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String current_user_id;
                        final DatabaseReference customerRef;
                        current_user_id = mAuth.getCurrentUser().getUid();
                        customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customer").child(current_user_id);

                        loadingBar.dismiss();
                        loadingBar.setMessage("Creating Your Account");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(false);

                        HashMap customerrMap = new HashMap();
                        customerrMap.put("customerUsername", customerUsername);
                        customerrMap.put("customerPhonenumber", customerPhonenumber);
                        customerrMap.put("customerGender", customerGender);
                        customerrMap.put("email", customerEmail);

                        CustomerUser user = new CustomerUser();
                        user.setCustomerEmail(edtCustomerEmail.getText().toString());
                        user.setCustomerUsername(edtCustomerUsername.getText().toString());
                        user.setCustomerGender(spinnerCustomerGender.getSelectedItem().toString());
                        user.setCustomerPhonenumber(edtCustomerPhonenumber.getText().toString());

                        //Saving this data for chat purpose
                        ChatUserModel chatUserModel = new ChatUserModel(customerUsername, FirebaseAuth.getInstance().getUid(),
                                "customer", customerGender, customerPhonenumber, customerEmail);
                        DatabaseReference chatUserRefrence = FirebaseDatabase.getInstance().getReference("All_Users")
                                .child(FirebaseAuth.getInstance().getUid());
                        chatUserRefrence.setValue(chatUserModel);
                        //

                        customerRef.updateChildren(customerrMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterCustomer.this, "Account Created Succesfully.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    startActivity(new Intent(getApplicationContext(), CustomerWelocmeActivity.class));
                                    finish();
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(RegisterCustomer.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterCustomer.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}