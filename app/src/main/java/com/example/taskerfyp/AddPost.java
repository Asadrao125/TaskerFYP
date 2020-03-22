package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.Models.AddPostCustomer;
import com.example.taskerfyp.Models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddPost extends AppCompatActivity {
    EditText edtTaskDescription, edtTaskBudget, edtTaskDeadline;
    Spinner spinnerProfessionCustomer;
    Button btnAddPostCustomer;
    TextInputLayout TILDescription, TILBudget, TILDeadline;
    DatabaseReference ref;
    String current_user_id;
    FirebaseUser currentFirebaseUser;
    String CurrentUser_Name;
    long counter = 0;
    long currentUserIDcounter = 0;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Post");

        initilize();

        /* Getting All Post Counter Value*/
        DatabaseReference databaseReferenceCounter = FirebaseDatabase.getInstance().getReference("Counter");
        databaseReferenceCounter.child("Count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue(String.class);
                counter = Long.parseLong(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /* Getting All Post Counter Value*/

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentFirebaseUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrentUser_Name = (String) dataSnapshot.child("customerUsername").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        current_user_id = firebaseUser.getUid();

        ref = FirebaseDatabase.getInstance().getReference("All_Posts").child(current_user_id);

        btnAddPostCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPost();
            }
        });

        // Getting Profile Image Child From Current User To Use In View Post Activity
        DatabaseReference imageRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(currentFirebaseUser.getUid());
        imageRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(AddPost.this, ""+dataSnapshot.child("profileimage").getValue(), Toast.LENGTH_SHORT).show();
                imageUrl = String.valueOf(dataSnapshot.child("profileimage").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /* Getting No of Post On Current User ID  */
        DatabaseReference current = FirebaseDatabase.getInstance().getReference("Current_UID_Post_Counter").child(current_user_id);
        current.child("Count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue(String.class);
                if (val != null) {
                    currentUserIDcounter = Long.parseLong(val);
                    //Toast.makeText(AddPost.this, "Counter: "+currentUserIDcounter, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(AddPost.this, "No Post Yet!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /* Getting No of Post On Current User ID  */
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

        // Getting Current GPS Location By Applying Latitude and Longitude To Geocoder ******/
        /*Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(122, 132, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /******* Getting Current GPS Location ********/

        // Getting Current Date and Time
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        String date = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        String time = currentTime.format(calFordTime.getTime());
        // Getting Current Date and Time

        String titleCustomer = spinnerProfessionCustomer.getSelectedItem().toString().trim();
        String description = edtTaskDescription.getText().toString().trim();
        String budget = edtTaskBudget.getText().toString().trim();
        String deadline = edtTaskDeadline.getText().toString().trim();

        if (titleCustomer.equals("Select Post Title")) {
            Toast.makeText(this, "Select Title", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            TILDescription.setError("Enter Description");
        } else if (TextUtils.isEmpty(budget)) {
            TILBudget.setError("Enter Budget");
        } else if (TextUtils.isEmpty(deadline)) {
            TILDeadline.setError("Enter Deadline");
        } else {

            counter++;
            String postId = ref.push().getKey();
            Post post = new Post(current_user_id, titleCustomer, description, budget, deadline, time, date, CurrentUser_Name, imageUrl, postId);
            ref.child(postId).setValue(post);
            Toast.makeText(this, "Post Uploaded To Firebase !", Toast.LENGTH_SHORT).show();

            // Updating Counter

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Counter");
            HashMap map = new HashMap();
            map.put("Count", String.valueOf(counter));
            ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                }
            });

            // Updating Counter
            currentUserIDcounter++;
            // Updating Counter for No of Posts, that a current user updated *//*
            DatabaseReference seperateCurrentUIDcounter = FirebaseDatabase.getInstance().getReference("Current_UID_Post_Counter").child(current_user_id);
            HashMap C_UIDCounterMap = new HashMap();
            C_UIDCounterMap.put("Count", String.valueOf(currentUserIDcounter));
            seperateCurrentUIDcounter.updateChildren(C_UIDCounterMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                }
            });
            // Updating Counter for No of Posts, that a current user updated

        }
    }
}