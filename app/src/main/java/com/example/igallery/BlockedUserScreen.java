package com.example.igallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BlockedUserScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView blockTitle, blockMessage;
    private Button backHomeBut;
    private String message = "You have been blocked temporarily from IGallery for making personal attacks " +
            "towards admin. Once the block has expired, you are welcome to make useful contributions." +
            "If you think there are good reasons for being unblocked, please read the guide and contact our support.";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_screen);
//        getSupportActionBar().hide();

        blockTitle = findViewById(R.id.blockScreenTitle);
        blockMessage=findViewById(R.id.subtitleBlockScreen);
        backHomeBut = findViewById(R.id.backHomeBlockUser);

        blockMessage.setText(message);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        setUserLogoutStatus();

        backHomeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        setFont(blockTitle);
        setFont(blockMessage);
        setFont(backHomeBut);

    }


    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    private void setUserLogoutStatus () {
        SharedPreferences sp = getSharedPreferences("userStatus", UserHomeScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", -1);
        editor.commit();
    }

    private void goHome () {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goHome();
    }
}

