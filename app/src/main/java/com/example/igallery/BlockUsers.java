package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class BlockUsers extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView recyclerView;
    private TextView activityTitle;
    private Button backHomeButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_users);

        activityTitle=findViewById(R.id.titleTextBlock);
        backHomeButt=findViewById(R.id.backhomeBlock);

        setFont(backHomeButt);
        setFont(activityTitle);

        final ArrayList<UserModel> userListForRecycler = new ArrayList<>();

        recyclerView = findViewById(R.id.block_user_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this) ;
        recyclerView.setLayoutManager(manager);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel userModel = ds.getValue(UserModel.class);
                    userListForRecycler.add(userModel);
                }
                Block_user_adapter adapter = new Block_user_adapter(getApplicationContext(),userListForRecycler);
                recyclerView.setAdapter(adapter);
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

    public void backHome(View view) {
        BlockUsers.this.onBackPressed();
    }
}
