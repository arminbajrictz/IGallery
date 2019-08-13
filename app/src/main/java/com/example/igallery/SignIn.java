package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private TextView titleTextSign, subtitleTextSign, emailTxtVSign, passTxtVSign;
    private EditText emailEdtTxtSign, passEdtTxtSign;
    private Button signInButSign, backHomeButtSign;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ProgressBar loadingCurrentUser;
    private final int AdminLogged = 1;
    private final int UserLogged = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        getSupportActionBar().hide();

        titleTextSign = findViewById(R.id.titleTextSignIn);
        subtitleTextSign = findViewById(R.id.subtitleTextSignIn);
        emailTxtVSign = findViewById(R.id.emailTextViewSign);
        passTxtVSign = findViewById(R.id.passTextViewSign);
        emailEdtTxtSign = findViewById(R.id.emailEditTexSign);
        passEdtTxtSign = findViewById(R.id.passwordEditTextSign);
        signInButSign = findViewById(R.id.signInButtonSign);
        backHomeButtSign = findViewById(R.id.homeButtSign);
        loadingCurrentUser = findViewById(R.id.loadingProgressSign);


        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setFont(titleTextSign);
        setFont(subtitleTextSign);
        setFont(emailTxtVSign);
        setFont(passTxtVSign);
        setFont(emailEdtTxtSign);
        setFont(passEdtTxtSign);
        setFont(signInButSign);
        setFont(backHomeButtSign);


//        switch (getUserStatus()) {
//            case AdminLogged:
//                Intent intent = new Intent(getApplicationContext(), AdminHomeScreen.class);
//                startActivity(intent);
//                break;
//            case UserLogged:
//                Intent intentUser = new Intent(getApplicationContext(), AdminHomeScreen.class);
//                startActivity(intentUser);
//                break;
//            case 0 :
//                setUpAuthState();
//
//        }

        setUpAuthState();

        loadingCurrentUser.setVisibility(View.INVISIBLE);
        loadingCurrentUser.getIndeterminateDrawable().setColorFilter(0xffec5946, android.graphics.PorterDuff.Mode.MULTIPLY);


    }

    private void setFont(EditText editText) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        editText.setTypeface(typeface);
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void backHome(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void signUser(View view) {
        if (isEmpty()) {
            Toast.makeText(getApplicationContext(), "You should fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            if (isValidEmailAddress(emailEdtTxtSign.getText().toString())) {
                mAuth.signInWithEmailAndPassword(emailEdtTxtSign.getText().toString(), passEdtTxtSign.getText().toString())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "This account doesn't exist!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpAuthState() {
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() != null) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {

                        switch (getUserStatus()) {
                            case AdminLogged:
                                switchAdminScreen();
                                break;
                            case UserLogged:
                                switchUserScreen();
                                break;
                            case -1:
                                loadingCurrentUser.setVisibility(View.VISIBLE);
                                mRef = mDatabase.getReference("Admin");
                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (mAuth.getCurrentUser().getUid().equals(ds.getKey())) {
                                                loadingCurrentUser.setVisibility(View.INVISIBLE);
                                                setUserStatus(AdminLogged);
                                                switchAdminScreen();

                                            } else {
                                                loadingCurrentUser.setVisibility(View.INVISIBLE);
                                                setUserStatus(UserLogged);
                                                switchUserScreen();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        }

                    } else {

                        Toast.makeText(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        Log.d("user", "Signed out");
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mAuth.removeAuthStateListener(mListener);
        }
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(emailEdtTxtSign.getText()) || TextUtils.isEmpty(passEdtTxtSign.getText())) {
            return true;
        } else {
            return false;
        }
    }

    private void setUserStatus(int status) {
        SharedPreferences sp = getSharedPreferences("userStatus", SignIn.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", status);
        editor.commit();
    }

    private int getUserStatus() {
        SharedPreferences sp = getSharedPreferences("userStatus", SignIn.MODE_PRIVATE);
        int userStatus = sp.getInt("logged", -1);
        return userStatus;
    }

    private void switchAdminScreen() {
        Intent intent = new Intent(getApplicationContext(), AdminHomeScreen.class);
        startActivity(intent);
    }

    private void switchUserScreen() {
        Intent intent = new Intent(getApplicationContext(), UserHomeScreen.class);
        startActivity(intent);
    }

}
