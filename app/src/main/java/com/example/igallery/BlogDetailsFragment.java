package com.example.igallery;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BlogDetailsFragment extends Fragment {

    private ImageView detailBlogPh;
    private TextView deatilBlogTitle, deatilBlogText;
    private Button backToBlogsButt, deleteBlogBut;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private long passedKey;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blog_details_fragment, container, false);

        Bundle bundle = new Bundle();
        passedKey = getArguments().getLong("passedkey");
        detailBlogPh = view.findViewById(R.id.blog_details_photo);
        deatilBlogTitle = view.findViewById(R.id.titleBlogDetails);
        deatilBlogText = view.findViewById(R.id.blogDetailsText);
        backToBlogsButt = view.findViewById(R.id.backToBlogsButt);
        deleteBlogBut = view.findViewById(R.id.deleteBlog);

        backToBlogsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToBlogs();
            }
        });

        deleteBlogBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlog();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN){
                    return true;
                }
                return false;
            }
        });

        setFont(deatilBlogTitle);
        setFont(deatilBlogText);
        setFont(backToBlogsButt);
        setFont(deleteBlogBut);

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
        return view;
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    private void backToBlogs () {
        BlogLIst blogLIst = new BlogLIst();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.viewBlogLayout,blogLIst);
        ViewBlogs.blogsForRecycler.clear();
        transaction.commit();
    }

    public void deleteBlog() {

        android.app.AlertDialog success = new android.app.AlertDialog.Builder(getActivity()).
                setTitle("Delete warning")
                .setMessage("Are you sure you want to delete this blog?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("blogs");
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getKey().equals(String.valueOf(passedKey))) {
                                        BlogModel blogModel = ds.getValue(BlogModel.class);
                                        mStorage = FirebaseStorage.getInstance();
                                        mStorRef = mStorage.getReferenceFromUrl(blogModel.getBlogPhotoUrl());
                                        mStorRef.delete();
                                    }
                                }
                                mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("blogs").child(String.valueOf(passedKey));
                                mRef.removeValue();
                                Toast.makeText(getContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                backToBlogs();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_delete)
                .setCancelable(false)
                .show();

        TextView dialogTextView = success.findViewById(android.R.id.message);
        setFont(dialogTextView);
    }

}

