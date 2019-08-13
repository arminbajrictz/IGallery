package com.example.igallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasketAdapter extends  RecyclerView.Adapter<BasketAdapter.ViewHolder>{

    private ArrayList<PhotoModel> mPhotos ;
    private Context mContext;

    public BasketAdapter(Context mContext,ArrayList<PhotoModel> mPhotos) {
        this.mPhotos = mPhotos;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuustom_basket_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoModel photoModel = mPhotos.get(position);
        holder.titleBasket.setText(photoModel.getTitle());
        Picasso.get().load(photoModel.getPhotoUrl()).into(holder.thumbnailOrderBasket);
        Log.d("Url is",""+photoModel.getPhotoUrl());
        holder.price.setText("You paid "+photoModel.getPrice()+"$");
        switch (photoModel.getState()) {
            case 0 :
                holder.statOrder.setText("Order status :\nIn proceces of painting");
                holder.statsIcon.setImageResource(R.drawable.ic_proggrees);
                break;
            case 1:
                holder.statOrder.setText("Order status :\nOrder sent");
                holder.statsIcon.setImageResource(R.drawable.ic_send);
                break;
            case 2:
                holder.statOrder.setText("Order status :\nOrder delivered");
                holder.statsIcon.setImageResource(R.drawable.ic_delivered);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnailOrderBasket,statsIcon;
        TextView titleBasket,price,statOrder;
        CardView cardBlog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailOrderBasket = itemView.findViewById(R.id.circleImageView);
            titleBasket = itemView.findViewById(R.id.titleOfOrder);
            price=itemView.findViewById(R.id.priceOfOrder);
            cardBlog=itemView.findViewById(R.id.cardView);
            statOrder=itemView.findViewById(R.id.curenStateOrder);
            statsIcon=itemView.findViewById(R.id.iconState);

            setFont(titleBasket);
            setFont(price);
            setFont(statOrder);

        }
    }

    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset((mContext.getAssets()),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

}
