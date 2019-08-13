
package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserPhotoDet extends AppCompatActivity {

    private TextView photoTitleDet, photoPriceDet, descriptionDet, likesNum, stockState;
    private Button backToGallButt, buyButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;
    private ImageView photoFrameDet;
    private long passedKey;
    private int numLiked;
    private boolean foundUsersLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_det);

        ////////////

        passedKey = getIntent().getLongExtra("passedkey", 0);
        Log.d("I PASSED ", "" + passedKey);

        photoTitleDet = findViewById(R.id.titleTextDetFrag);
        photoPriceDet = findViewById(R.id.pricePhotDetailsFrag);
        descriptionDet = findViewById(R.id.descriptionDetFrag);
        backToGallButt = findViewById(R.id.backToGallButt);
        buyButton = findViewById(R.id.buyPaintingButt);
        photoFrameDet = findViewById(R.id.photoFrameDetFrag);
        likesNum = findViewById(R.id.likesNumDetFrag);
        stockState = findViewById(R.id.onStockDetails);

        descriptionDet.setMovementMethod(new ScrollingMovementMethod());

        setFont(photoTitleDet);
        setFont(photoPriceDet);
        setFont(descriptionDet);
        setFont(backToGallButt);
        setFont(buyButton);
        setFont(likesNum);
        setFont(stockState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(String.valueOf(passedKey))) {
                        PhotoModel photoModel = ds.getValue(PhotoModel.class);
                        photoTitleDet.setText(photoModel.getTitle());
                        photoPriceDet.setText("Price : " + photoModel.getPrice());
                        descriptionDet.setText(photoModel.getDescription());
                        likesNum.setText(String.valueOf(photoModel.getLikes()));
                        if (photoModel.getOnStock()) {
                            stockState.setText("In stock");
                        } else {
                            stockState.setText("Out of stock");
                            buyButton.setVisibility(View.INVISIBLE);
                        }
                        Picasso.get().load(photoModel.getPhotoUrl()).into(photoFrameDet);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backToGallButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToGallery();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog success = new AlertDialog.Builder(UserPhotoDet.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Thank you!")
                        .setMessage("You bought a painting! You can track order status in your basket tab.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.ic_action_name)
                        .setCancelable(true)
                        .show();

                TextView dialogTextView = (TextView) success.findViewById(android.R.id.message);
                setFont(dialogTextView);


                stockState.setText("Out of stock");
                mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                        .child(String.valueOf(passedKey)).child("onStock");
                mRef.setValue(false);

                mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                        .child(String.valueOf(passedKey)).child("buyer");
                mRef.setValue(mAuth.getCurrentUser().getEmail());

                buyButton.setVisibility(View.INVISIBLE);
            }
        });


        photoFrameDet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                foundUsersLike = false;

                numLiked = Integer.parseInt(likesNum.getText().toString());

                mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                        .child(String.valueOf(passedKey));

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("likedBy")) {

                            mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                                    .child(String.valueOf(passedKey)).child("likedBy");

                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                                            mRef.child(mAuth.getCurrentUser().getUid()).removeValue();
                                            numLiked--;
                                            setLikesFirebase(numLiked);
                                            foundUsersLike = true;
                                            Toast.makeText(getApplicationContext(), "Photo disliked!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (foundUsersLike == false) {
                                        Toast.makeText(getApplicationContext(), "Photo liked!", Toast.LENGTH_SHORT).show();
                                        numLiked++;
                                        setLikesFirebase(numLiked);
                                        setLikedBy();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "Photo liked!", Toast.LENGTH_SHORT).show();
                            numLiked++;
                            setLikesFirebase(numLiked);
                            setLikedBy();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            }
        });

    }


    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void backToGallery() {
        UserPhotoDet.this.onBackPressed();
    }

    private void setLikesFirebase(int passedNum) {
        likesNum.setText(String.valueOf(passedNum));
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                .child(String.valueOf(passedKey)).child("likes");
        mRef.setValue(passedNum);
    }

    private void setLikedBy() {
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos")
                .child(String.valueOf(passedKey)).child("likedBy").child(mAuth.getCurrentUser().getUid());
        mRef.setValue(true);
    }

}
