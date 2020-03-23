package com.example.taskerfyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.SendOfferTasker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewOfferAdapter extends RecyclerView.Adapter<ViewOfferAdapter.MyViewHolder> {

    Context context;
    ArrayList<SendOfferTasker> sendOfferTaskers;

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
    public void onBindViewHolder(@NonNull ViewOfferAdapter.MyViewHolder holder, int position) {
        holder.username.setText("Name: " + sendOfferTaskers.get(position).getUserName());
        holder.budget.setText("Budget: " + sendOfferTaskers.get(position).getOffer_budget());
        holder.deadline.setText("Deadline: " + sendOfferTaskers.get(position).getOffer_deadline());
        holder.description.setText("Description: \n" + sendOfferTaskers.get(position).getOffer_description());

        holder.btnAcceptOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Accept Offer", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReferenceOffer = FirebaseDatabase.getInstance().getReference("Offers");
            }
        });
        holder.btnDeclineOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Decline Offer", Toast.LENGTH_SHORT).show();
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