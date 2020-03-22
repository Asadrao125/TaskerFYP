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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPostCustomer extends AppCompatActivity {
    EditText edtEditBudget, edtEditDescription, edtEditDeadline;
    Button btnUpdatePost;
    Spinner spinnerEditTitle;
    FirebaseUser currentUser;
    DatabaseReference refrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Edit Post");

        String id = getIntent().getStringExtra("id");
        //Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();

        edtEditBudget = findViewById(R.id.edtEditBudget);
        edtEditDeadline = findViewById(R.id.edtEditDeadline);
        edtEditDescription = findViewById(R.id.desc);
        spinnerEditTitle = findViewById(R.id.spinnerEditPost);
        btnUpdatePost = findViewById(R.id.btnUpdatePost);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        refrence = FirebaseDatabase.getInstance().getReference("All_Posts").child(currentUser.getUid()).child(id);

        btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = edtEditBudget.getText().toString().trim();
                String deadline = edtEditDeadline.getText().toString().trim();
                String description = edtEditDescription.getText().toString().trim();
                String title = spinnerEditTitle.getSelectedItem().toString().trim();
                if (!TextUtils.isEmpty(budget)) {
                    refrence.child("budget").setValue(budget);
                    Toast.makeText(EditPostCustomer.this, "Budget Updated!", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(deadline)) {
                    refrence.child("deadline").setValue(deadline);
                    Toast.makeText(EditPostCustomer.this, "Deadline Updated!", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(description)) {
                    refrence.child("description").setValue(description);
                    Toast.makeText(EditPostCustomer.this, "Description Updated!", Toast.LENGTH_SHORT).show();
                } else if (!title.equals("Select Post Title")) {
                    refrence.child("title").setValue(title);
                    Toast.makeText(EditPostCustomer.this, "Title Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}