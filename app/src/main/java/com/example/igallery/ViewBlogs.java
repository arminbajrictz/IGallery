package com.example.igallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.ArrayList;

public class ViewBlogs extends AppCompatActivity {

    static ArrayList<BlogModel> blogsForRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blogs);
//        getSupportActionBar().hide();


        blogsForRecycler = new ArrayList<>();

        BlogLIst blogLIst = new BlogLIst();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.viewBlogLayout,blogLIst);
        transaction.commit();

    }

}
