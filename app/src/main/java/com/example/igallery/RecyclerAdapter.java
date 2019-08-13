package com.example.igallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private final Context mContext;
    private ArrayList<PhotoModel> mPhotos;
    private final LayoutInflater mLayoutInflater;
    private int userStatus;
    private View itemView;

    public RecyclerAdapter(Context mContext, ArrayList<PhotoModel> mPhotos) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mPhotos = mPhotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SharedPreferences sp = mContext.getSharedPreferences("userStatus", SignIn.MODE_PRIVATE);
        userStatus = sp.getInt("logged", -1);

        if (userStatus==2) {
            itemView = mLayoutInflater.inflate(R.layout.user_gallery_custom_row,parent,false);
        }else {
            itemView = mLayoutInflater.inflate(R.layout.recyclerview_custom_layout, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PhotoModel photoModel = mPhotos.get(position);
        final long stampId = photoModel.getTimeStamp();

        Log.d("WHAT HAPPENED ",""+userStatus);

        Picasso.get().load(photoModel.getPhotoUrl()).into(holder.thumbnail);



        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (userStatus==2) {
                    Intent intent = new Intent(mContext,UserPhotoDet.class);
                    intent.putExtra("passedkey",stampId);
                    mContext.startActivity(intent);
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putLong("myKey",stampId);
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(bundle);
                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.gridGalleryFrame, detailsFragment);
                    transaction.commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView thumbnail;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.d("LOGGED IS",""+userStatus);

            if (userStatus==2) {
                thumbnail=itemView.findViewById(R.id.user_rec_image);
                Log.d("DID JOB",""+userStatus);
                itemView.setOnClickListener(this);
            }else {
                thumbnail = itemView.findViewById(R.id.circleImageForRecylcer);
                itemView.setOnClickListener(this);
            }

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener  = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }

}
