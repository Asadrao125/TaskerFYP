package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskerfyp.Models.AddPostCustomer;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPost extends AppCompatActivity {
    EditText edtTaskDescription, edtTaskBudget, edtTaskDeadline;
    Spinner spinnerProfessionCustomer;
    Button btnAddPostCustomer;
    TextInputLayout TILDescription, TILBudget, TILDeadline;
    DatabaseReference ref;
    FirebaseAuth firebaseAuth;
    String current_user_id;
    FirebaseUser currentFirebaseUser;
    String CurrentUser_Name;
    Object All_Post_Counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initilize();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentFirebaseUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrentUser_Name = (String) dataSnapshot.child("customerUsername").getValue();
                Toast.makeText(AddPost.this, "" + CurrentUser_Name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        current_user_id = firebaseUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("All_Posts");

        btnAddPostCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPost();
            }
        });
    }

    public void initilize() {
        edtTaskDescription = findViewById(R.id.edtTaskDescription);
        edtTaskBudget = findViewById(R.id.edtTaskBudget);
        edtTaskDeadline = findViewById(R.id.edtTaskeDeadline);
        btnAddPostCustomer = findViewById(R.id.btnAddPostCustomer);
        spinnerProfessionCustomer = findViewById(R.id.spinnerCustomerProfession);
        TILDescription = findViewById(R.id.TILDescription);
        TILBudget = findViewById(R.id.TILBudget);
        TILDeadline = findViewById(R.id.TILDeadline);
    }

    private void UploadPost() {

        //// All Post Countrer ////

        /************ Retrieving All Post Counter ***********/
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("All_Posts").child("All_Post_Counter");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(AddPost.this, "" + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                All_Post_Counter = dataSnapshot.getValue();
                Toast.makeText(AddPost.this, "" + All_Post_Counter, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /******* Getting Current Date and Time *******/
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        String date = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String time = currentTime.format(calFordTime.getTime());
        /******* Getting Current Date and Time *******/

        /************ Retrieving All Post Counter ***********/

        //// All Post Counter ////

        String titleCustomer = spinnerProfessionCustomer.getSelectedItem().toString().trim();
        String description = edtTaskDescription.getText().toString().trim();
        String budget = edtTaskBudget.getText().toString().trim();
        String deadline = edtTaskDeadline.getText().toString().trim();

        if (titleCustomer.equals("Select Your Profession")) {
            Toast.makeText(this, "Select Title", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            TILDescription.setError("Enter Description");
        } else if (TextUtils.isEmpty(budget)) {
            TILBudget.setError("Enter Budget");
        } else if (TextUtils.isEmpty(deadline)) {
            TILDeadline.setError("Enter Deadline");
        } else {
            AddPostCustomer addPostCustomer = new AddPostCustomer(current_user_id, titleCustomer, description, budget, deadline, time, date, CurrentUser_Name);
            ref.push().child(current_user_id).setValue(addPostCustomer);
            Toast.makeText(this, "Post Uploaded To Firebase !", Toast.LENGTH_SHORT).show();
        }

        /******* Updating All Post Counter *******/

        dbRef.setValue(All_Post_Counter);

        /******* Updating All Post Counter *******/

    }
}