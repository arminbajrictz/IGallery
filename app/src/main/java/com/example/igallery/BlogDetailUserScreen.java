package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static java.lang.Long.getLong;

public class BlogDetailUserScreen extends AppCompatActivity {

    private ImageView detailBlogPh;
    private TextView deatilBlogTitle, deatilBlogText;
    private Button backToBlogsButt;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private long passedKey;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail_user_screen);


        passedKey = getIntent().getLongExtra("passedkey",0);
        detailBlogPh = findViewById(R.id.blog_details_photo);
        deatilBlogTitle = findViewById(R.id.titleBlogDetails);
        deatilBlogText =  findViewById(R.id.blogDetailsText);
        backToBlogsButt =findViewById(R.id.backToBlogsButt);


        backToBlogsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogDetailUserScreen.this.onBackPressed();
            }
        });

        setFont(deatilBlogTitle);
        setFont(deatilBlogText);
        setFont(backToBlogsButt);

        deatilBlogText.setMovementMethod(new ScrollingMovementMethod());

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("blogs");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(String.valueOf(passedKey))) {
                        BlogModel blogModel = ds.getValue(BlogModel.class);
                        Picasso.get().load(blogModel.getBlogPhotoUrl()).into(detailBlogPh);
                        deatilBlogText.setText(blogModel.getBlogText());
                        deatilBlogTitle.setText(blogModel.getBlogTitle());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
