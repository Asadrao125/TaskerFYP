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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditPostCustomer extends AppCompatActivity {
    EditText edtEditBudget, edtEditDescription, edtEditDeadline;
    Button btnUpdatePost;
    Spinner spinnerEditTitle;
    FirebaseUser currentUser;
    DatabaseReference refrence;
    String budget, description, deadline;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Post");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        id = getIntent().getStringExtra("id");

        edtEditBudget = findViewById(R.id.edtEditBudget);
        edtEditDeadline = findViewById(R.id.edtEditDeadline);
        edtEditDescription = findViewById(R.id.desc);
        spinnerEditTitle = findViewById(R.id.spinnerEditPost);
        btnUpdatePost = findViewById(R.id.btnUpdatePost);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        refrence = FirebaseDatabase.getInstance().getReference("All_Posts").child(currentUser.getUid()).child(id);

        // Getting Customer Post Data
        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("postId")) {
                    budget = dataSnapshot.child("budget").getValue().toString();
                    description = dataSnapshot.child("description").getValue().toString();
                    deadline = dataSnapshot.child("deadline").getValue().toString();

                    edtEditBudget.setText(budget);
                    edtEditDeadline.setText(deadline);
                    edtEditDescription.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Getting Customer Post data

        btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = edtEditDescription.getText().toString().trim();
                String budg = edtEditBudget.getText().toString().trim();
                String dead = edtEditDeadline.getText().toString().trim();
                String tit = spinnerEditTitle.getSelectedItem().toString().trim();
                if (TextUtils.isEmpty(desc)) {
                    Toast.makeText(EditPostCustomer.this, "Enter Description", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(budg)) {
                    Toast.makeText(EditPostCustomer.this, "Enter Budget", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dead)) {
                    Toast.makeText(EditPostCustomer.this, "Enter Deadline", Toast.LENGTH_SHORT).show();
                } else {
                    refrence.child("budget").setValue(budg);
                    refrence.child("description").setValue(desc);
                    refrence.child("deadline").setValue(dead);
                    if (!tit.equals("Select Post Title")) {
                        refrence.child("title").setValue(tit);
                    }
                    Toast.makeText(EditPostCustomer.this, "Post Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}