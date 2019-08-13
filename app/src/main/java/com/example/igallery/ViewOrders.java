package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {

    public static ArrayList<PhotoModel> photosForOrders;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView ordersRecycler;
    private OrdersAdapter adapter;
    private Button backHomeButt;
    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        photosForOrders=new ArrayList<>();
        backHomeButt=findViewById(R.id.backTohomeButt);
        activityTitle=findViewById(R.id.ordersTitle);


        backHomeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewOrders.this.onBackPressed();
            }
        });

        setFont(backHomeButt);
        setFont(activityTitle);


        ordersRecycler=findViewById(R.id.orders_recycler_list);
        LinearLayoutManager manager = new LinearLayoutManager(this) ;
        ordersRecycler.setLayoutManager(manager);

        mDatabase = FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
                    if (photoModel.getOnStock()==false){
                        photosForOrders.add(photoModel);
                    }
                }

                Log.d("NUMBER OF ORDERS", ""+photosForOrders.size());
                adapter= new OrdersAdapter(getApplicationContext(),photosForOrders);
                ordersRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
