package com.project.us4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Adapters.OrdersAdapter;
import Models.Order;
import Models.User;

public class ViewOrders extends AppCompatActivity {
RecyclerView rvOrders;
OrdersAdapter adapter;
ArrayList<Order> orders;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private LinearLayoutManager OrdersLinearLyout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        rvOrders=findViewById(R.id.rv_orders);
        orders=new ArrayList<>();

       OrdersLinearLyout = new LinearLayoutManager(ViewOrders.this,LinearLayoutManager.VERTICAL,false);
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("ORDERS");
        String uid=sharedPreferences.getString("ID","");
        databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Order order = task.getResult().getValue(Order.class);
                orders.add(order);
            }
        });

    }
}