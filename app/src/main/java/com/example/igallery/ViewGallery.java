package com.example.igallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import java.util.ArrayList;

public class ViewGallery extends AppCompatActivity {

    static ArrayList<PhotoModel> photosForRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gallery);
//        getSupportActionBar().hide();

        photosForRecycler = new ArrayList<PhotoModel>();
        GridGalleryFragment gridGalleryFragment = new GridGalleryFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.gridGalleryFrame,gridGalleryFragment,"gridGallery");
        transaction.commit();

    }
}
