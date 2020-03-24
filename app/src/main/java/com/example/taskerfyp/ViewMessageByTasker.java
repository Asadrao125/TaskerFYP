package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMessageByTasker extends AppCompatActivity {
    TextView tvPostid, tvmessageid, message, name, email, gender, number, currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message_by_tasker);

        tvPostid = findViewById(R.id.tvPostId);
        tvmessageid = findViewById(R.id.tvMessageId);
        message = findViewById(R.id.tvMessage);
        name = findViewById(R.id.tvName);
        email = findViewById(R.id.tvEmail);
        gender = findViewById(R.id.tvGender);
        number = findViewById(R.id.tvNumber);
        currentuserid = findViewById(R.id.tvCurrentUserId);

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messages").child(fUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(ViewMessageByTasker.this, "" + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                String c_uid = dataSnapshot.child("current_user_id").getValue().toString();
                String c_email = dataSnapshot.child("customer_email").getValue().toString();
                String c_gender = dataSnapshot.child("customer_gender").getValue().toString();
                String c_name = dataSnapshot.child("customer_name").getValue().toString();
                String c_number = dataSnapshot.child("customer_number").getValue().toString();
                String msg = dataSnapshot.child("message").getValue().toString();
                String m_id = dataSnapshot.child("message_id").getValue().toString();
                String post_id_id = dataSnapshot.child("post_id").getValue().toString();

                currentuserid.setText("Post krny wly ki ID \n" + c_uid);
                number.setText("Number \n" + c_number);
                gender.setText("Gender \n" + c_gender);
                email.setText("Email \n" + c_email);
                name.setText("Name \n" + c_name);
                message.setText(msg + " by following detail \n");
                tvPostid.setText("Post Ki ID \n" + post_id_id);
                tvmessageid.setText("Message Ki ID \n" + m_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
