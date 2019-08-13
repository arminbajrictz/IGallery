package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHomeScreen extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    public static ArrayList<PhotoModel>orderedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);
//        getSupportActionBar();

//        orderedList = new ArrayList<>();
//
//        mDatabase=FirebaseDatabase.getInstance();
//        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2")
//                .child("photos");
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
//                    if (photoModel.getBuyer().equals(mAuth.getCurrentUser().getEmail())) {
//                        orderedList.add(photoModel);
//                    }
//                }
//                Log.d("OVLIKO IH IMA",""+orderedList.size());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        TabLayout tabLayout = findViewById(R.id.tablayout);
        TabItem tabHome = findViewById(R.id.homeTab);
        TabItem tabBasket = findViewById(R.id.basketTab);
        TabItem tabBlogs = findViewById(R.id.blogsTab);
        ViewPager viewPager = findViewById(R.id.viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_basket);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_blog);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#AFB6FF"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                        UserModel userModel = ds.getValue(UserModel.class);
                        Log.d("USER IS BLOCKED = ",""+userModel.getBlocked());
                        if (userModel.getBlocked()) {
                            Intent blockintent = new Intent(getApplicationContext(), BlockedUserScreen.class);
                            startActivity(blockintent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        setUserLogoutStatus();
//        mAuth.signOut();
    }

    private void setUserLogoutStatus () {
        SharedPreferences sp = getSharedPreferences("userStatus", UserHomeScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", -1);
        editor.commit();
    }
}
