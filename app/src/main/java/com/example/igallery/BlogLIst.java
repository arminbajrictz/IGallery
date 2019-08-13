package com.example.igallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BlogLIst extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Button hombButtBlogLis;
    private TextView titleTextBlList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_list,container,false);

        hombButtBlogLis=view.findViewById(R.id.homeButtBlFrag);
        titleTextBlList=view.findViewById(R.id.titleTextBlogFrag);

        hombButtBlogLis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AdminHomeScreen.class);
                startActivity(intent);
            }
        });

        setFont(hombButtBlogLis);
        setFont(titleTextBlList);


        final RecyclerView recyclerView = view.findViewById(R.id.blogRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        final BlogListRecyclerAdapter adapter = new BlogListRecyclerAdapter(getContext(),ViewBlogs.blogsForRecycler);


        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("blogs");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BlogModel blogModel = ds.getValue(BlogModel.class);
                    ViewBlogs.blogsForRecycler.add(blogModel);
                }
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

}
