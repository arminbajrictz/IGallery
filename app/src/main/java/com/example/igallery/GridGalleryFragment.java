package com.example.igallery;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class GridGalleryFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private RecyclerAdapter recyclerAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TextView titleTextgridFragment;
    private Button backHomeGridFrag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_gallery,container,false);

        myRecyclerView = view.findViewById(R.id.gridRecyclerView);
        titleTextgridFragment = view.findViewById(R.id.titleTextgridFragment);
        backHomeGridFrag = view.findViewById(R.id.homeButtGridFrag);

        backHomeGridFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        setFont(titleTextgridFragment);
        setFont(backHomeGridFrag);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
                    ViewGallery.photosForRecycler.add(photoModel);
                    recyclerAdapter = new RecyclerAdapter(getContext(),ViewGallery.photosForRecycler);
                    myRecyclerView.setAdapter(recyclerAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void backToHome () {
        Intent intent = new Intent(getContext(),AdminHomeScreen.class);
        startActivity(intent);
    }
    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
