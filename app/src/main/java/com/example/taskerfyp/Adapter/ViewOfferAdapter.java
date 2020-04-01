package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.SendMessage;
import com.example.taskerfyp.Models.SendOfferTasker;
import com.example.taskerfyp.R;
import com.example.taskerfyp.SendOffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOfferAdapter extends RecyclerView.Adapter<ViewOfferAdapter.MyViewHolder> {

    Context context;
    ArrayList<SendOfferTasker> sendOfferTaskers;
    private String name, email, number, gender;

    public ViewOfferAdapter(Context c, ArrayList<SendOfferTasker> s) {
        context = c;
        sendOfferTaskers = s;
    }

    @NonNull
    @Override
    public ViewOfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_offers, parent, false);
        return new ViewOfferAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewOfferAdapter.MyViewHolder holder, final int position) {
        holder.username.setText("Name: " + sendOfferTaskers.get(position).getUserName());
        holder.budget.setText("Budget: " + sendOfferTaskers.get(position).getOffer_budget());
        holder.deadline.setText("Deadline: " + sendOfferTaskers.get(position).getOffer_deadline());
        holder.description.setText("Description: \n" + sendOfferTaskers.get(position).getOffer_description());

        DatabaseReference onClickRef = FirebaseDatabase.getInstance().getReference("Messages").child(sendOfferTaskers.get(position).getOffer_sender_id());
        onClickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("onClick")) {
                    if (dataSnapshot.child("onClick").getValue().equals("1")) {
                        holder.btnAcceptOffer.setText("Offer Accpted");
                        holder.btnAcceptOffer.setBackgroundColor(Color.LTGRAY);
                        holder.btnAcceptOffer.setEnabled(false);

                        holder.btnDeclineOffer.setBackgroundColor(Color.LTGRAY);
                        holder.btnDeclineOffer.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference offerdecliedref = FirebaseDatabase.getInstance().getReference("Messages").child(sendOfferTaskers.get(position).getOffer_sender_id());
        offerdecliedref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("onClick")) {
                    if (dataSnapshot.child("onClick").getValue().equals("0")) {
                        holder.btnDeclineOffer.setText("Offer Declined");
                        holder.btnDeclineOffer.setBackgroundColor(Color.LTGRAY);
                        holder.btnDeclineOffer.setEnabled(false);

                        holder.btnAcceptOffer.setBackgroundColor(Color.LTGRAY);
                        holder.btnAcceptOffer.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.btnAcceptOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Getting Curent User Name Who Will Accept That Post
                DatabaseReference currentName = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(user.getUid());
                currentName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name = dataSnapshot.child("customerUsername").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();
                        number = dataSnapshot.child("customerPhonenumber").getValue().toString();
                        gender = dataSnapshot.child("customerGender").getValue().toString();
                        String current_user_id = user.getUid();
                        String post_id = sendOfferTaskers.get(position).getPost_id();
                        // Sending message to the tasker, that has offered for the post
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages").child(sendOfferTaskers.get(position).getOffer_sender_id());
                        String message_id = reference.push().getKey();
                        String message = "Your Offer Has Been Accepted";
                        SendMessage sendMessage = new SendMessage(post_id, message_id, message, current_user_id, name, email, number, gender);
                        reference.setValue(sendMessage);
                        reference.child("onClick").setValue("1");
                        Toast.makeText(context, "Offer accepted messege sent !", Toast.LENGTH_LONG).show();
                        /////////
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.btnDeclineOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Getting Curent User Name Who Will Accept That Post
                DatabaseReference currentName = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(user.getUid());
                currentName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name = dataSnapshot.child("customerUsername").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();
                        number = dataSnapshot.child("customerPhonenumber").getValue().toString();
                        gender = dataSnapshot.child("customerGender").getValue().toString();
                        String current_user_id = user.getUid();
                        String post_id = sendOfferTaskers.get(position).getPost_id();
                        // Sending message to the tasker, that has offered for the post
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages").child(sendOfferTaskers.get(position).getOffer_sender_id());
                        String message_id = reference.push().getKey();
                        String message = "Your Offer Has Been Declined";
                        SendMessage sendMessage = new SendMessage(post_id, message_id, message, current_user_id, name, email, number, gender);
                        reference.setValue(sendMessage);
                        reference.child("onClick").setValue("0");
                        Toast.makeText(context, "Offer declined messege sent !", Toast.LENGTH_LONG).show();
                        /////////
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return sendOfferTaskers.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username, budget, deadline, description;
        private Button btnAcceptOffer, btnDeclineOffer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvViewOfferName);
            budget = itemView.findViewById(R.id.tvViewOfferBudget);
            deadline = itemView.findViewById(R.id.tvViewOfferDeadine);
            description = itemView.findViewById(R.id.tvViewOfferDescription);
            btnAcceptOffer = itemView.findViewById(R.id.btnAcceptOffer);
            btnDeclineOffer = itemView.findViewById(R.id.btnDeclineOffer);
        }
    }
}