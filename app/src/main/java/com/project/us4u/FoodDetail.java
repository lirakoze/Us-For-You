package com.project.us4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import Helpers.Constants;
import Helpers.DatabaseHelper;

public class FoodDetail extends AppCompatActivity {
    ImageView imageView;
    TextView titleTXT, descriptionTXT,priceTXT, statusTXT;
    Integer amount=1;
    Button addTocartBtn;
    ImageButton backBtn;
    ImageButton favBtn;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        imageView=findViewById(R.id.imageView_food);
        titleTXT=findViewById(R.id.textView_food_name);
        descriptionTXT=findViewById(R.id.textView_description);
        priceTXT=findViewById(R.id.text_view_food_price);
        backBtn=findViewById(R.id.button_back);
        favBtn=findViewById(R.id.button_favorite);
        addTocartBtn=findViewById(R.id.button_add_to_cart);
        statusTXT=findViewById(R.id.food_status);

        dbHelper=new DatabaseHelper(getApplicationContext());

        Bundle bundle=getIntent().getExtras();
        amount=bundle.getInt(Constants.FOOD_PRICE,1);
        titleTXT.setText(bundle.getString(Constants.FOOD_NAME));
        descriptionTXT.setText(bundle.getString(Constants.FOOD_DESCRIPTION));
        priceTXT.setText(""+amount+"");
        Glide.with(this).load(bundle.getString(Constants.FOOD_IMAGE_URL)).into(imageView);
        String status=bundle.getString(Constants.FOOD_STATUS);
        if (status.equalsIgnoreCase("Available")){
            statusTXT.setText(status);
        }
        else{
            statusTXT.setText(status);
            statusTXT.setTextColor(Color.parseColor("#D51915"));
            addTocartBtn.setEnabled(false);
        }

        String id = bundle.getString(Constants.FOOD_ID);
        String name = bundle.getString(Constants.FOOD_NAME);
        String url = bundle.getString(Constants.FOOD_IMAGE_URL);
        System.out.println(amount);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodDetail.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addTocartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the item is already in cart
                Cursor res= dbHelper.getCartItem(id);
                System.out.println(res.toString());
                if (res.getCount()==0){
                    boolean isAddedToCart= dbHelper.insertCartItem(id,name,url,"1",amount.toString(),amount.toString());
                    if (isAddedToCart){
                        String message="Item Has Been Added To Cart";
                        Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                    }
                    else {
                        String message="Failed To Add Item To Cart";
                        Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    String message="Item Is Already Added";
                    Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the item is already in cart
                Cursor res= dbHelper.getFavItem(id);
                System.out.println(res.toString());
                if (res.getCount()==0){
                    boolean isAddedToCart= dbHelper.insertFavItem(id,name,url,"1",amount.toString(), amount.toString());
                    if (isAddedToCart){
                        String message="Item Has Been Added To Favorites";
                        Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                    }
                    else {
                        String message="Failed To Add Item To Favorite";
                        Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    String message="Item Is Already On Favorite List";
                    Toast.makeText(FoodDetail.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}