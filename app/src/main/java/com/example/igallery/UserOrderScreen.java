package com.example.igallery;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserOrderScreen extends Fragment {

    private ArrayList<PhotoModel> orderedList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private TextView activityTitle, firstBuy;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_order_screen, container, false);

        orderedList = new ArrayList<>();
        activityTitle = view.findViewById(R.id.basketTitleTxt);
        firstBuy = view.findViewById(R.id.firstBuyTitle);

        setFont(activityTitle);

        recyclerView = view.findViewById(R.id.basketRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        final BasketAdapter adapter = new BasketAdapter(getContext(), orderedList);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Admin").child("shYGRxbuZYN2Zowu2kr0qHYveva2")
                .child("photos");
        mRef.addValueEventListener(new ValueEventListener() {
            SharedPreferences sp = getActivity().getSharedPreferences("userStatus", SignIn.MODE_PRIVATE);
            int userStatus = sp.getInt("logged", -1);

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderedList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PhotoModel photoModel = ds.getValue(PhotoModel.class);
                    if (photoModel.getBuyer().equals(mAuth.getCurrentUser().getEmail())) {
                        orderedList.add(photoModel);
                    }
                    recyclerView.setAdapter(adapter);
                    Log.d("OVLIKO IH IMA", "" + orderedList.size());
                    setScreenMessage();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void setScreenMessage() {
        if (orderedList.isEmpty()) {
            firstBuy.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            setFont(firstBuy);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            firstBuy.setVisibility(View.INVISIBLE);
        }
    }

    private void setFont(TextView textView) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Banks&MilesSingleLine.ttf");
        textView.setTypeface(typeface);
    }
}
