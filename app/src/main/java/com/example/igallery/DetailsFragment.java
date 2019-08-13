package com.example.igallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    private TextView photoTitleDet,photoPriceDet,descriptionDet,likesNum,stockState;
    private Button backToGallButt,deletePhotoButt;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorRef;
    private ImageView photoFrameDet;
    private long passedKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment,container,false);

        Bundle bundle = new Bundle();
        passedKey = getArguments().getLong("myKey");
        Log.d("I PASSED ",""+passedKey);

        photoTitleDet = view.findViewById(R.id.titleTextDetFrag);
        photoTitleDet = view.findViewById(R.id.titleTextDetFrag);
        photoPriceDet =view.findViewById(R.id.pricePhotDetailsFrag);
        descriptionDet=view.findViewById(R.id.descriptionDetFrag);
        backToGallButt=view.findViewById(R.id.backToGallButt);
        deletePhotoButt = view.findViewById(R.id.deletePaintingBut);
        photoFrameDet=view.findViewById(R.id.photoFrameDetFrag);
        likesNum=view.findViewById(R.id.likesNumDetFrag);
        stockState=view.findViewById(R.id.onStockDetails);

        descriptionDet.setMovementMethod(new ScrollingMovementMethod());

        setFont(photoTitleDet);
        setFont(photoPriceDet);
        setFont(descriptionDet);
        setFont(backToGallButt);
        setFont(likesNum);
        setFont(deletePhotoButt);
        setFont(stockState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(String.valueOf(passedKey))){
                        PhotoModel photoModel = ds.getValue(PhotoModel.class);
                        photoTitleDet.setText(photoModel.getTitle());
                        photoPriceDet.setText("Price : "+photoModel.getPrice());
                        descriptionDet.setText(photoModel.getDescription());
                        likesNum.setText(String.valueOf(photoModel.getLikes()));
                        if (photoModel.getOnStock()){
                            stockState.setText("In stock");
                        }else {
                            stockState.setText("Out of stock");
                        }
                        Picasso.get().load(photoModel.getPhotoUrl()).into(photoFrameDet);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backToGallButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToGallery();
            }
        });

        deletePhotoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePainting();
            }
        });

        return view;
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

    public void backToGallery() {

        GridGalleryFragment gridGalleryFragment =new GridGalleryFragment();
        FragmentManager manager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.gridGalleryFrame,gridGalleryFragment);
        ViewGallery.photosForRecycler.clear();
        transaction.commit();

    }

    public void deletePainting() {

        android.app.AlertDialog success = new android.app.AlertDialog.Builder(getActivity()).
                setTitle("Delete warning")
                .setMessage("Are you sure you want to delete this painting?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
                        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getKey().equals(String.valueOf(passedKey))) {
                                        PhotoModel photoModel = ds.getValue(PhotoModel.class);
                                        mStorage = FirebaseStorage.getInstance();
                                        mStorRef = mStorage.getReferenceFromUrl(photoModel.getPhotoUrl());
                                        mStorRef.delete();
                                    }
                                }
                                mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos").child(String.valueOf(passedKey));
                                mRef.removeValue();
                                Toast.makeText(getContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                                backToGallery();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_delete)
                .setCancelable(false)
                .show();

        TextView dialogTextView = success.findViewById(android.R.id.message);
        setFont(dialogTextView);
    }
}
