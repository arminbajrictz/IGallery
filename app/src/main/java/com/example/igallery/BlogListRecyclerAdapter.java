package com.example.igallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogListRecyclerAdapter extends  RecyclerView.Adapter<BlogListRecyclerAdapter.ViewHolder>{

    private ArrayList<BlogModel> mBlogs = new ArrayList<>();
    private Context mContext;

    public BlogListRecyclerAdapter(Context mContext,ArrayList<BlogModel> mBlogs) {
        this.mBlogs = mBlogs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_blog_tab,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BlogModel blogModel = mBlogs.get(position);
        final long passedStampKey = blogModel.getBlogTimestamp();
        setFont(holder.titleBlog);
        holder.titleBlog.setText(blogModel.getBlogTitle());
        Picasso.get().load(blogModel.getBlogPhotoUrl()).into(holder.thumbnailBlog);
        holder.cardBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = mContext.getSharedPreferences("userStatus", SignIn.MODE_PRIVATE);
                int userStatus = sp.getInt("logged", -1);
                Log.d("WHO IS LOGGED",""+String.valueOf(userStatus));

                Bundle bundle = new Bundle();
                bundle.putLong("passedkey",passedStampKey);

                if (userStatus==2) {
                    Intent intent = new Intent(mContext,BlogDetailUserScreen.class);
                    intent.putExtra("passedkey",passedStampKey);
                    mContext.startActivity(intent);
                }else if (userStatus==1){

                    BlogDetailsFragment blogDetailsFragment = new BlogDetailsFragment();
                    FragmentManager fragmentManager =((AppCompatActivity)mContext).getSupportFragmentManager();

                    blogDetailsFragment.setArguments(bundle);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.viewBlogLayout,blogDetailsFragment);
                    transaction.commit();

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mBlogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView thumbnailBlog;
        TextView titleBlog;
        CardView cardBlog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailBlog = itemView.findViewById(R.id.blogThumb);
            titleBlog = itemView.findViewById(R.id.blogTitleItem);
            cardBlog=itemView.findViewById(R.id.cardView);
        }
    }

    private void setFont (TextView textView) {
        Typeface typeface = Typeface.createFromAsset((mContext.getAssets()),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

}
