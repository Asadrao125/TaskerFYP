package com.example.taskerfyp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ViewPostDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_detail);

        Toast.makeText(this, "Post id: " + getIntent().getStringExtra("post_ki_id"), Toast.LENGTH_SHORT).show();
    }
}