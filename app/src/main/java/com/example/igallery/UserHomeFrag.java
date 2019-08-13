package com.example.igallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHomeFrag extends Fragment {

    private RecyclerView userRecycler;
    private RecyclerAdapter recyclerAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<PhotoModel> photosForRecycler;
    private TextView meessage;
    private String msgText;
    private FirebaseAuth mAuth;
    private Button signOutButt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_home_screen,container,false);

        photosForRecycler=new ArrayList<PhotoModel>();
        userRecycler = view.findViewById(R.id.user_recycler);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        userRecycler.setLayoutManager(staggeredGridLayoutManager);
        meessage=view.findViewById(R.id.messageToUser);
        signOutButt=view.findViewById(R.id.signOutUser);

        signOutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mAuth=FirebaseAuth.getInstance();

        setFont(meessage);
        setFont(signOutButt);

        msgText="Tap on photo to view painting details!";
        meessage.setText(msgText);


        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
                    photosForRecycler.add(photoModel);
                    recyclerAdapter = new RecyclerAdapter(getContext(),photosForRecycler);
                    userRecycler.setAdapter(recyclerAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    private void signOut() {
        setUserLogoutStatus();
        mAuth.signOut();
        Intent intent = new Intent(getContext(),MainActivity.class);
        startActivity(intent);
    }

    private void setUserLogoutStatus () {
        SharedPreferences sp = getActivity().getSharedPreferences("userStatus", UserHomeScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", -1);
        editor.commit();
    }
}
