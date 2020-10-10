package com.example.taskerfyp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.taskerfyp.Models.Post;
import com.example.taskerfyp.Models.RatingModel;
import com.example.taskerfyp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.MyViewHolder> {
    Context context;
    ArrayList<RatingModel> ratingModel;

    public RatingsAdapter(Context c, ArrayList<RatingModel> p) {
        context = c;
        ratingModel = p;
    }

    @NonNull
    @Override
    public RatingsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rating, parent, false);
        return new RatingsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(ratingModel.get(position).getName());
        holder.tv_date.setText(ratingModel.get(position).getDate());
        holder.tv_review.setText(ratingModel.get(position).getReview());
        holder.ratingBar.setRating(ratingModel.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return ratingModel.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_date, tv_review;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_username);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_review = itemView.findViewById(R.id.tv_review);
            ratingBar = itemView.findViewById(R.id.ratingbar);
        }
    }
}