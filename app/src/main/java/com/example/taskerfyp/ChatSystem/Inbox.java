package com.example.taskerfyp.ChatSystem;

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
import com.example.taskerfyp.Adapter.UserAdapter;
import com.example.taskerfyp.Models.ChatUserModel;
import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.TaskerUser;
import com.example.taskerfyp.R;
import com.example.taskerfyp.ViewPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Inbox extends AppCompatActivity {
    RecyclerView recycler_All_Chats;
    ArrayList<ChatUserModel> list;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Inbox");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recycler_All_Chats = findViewById(R.id.recycler_All_Chats);
        recycler_All_Chats.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("All_Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot shot : dataSnapshot.getChildren()) {
                        if (!FirebaseAuth.getInstance().getUid().equals(shot.getKey())) {
                            ChatUserModel chatUserModel = shot.getValue(ChatUserModel.class);
                            list.add(chatUserModel);
                        }
                    }
                    userAdapter = new UserAdapter(Inbox.this, list);
                    recycler_All_Chats.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}