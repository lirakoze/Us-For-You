package com.project.us4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Adapters.CartAdapter;
import Helpers.DatabaseHelper;
import Models.CartItem;

public class CartView extends AppCompatActivity{
    private FirebaseFirestore firestore;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button checkOutBTN;
    ImageButton backBtn;
    DatabaseHelper _dbHelper;
    RecyclerView rv_cartItems;
    TextView totalTXT;
    Integer total=0;
    String UserId;
    private LinearLayoutManager cartItemsLayoutManager;
    private ArrayList<CartItem> _cartItems;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        backBtn=findViewById(R.id.backBtn);
        checkOutBTN=findViewById(R.id.button_check_out);
        _cartItems=new ArrayList<>();
        _dbHelper=new DatabaseHelper(this);
        rv_cartItems=findViewById(R.id.rv_cart_Items);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        checkOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CartView.this, CheckOut.class);
                startActivity(intent);
            }
        });

        Cursor res=_dbHelper.getCartItems();
        _cartItems.clear();
        if (res.getCount()==0){
            Toast.makeText(this,"No Cart Items",Toast.LENGTH_SHORT).show();
            checkOutBTN.setEnabled(false);
        }
        while(res.moveToNext()) {
            _cartItems.add(new CartItem(res.getString(0),res.getString(1),res.getString(2),res.getString(3), res.getString(4), res.getString(5)));

        }
        for (CartItem item:_cartItems) {
            System.out.println(item);
        }

        //Setting the Recycler View Adapter
        cartItemsLayoutManager=new LinearLayoutManager(this);
        adapter=new CartAdapter(_cartItems, getApplicationContext());
        rv_cartItems.setLayoutManager(cartItemsLayoutManager);
        rv_cartItems.setAdapter(adapter);
        //End
    }

    @Override
    protected void onResume() {
        super.onResume();
        total=0;
        _cartItems.clear();
        Cursor res=_dbHelper.getCartItems();
        if (res.getCount()==0){
            return;
            //Toast.makeText(this,"No Cart Items",Toast.LENGTH_SHORT).show();
        }
        while(res.moveToNext()) {
            _cartItems.add(new CartItem(res.getString(0),res.getString(1),res.getString(2),res.getString(3), res.getString(4), res.getString(5)));

        }


        adapter=new CartAdapter(_cartItems, getApplicationContext());
        rv_cartItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}