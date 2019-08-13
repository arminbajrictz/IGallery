package com.example.igallery;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Block_user_adapter extends RecyclerView.Adapter<Block_user_adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UserModel> users;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private int myPosition;
    private boolean myChecked;


    public Block_user_adapter(Context mContext, ArrayList<UserModel> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card_block_user,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final UserModel userModel = users.get(position);
        holder.blockUsernameTitle.setText(userModel.getEmail());
        setFont(holder.blockUsernameTitle);
        if (userModel.getBlocked()==true) {
            holder.blockSwitch.setChecked(true);
        }

        holder.blockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance();
                mRef = mDatabase.getReference("Users").child(userModel.getUsersId()).child("blocked");
                mRef.setValue(isChecked);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView blockUsernameTitle;
        Switch blockSwitch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.setIsRecyclable(false);
            blockUsernameTitle=itemView.findViewById(R.id.blockUserEmail);
            blockSwitch=itemView.findViewById(R.id.option);

        }
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }

}
