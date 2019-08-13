package com.example.igallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Type;

public class AdminHomeScreen extends AppCompatActivity {

    private TextView titleTxtAdmin;
    private Button addPhotosButAd,viewGallButAd,addNewsButtAd,checkOrdButAd,blockUsersButAd,viewNewsButAd,signOutButAd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);
//        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();


        addPhotosButAd= findViewById(R.id.addPhotosAdmin);
        viewGallButAd=findViewById(R.id.viewGalleryAdmin);
        addNewsButtAd=findViewById(R.id.addNewsAdmin);
        checkOrdButAd=findViewById(R.id.checkOrdersAdmin);
        blockUsersButAd=findViewById(R.id.blockUsersAdmin);
        viewNewsButAd=findViewById(R.id.viewNewsAdmin);
        signOutButAd=findViewById(R.id.signOutAdmin);
        titleTxtAdmin=findViewById(R.id.adminTitleTxtV);

        setFont(addPhotosButAd);
        setFont(viewGallButAd);
        setFont(addNewsButtAd);
        setFont(checkOrdButAd);
        setFont(blockUsersButAd);
        setFont(viewNewsButAd);
        setFont(signOutButAd);
        setFont(titleTxtAdmin);
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void signOutAdmin (View view) {
        Toast.makeText(getApplicationContext(),"Bye!",Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        setUserLogoutStatus();
        startActivity(intent);
    }

    public void addPhotosAdmin (View view) {
        Intent intent = new Intent(getApplicationContext(),AddPhotos.class);
        startActivity(intent);
    }

    private void setUserLogoutStatus () {
        SharedPreferences sp = getSharedPreferences("userStatus", AdminHomeScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", -1);
        editor.commit();
    }

    public void vievGalleryAdmin (View view) {
        Intent intent = new Intent(getApplicationContext(),ViewGallery.class);
        startActivity(intent);
    }

    public void writeBlog (View view) {
        Intent intent = new Intent(getApplicationContext(),AddBlog.class);
        startActivity(intent);
    }

    public void viewBlogs(View view) {
        Intent intent = new Intent(getApplicationContext(),ViewBlogs.class);
        startActivity(intent);
    }

    public void blockUsersAdmin (View view) {
        Intent intent = new Intent(getApplicationContext(),BlockUsers.class);
        startActivity(intent);
    }

    public void viewOrders(View view) {
        Intent intent = new Intent(getApplicationContext(),ViewOrders.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
