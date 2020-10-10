package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.Models.SendOfferTasker;
import com.example.taskerfyp.Models.TaskerUser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class SendOffer extends AppCompatActivity {
    EditText edtSendOfferDescription, edtSendOfferBudget, edtSendOfferDeadline;
    Button btnSendOfferTasker, btnTrackingTasker;
    String userName;
    String image;
    String offer_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_offer);

        final String post_krny_waly_ki_id = getIntent().getStringExtra("Post_krny_waly_ki_id");
        final String post_ki_id = getIntent().getStringExtra("post_ki_id");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Send Offer");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edtSendOfferBudget = findViewById(R.id.edtSendOfferBudget);
        edtSendOfferDeadline = findViewById(R.id.edtSendOfferDeadline);
        edtSendOfferDescription = findViewById(R.id.edtSendOfferDescription);
        btnSendOfferTasker = findViewById(R.id.btnSendOfferTasker);
        btnTrackingTasker = findViewById(R.id.btnTrackingTasker);

        DatabaseReference A_O_R = FirebaseDatabase.getInstance().getReference("Accepted_Offers");
        A_O_R.keepSynced(true);
        A_O_R.child(post_ki_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    btnSendOfferTasker.setText("Disabled!");
                    btnSendOfferTasker.setBackgroundColor(Color.LTGRAY);
                    btnSendOfferTasker.setEnabled(false);
                    Toast.makeText(SendOffer.this, "True", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser userC = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference onClickRef = FirebaseDatabase.getInstance().getReference("Offer_Sent").child(userC.getUid()).child(post_ki_id);
        onClickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    btnSendOfferTasker.setText("Offer Sent");
                    btnSendOfferTasker.setBackgroundColor(Color.LTGRAY);
                    btnSendOfferTasker.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSendOfferTasker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String offerDescription = edtSendOfferDescription.getText().toString().trim();
                final String offerBudget = edtSendOfferBudget.getText().toString().trim();
                final String offerDeadline = edtSendOfferDeadline.getText().toString().trim();
                if (TextUtils.isEmpty(offerDescription)) {
                    edtSendOfferDescription.setError("Enter Description");
                } else if (TextUtils.isEmpty(offerBudget)) {
                    edtSendOfferBudget.setError("Enter Budget");
                } else if (TextUtils.isEmpty(offerDeadline)) {
                    edtSendOfferDeadline.setError("Enter Deadline");
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reffff = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(user.getUid());
                    reffff.keepSynced(true);
                    reffff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("profileimage")) {
                                image = dataSnapshot.child("profileimage").getValue().toString();
                            } else {
                                Toast.makeText(SendOffer.this, "No Profile Image", Toast.LENGTH_SHORT).show();
                            }
                            userName = String.valueOf(dataSnapshot.child("taskerUsername").getValue());
                            DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("Offers");
                            offer_id = refrence.push().getKey();
                            SendOfferTasker sendOfferTasker = new SendOfferTasker(offerBudget, offerDeadline, offerDescription, offer_id, userName, user.getUid(), post_ki_id);
                            refrence.child(post_ki_id).push().setValue(sendOfferTasker);
                            refrence.keepSynced(true);
                            Toast.makeText(SendOffer.this, "Offer Sent!", Toast.LENGTH_SHORT).show();

                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Offer_Sent").child(user.getUid()).child(post_ki_id);
                            mRef.keepSynced(true);
                            mRef.setValue("Offer_Sent");

                            DatabaseReference dBRef = FirebaseDatabase.getInstance().getReference("Offers_Sent_By_Tasker");
                            SendOfferTasker sendOfferTasker2 = new SendOfferTasker(offerBudget, offerDeadline, offerDescription, offer_id, userName, user.getUid(), post_ki_id);
                            dBRef.child(user.getUid()).push().setValue(sendOfferTasker2);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SendOffer.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}