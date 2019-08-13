package com.example.igallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPhotos extends AppCompatActivity {

    private TextView addTitleTxt,tapTxt,paintTitletxt,paintPriceTxt;
    private EditText paintTitleEditTxt,paintPriceEditTxt,descriptionEditTxt;
    private Button confirmButAddPhot,cancelButAddPhot;
    private CircleImageView photoThumbnailAdd;
    private Bitmap selectedImage;
    private boolean isUpadated;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;
    private Uri imageUri;
    private String photoUrl;
    private ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
//        getSupportActionBar().hide();

        addTitleTxt = findViewById(R.id.addPhotosTitleTxt);
        tapTxt=findViewById(R.id.tapTextAdd);
        paintTitletxt=findViewById(R.id.photoTitleTextViewAdd);
        paintPriceTxt=findViewById(R.id.priceTitleTxtAdd);
        paintPriceEditTxt=findViewById(R.id.priceEditTxtAdd);
        paintTitleEditTxt=findViewById(R.id.paintTitleEditTxtAdd);
        descriptionEditTxt=findViewById(R.id.descriptionEditText);
        confirmButAddPhot=findViewById(R.id.confirmAdd);
        cancelButAddPhot=findViewById(R.id.cancelAdd);
        photoThumbnailAdd = findViewById(R.id.photoThumbnailAdd);
        loadingProgress=findViewById(R.id.loadingProgressaAdPh);

        loadingProgress.setVisibility(View.INVISIBLE);
        loadingProgress.getIndeterminateDrawable().setColorFilter(0xffec5946, android.graphics.PorterDuff.Mode.MULTIPLY);

        setFont(addTitleTxt);
        setFont(tapTxt);
        setFont(paintTitletxt);
        setFont(paintPriceTxt);
        setFont(paintPriceEditTxt);
        setFont(paintTitleEditTxt);
        setFont(descriptionEditTxt);
        setFont(cancelButAddPhot);
        setFont(confirmButAddPhot);

        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

    }

    private void setFont (EditText editText) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        editText.setTypeface(typeface);
    }

    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void cancelAdd (View view) {
        Intent intent = new Intent(getApplicationContext(),AdminHomeScreen.class);
        startActivity(intent);
    }

    public void browsePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0 && resultCode==RESULT_OK) {

            try {

                imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                photoThumbnailAdd.setImageBitmap(selectedImage);
                isUpadated=true;

            } catch (FileNotFoundException e) {
                isUpadated=false;
                e.printStackTrace();
            }
        }
    }

    public void confirmAdd (View view) {
        if (isUpadated) {
            if (!isEmpty()) {
                loadingProgress.setVisibility(View.VISIBLE);
                confirmButAddPhot.setClickable(false);
                cancelButAddPhot.setClickable(false);
                final Date date= new Date();
                mStorRef = mStorage.getReference("adminContent").child(String.valueOf(date.getTime()));
                mStorRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                android.app.AlertDialog success = new android.app.AlertDialog.Builder(AddPhotos.this)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Photo added to gallery!")
                                        .setMessage("Do you want to add another one ?")
                                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            clearAll();
                                            }
                                        }).setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switchAdminScreen();
                                            }
                                        })
                                        .setIcon(R.drawable.ic_succ)
                                        .setCancelable(false)
                                        .show();

                                TextView dialogTextView = (TextView) success.findViewById(android.R.id.message);
                                setFont(dialogTextView);

                                loadingProgress.setVisibility(View.INVISIBLE);
                                photoUrl = uri.toString();

                                PhotoModel photoModel = new PhotoModel();
                                photoModel.setTitle(paintTitleEditTxt.getText().toString());
                                photoModel.setPrice(paintPriceEditTxt.getText().toString());
                                photoModel.setPhotoUrl(photoUrl);
                                photoModel.setTimeStamp(date.getTime());
                                photoModel.setDescription(descriptionEditTxt.getText().toString());
                                photoModel.setLikes(0);
                                photoModel.setOnStock(true);
                                photoModel.setBuyer("");

                                mRef = mDatabase.getReference("Admin").child(mAuth.getCurrentUser().getUid()).child("photos")
                                        .child(String.valueOf(photoModel.getTimeStamp()));
                                mRef.setValue(photoModel);
                            }
                        });
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),"You need to enter painting title and price",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"You need to select painting photo",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty () {
        if (TextUtils.isEmpty(paintTitleEditTxt.getText()) || TextUtils.isEmpty(paintPriceEditTxt.getText())) {
            return true;
        }else {
            return false;
        }
    }

    private void clearAll() {
        isUpadated=false;
        photoThumbnailAdd.setImageResource(R.drawable.deftumb);
        paintTitleEditTxt.getText().clear();
        paintPriceEditTxt.getText().clear();
        descriptionEditTxt.getText().clear();
        confirmButAddPhot.setClickable(true);
        cancelButAddPhot.setClickable(true);
    }

    private void switchAdminScreen() {
        Intent intent = new Intent(getApplicationContext(),AdminHomeScreen.class);
        startActivity(intent);
    }

}
