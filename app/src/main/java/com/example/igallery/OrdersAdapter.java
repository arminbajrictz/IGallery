package com.example.igallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<PhotoModel> mPhotos;

    public OrdersAdapter(Context mContext, ArrayList<PhotoModel> mPhotos) {
        this.mContext = mContext;
        this.mPhotos = mPhotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_custom,parent,false);
        OrdersAdapter.ViewHolder holder = new OrdersAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PhotoModel photoModel = mPhotos.get(position);
        setFont(holder.orderTitle);
        holder.orderTitle.setText(photoModel.getTitle());
        Picasso.get().load(photoModel.getPhotoUrl()).into(holder.orderThumbnail);

        Log.d("TITLE IS ",""+photoModel.getTitle());
        Log.d("PRICE IS ",""+photoModel.getPrice());

        holder.orderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,OrderDetailsManagar.class);
                intent.putExtra("passedKey",photoModel.getTimeStamp());
//                Toast.makeText(mContext,"PASSED"+photoModel.getTimeStamp(),Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView orderThumbnail;
        TextView orderTitle;
        CardView orderCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderThumbnail=itemView.findViewById(R.id.orderThumbnail);
            orderTitle=itemView.findViewById(R.id.orderTitle);
            orderCard=itemView.findViewById(R.id.orderCard);
        }
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
