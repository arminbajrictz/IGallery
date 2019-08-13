package com.example.igallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailsManagar extends AppCompatActivity {

    private TextView activityTitle,orderMessage,infoMessage,orderStatMess;
    private Button goToOrders;
    private String messageToPut,secondMessage;
    private String customer ;
    private String price;
    private String photoTitle;
    private long passedId;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RadioButton inProc,sent,delivered;
    private RadioGroup choices;
    private int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_managar);
        passedId=getIntent().getLongExtra("passedKey",0);
        Log.d("PASSED IN ORDERS",String.valueOf(passedId));

        activityTitle=findViewById(R.id.ordersDetailTitle);
        orderMessage=findViewById(R.id.ordersMessage);
        infoMessage=findViewById(R.id.ordesrUserInfo);
        choices=findViewById(R.id.RGroup);
        inProc=findViewById(R.id.inProcc);
        sent=findViewById(R.id.orderSent);
        delivered=findViewById(R.id.delivered);
        goToOrders=findViewById(R.id.homeButtOrders);

        goToOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsManagar.this.onBackPressed();
            }
        });


        mDatabase= FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2").child("photos");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
                    if (photoModel.getTimeStamp()==passedId) {
                        customer=photoModel.getBuyer();
                        price=photoModel.getPrice();
                        photoTitle=photoModel.getTitle();
                        status=photoModel.getState();
                    }
                    switch (status) {
                        case 0:
                            inProc.setChecked(true);
                            break;
                        case 1:
                            sent.setChecked(true);
                            break;
                        case 2:
                            delivered.setChecked(true);
                            break;
                    }
                }
                messageToPut ="Money goes into your piggy bank!\n" +
                        "Now let's act like a real pro and notifiy\n" +
                        "customer about order status.";
                secondMessage=""+customer+"\n bought your painting " +photoTitle+
                        " for "+price+"$.";
                orderStatMess=findViewById(R.id.orderStatMessage);
                setFont(orderStatMess);
                orderMessage.setText(messageToPut);
                infoMessage.setText(secondMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setFont(activityTitle);
        setFont(orderMessage);
        setFont(infoMessage);
        setFont(inProc);
        setFont(sent);
        setFont(delivered);
        setFont(goToOrders);




        choices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (choices.getCheckedRadioButtonId()) {
                    case R.id.inProcc:
                        setOrderState(0);
                        break;
                    case R.id.orderSent :
                        setOrderState(1);
                        break;
                    case R.id.delivered:
                        setOrderState(2);
                        break;
                }
            }
        });


    }

    private void setOrderState(int state) {
        mRef=mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2")
                .child("photos").child(String.valueOf(passedId)).child("state");
        mRef.setValue(state);
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
