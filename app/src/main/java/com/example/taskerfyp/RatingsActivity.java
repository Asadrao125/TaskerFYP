package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.Adapter.MyAdapter;
import com.example.taskerfyp.Adapter.RatingsAdapter;
import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.RatingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<RatingModel> list;
    RatingsAdapter ratingsAdapter;
    String tasker_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Ratings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tasker_id = getIntent().getStringExtra("tasker_id");

        recyclerView = findViewById(R.id.recycler_view_ratings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Ratings").child(tasker_id);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        RatingModel ratingModel = dataSnapshot1.getValue(RatingModel.class);
                        list.add(ratingModel);
                        Log.d("data_kya_hai", "onDataChange: " + dataSnapshot1.getValue());
                    }
                    ratingsAdapter = new RatingsAdapter(RatingsActivity.this, list);
                    recyclerView.setAdapter(ratingsAdapter);
                } else {
                    Toast.makeText(RatingsActivity.this, "No Rating To Show", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}