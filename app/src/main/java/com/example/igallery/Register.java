package com.example.igallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.Date;

public class Register extends AppCompatActivity {

    private TextView emailTextView,passwordTextView,repeatPassTextView,titleTextView,subtitleTextView;
    private EditText emailEdit,passEdit,repeatPassEdit;
    private Button saveButtonReg,backButt;
    private ProgressBar loadingProgress;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();

        titleTextView=findViewById(R.id.titleTextRegister);
        subtitleTextView=findViewById(R.id.subtitleTextRegister);
        emailTextView =findViewById(R.id.emailTextViewReg);
        passwordTextView=findViewById(R.id.passTextViewReg);
        repeatPassTextView=findViewById(R.id.repeatPassTextViewReg);
        emailEdit=findViewById(R.id.emailEditTextRegister);
        passEdit=findViewById(R.id.passwordEditTextRegister);
        repeatPassEdit=findViewById(R.id.passwordRepeatEditTextRegister);
        saveButtonReg = findViewById(R.id.saveButtonReg);
        loadingProgress = findViewById(R.id.loadingProgressReg);
        backButt=findViewById(R.id.backToMainButt);

        mAuth = FirebaseAuth.getInstance();

        setFont(titleTextView);
        setFont(subtitleTextView);
        setFont(emailTextView);
        setFont(passwordTextView);
        setFont(repeatPassTextView);
        setFont(emailEdit);
        setFont(passEdit);
        setFont(repeatPassEdit);
        setFont(saveButtonReg);
        setFont(backButt);


        loadingProgress.setVisibility(View.INVISIBLE);
        loadingProgress.getIndeterminateDrawable().setColorFilter(0xffec5946, android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    public void saveRegistration(View view) {
        loadingProgress.setVisibility(View.VISIBLE);
        if (isEmpty()) {
            loadingProgress.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"You should fill all field",Toast.LENGTH_SHORT).show();
        }else {
            if (isValidEmailAddress(emailEdit.getText().toString())) {
                if (passwordMatch()) {
                    if (passEdit.length()<6) {
                        loadingProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Password must contains at least 6 characters",Toast.LENGTH_SHORT).show();
                    }else {
                        createNewUser(emailEdit.getText().toString(),passEdit.getText().toString());
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Password doesn't match",Toast.LENGTH_SHORT).show();
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }else {
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Please enter valid email",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNewUser (String email,String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("User","Created for sure!");
                loadingProgress.setVisibility(View.INVISIBLE);
                mAuth.getCurrentUser().sendEmailVerification();

                UserModel userModel = new UserModel(emailEdit.getText().toString(),passEdit.getText().toString());
                userModel.setAdmin(false);
                userModel.setBlocked(false);
                userModel.setUsersId(mAuth.getCurrentUser().getUid());
                mDatabase = FirebaseDatabase.getInstance();
                mRef=mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid());
                mRef.setValue(userModel);

                mAuth.signOut();
                setUserLogoutStatus();

                AlertDialog success = new AlertDialog.Builder(Register.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Well done!")
                        .setMessage("You've created your own account.   Check your email inbox to verify account!")
                        .setPositiveButton("Back to home", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setIcon(R.drawable.ic_action_name)
                        .setCancelable(false)
                        .show();

                TextView dialogTextView = (TextView) success.findViewById(android.R.id.message);
                setFont(dialogTextView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Account already created with email : " +emailEdit.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean passwordMatch () {
        if (passEdit.getText().toString().equals(repeatPassEdit.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isEmpty () {
        if (TextUtils.isEmpty(emailEdit.getText()) || TextUtils.isEmpty(passEdit.getText()) || TextUtils.isEmpty(repeatPassEdit.getText())) {
            return true;
        }else {
            return false;
        }
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    private void setFont(EditText editText) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        editText.setTypeface(typeface);
    }

    public void backHome (View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private void setUserLogoutStatus () {
        SharedPreferences sp = getSharedPreferences("userStatus", UserHomeScreen.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("logged", -1);
        editor.commit();
    }
}
