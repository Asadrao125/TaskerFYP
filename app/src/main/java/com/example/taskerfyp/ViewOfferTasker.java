package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.Adapter.ViewOfferAdapter;
import com.example.taskerfyp.Models.SendOfferTasker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOfferTasker extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<SendOfferTasker> list;
    ViewOfferAdapter adapter;
    String ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_tasker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("View Offers");

        recyclerView = findViewById(R.id.recycler_view_offer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<SendOfferTasker>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot shot : dataSnapshot1.getChildren()) {
                            SendOfferTasker sendOfferTasker = shot.getValue(SendOfferTasker.class);
                            list.add(sendOfferTasker);
                        }
                        adapter = new ViewOfferAdapter(ViewOfferTasker.this, list);
                        recyclerView.setAdapter(adapter);
                    }
                } else
                    Toast.makeText(ViewOfferTasker.this, "No Offers To Show", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}