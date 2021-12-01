package com.project.us4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

public class RestaurantView extends AppCompatActivity {
    LinearLayout homeLnl,settingsLnl, profileLnl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);
        initViews();
        profileLnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantView.this, UserProfile.class);
                startActivity(intent);
            }
        });
        settingsLnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent= new Intent(RestaurantView.this, Settings.class);
                //startActivity(intent);
            }
        });
        homeLnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(RestaurantView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        homeLnl=findViewById(R.id.home1_layout);
        settingsLnl=findViewById(R.id.settings1_layout);
        profileLnl=findViewById(R.id.profile1_layout);
    }
}