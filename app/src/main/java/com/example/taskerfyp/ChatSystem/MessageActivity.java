package com.example.taskerfyp.ChatSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskerfyp.R;
import com.example.taskerfyp.ViewOfferTasker;
import com.example.taskerfyp.ViewProfileByCustomer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image_chat;
    TextView username_chat;
    DatabaseReference mRef;
    ImageButton btnSend;
    EditText txt_send;
    ImageView imgBack;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final String reciever_id = getIntent().getStringExtra("reciever_id");

        btnSend = findViewById(R.id.btnSend);
        txt_send = findViewById(R.id.txtSend);
        imgBack = findViewById(R.id.img_back);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting Current Date and Time
                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
                String date = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
                String time = currentTime.format(calFordTime.getTime());
                // Getting Current Date and Time

                String sender_id = getIntent().getStringExtra("sender_id");
                String reciever_id = getIntent().getStringExtra("reciever_id");
                String message = txt_send.getText().toString().trim();

                if (!message.equals("")) {
                    sendMessage(sender_id, reciever_id, time, date, message);
                    //Toast.makeText(MessageActivity.this, "Msg Sent!", Toast.LENGTH_SHORT).show();
                    txt_send.setText("");
                }
            }
        });

        final String sender_id = getIntent().getStringExtra("sender_id");

        // Getting profile image of reciever
       /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Tasker").child(reciever_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //readMessages(sender_id, reciever_id, "https://firebasestorage.googleapis.com/v0/b/taskerfyp.appspot.com/o/Profile%20Images%2Fcom.google.firebase.auth.internal.zzn%40abe2bde.jpg?alt=media&token=fe7794c7-7e4a-4522-bc9a-766f014f8a2f");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Inbox.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        profile_image_chat = findViewById(R.id.profile_image_chat);
        username_chat = findViewById(R.id.username_chat);
        mRef = FirebaseDatabase.getInstance().getReference("All_Users").child(sender_id);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                String name = getIntent().getStringExtra("name");
                username_chat.setText(name);
                readMessages(sender_id, reciever_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile_image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewProfileByCustomer.class);
                intent.putExtra("tasker_ki_profile_ki_id", reciever_id);
                startActivity(intent);
            }
        });

        username_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewProfileByCustomer.class);
                intent.putExtra("tasker_ki_profile_ki_id", reciever_id);
                startActivity(intent);
            }
        });

    }

    private void sendMessage(String sender, String reciever, String time, String date, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All_Chats");
        Chat sendMessage = new Chat(sender, reciever, time, date, message);
        databaseReference.push().setValue(sendMessage);
    }

    private void readMessages(final String myid, final String userid) {
        mChat = new ArrayList<>();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference("All_Chats");
        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReciever().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}