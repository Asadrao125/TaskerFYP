package com.example.taskerfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.taskerfyp.Models.RatingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Date;

public class ScanQRActivity extends AppCompatActivity {
    private CodeScanner codeScanner;
    private CodeScannerView scanView;
    String post_id;
    private AlertDialog alertDialog;
    String review;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r);

        scanView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scanView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        post_id = getIntent().getStringExtra("key");
                        Log.d("post_ki_id", "run: " + result.getText());

                        if (result.getText().equals(post_id)) {
                            Toast.makeText(ScanQRActivity.this, "IDs Matched!", Toast.LENGTH_SHORT).show();
                            showDialog();
                        } else {
                            Toast.makeText(ScanQRActivity.this, "IDs Does Not Matched!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        codeScanner.stopPreview();
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.dialog_rating, null);

        Button btnSubmit = v.findViewById(R.id.btnSubmit);
        final EditText edtReview = v.findViewById(R.id.review);
        final RatingBar ratingBar = v.findViewById(R.id.rating);

        alertDialog = new AlertDialog.Builder(ScanQRActivity.this)
                .setView(v)
                .create();

        alertDialog.show();

        Log.d("review", "showDialog: " + review);
        Log.d("rating", "showDialog: " + rating);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Offers").child(post_id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String offer_sender_id = ds.child("offer_sender_id").getValue().toString();
                            String name = ds.child("userName").getValue().toString();
                            Log.d("offer_sender_id", "onDataChange: " + offer_sender_id);
                            Date d = new Date();
                            String date = String.valueOf(DateFormat.format("MMMM d, yyyy ", d.getTime()));
                            DatabaseReference mRefrence = FirebaseDatabase.getInstance().getReference("Ratings");
                            review = edtReview.getText().toString().trim();
                            rating = ratingBar.getRating();
                            RatingModel ratingModel = new RatingModel(rating, review, date, name);
                            mRefrence.child(offer_sender_id).push().setValue(ratingModel);

                            //Setting Post Completion Status
                           /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference("All_Posts")
                                    .child(FirebaseAuth.getInstance().getUid()).child(post_id);
                            reference.child("status").setValue("Completed");*/

                            startActivity(new Intent(getApplicationContext(), ViewPost.class));
                            //finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                alertDialog.cancel();
            }
        });
    }
}