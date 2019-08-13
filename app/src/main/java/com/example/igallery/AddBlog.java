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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBlog extends AppCompatActivity {

    private TextView activityTitle, blogTitle,tapBlogPhoto;
    private EditText blogTitleEdtTxt,blogEdtTxt;
    private Button publishButt,cancelBlogButt;
    private CircleImageView blogPhoto;
    private boolean isUpadated ;
    private Uri imageUri;
    private Bitmap selectedImage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
//        getSupportActionBar().hide();

        activityTitle=findViewById(R.id.writeBlogAdd);
        blogTitle=findViewById(R.id.blogTitleTxtV);
        tapBlogPhoto = findViewById(R.id.tapTextAddBlog);
        blogTitleEdtTxt= findViewById(R.id.blogTitleEdtTxt);
        blogEdtTxt=findViewById(R.id.blogEdtTxt);
        publishButt=findViewById(R.id.publishButt);
        cancelBlogButt=findViewById(R.id.cancelButtAddBl);
        blogPhoto=findViewById(R.id.blogPhoto);
        loading=findViewById(R.id.loadingProgressaAdBl);

        loading.setVisibility(View.INVISIBLE);
        loading.getIndeterminateDrawable().setColorFilter(0xffec5946, android.graphics.PorterDuff.Mode.MULTIPLY);

        setFont(activityTitle);
        setFont(blogTitle);
        setFont(tapBlogPhoto);
        setFont(blogTitleEdtTxt);
        setFont(blogEdtTxt);
        setFont(publishButt);
        setFont(cancelBlogButt);

        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();

        cancelBlogButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToAdminPage();
            }
        });

    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void browseBlogPhoto (View view) {
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
                blogPhoto.setImageBitmap(selectedImage);
                isUpadated=true;

            } catch (FileNotFoundException e) {
                isUpadated=false;
                e.printStackTrace();
                }
            }
        }

    public void publishBlog (View view) {
        if (isUpadated) {
            if (!isEmpty()) {
                loading.setVisibility(View.VISIBLE);
                publishButt.setClickable(false);
                cancelBlogButt.setClickable(false);
                Date date = new Date();
                final long timeStamp = date.getTime();

                mStorRef=mStorage.getReference("adminContent").child("blogPhotos").child(String.valueOf(timeStamp));
                mStorRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoUrlFromDatabase = uri.toString();
                                BlogModel blogModel = new BlogModel();
                                blogModel.setBlogTitle(blogTitleEdtTxt.getText().toString());
                                blogModel.setBlogText(blogEdtTxt.getText().toString());
                                blogModel.setBlogTimestamp(timeStamp);
                                blogModel.setBlogPhotoUrl(photoUrlFromDatabase);

                                mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("blogs").child(String.valueOf(timeStamp));
                                mRef.setValue(blogModel);

                                loading.setVisibility(View.INVISIBLE);
                                createAnotherBlog();
                            }
                        });
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(),"You need to type blog title and text",Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(getApplicationContext(),"You need to select blog photo",Toast.LENGTH_SHORT).show();
        }



    }

    private void createAnotherBlog () {
        new android.app.AlertDialog.Builder(AddBlog.this)
                .setTitle("Blog published!")
                .setMessage("Do you want to write a new blog ?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAll();
                    }
                })
                .setNegativeButton("No thanks!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backToAdminPage();
                    }
                })
                .setIcon(R.drawable.ic_success)
                .setCancelable(false)
                .show();
    }


    private boolean isEmpty () {
        if (TextUtils.isEmpty(blogTitleEdtTxt.getText()) || TextUtils.isEmpty(blogEdtTxt.getText())) {
            return true;
        }else {
            return false;
        }
    }

    private void clearAll() {
        blogTitleEdtTxt.getText().clear();
        blogEdtTxt.getText().clear();
        blogPhoto.setImageResource(R.drawable.deftumb);
        loading.setVisibility(View.INVISIBLE);
        publishButt.setClickable(true);
        cancelBlogButt.setClickable(true);
     }

     private void backToAdminPage () {
        Intent intent = new Intent(getApplicationContext(),AdminHomeScreen.class);
        startActivity(intent);
     }
}

