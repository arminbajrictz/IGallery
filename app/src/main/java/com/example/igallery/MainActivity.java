package com.example.igallery;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView,subtitleTextView,registerTextView;
    private String titleMessage,subtitleMessage,registerMessage;
    private Button registerButton,signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();


        titleMessage = "Welcome to the IGallery!";
        subtitleMessage="Shop original,unique and affordable\nart from our huge online art gallery.";
        registerMessage = "If you don't have account yet, create one!";

        registerButton=findViewById(R.id.registerButton);
        signInButton  = findViewById(R.id.signInButton);
        titleTextView = findViewById(R.id.titleText);
        titleTextView.setText(titleMessage);
        subtitleTextView = findViewById(R.id.subtitleText);
        subtitleTextView.setText(subtitleMessage);
        registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setText(registerMessage);
        setFont(registerTextView);
        setFont(subtitleTextView);
        setFont(titleTextView);
        setFont(registerButton);
        setFont(signInButton);

    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void registerActivity(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }

    public void signInActivity(View view) {
        Intent intent = new Intent(getApplicationContext(),SignIn.class);
        startActivity(intent);
    }

}
