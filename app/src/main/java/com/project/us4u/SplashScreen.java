package com.project.us4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView usforyouTxt, sloganTxt;

    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initViews();
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        image.setAnimation(topAnim);
        usforyouTxt.setAnimation(bottomAnim);
        sloganTxt.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
                boolean status= sharedPreferences.getBoolean("LOGGED_IN_STATUS",false);

                if (status==true){
                    Intent intent= new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent= new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);
    }
    private void initViews(){
        image=findViewById(R.id.imageView_delivery);
        usforyouTxt=findViewById(R.id.textView_usforyou);
        sloganTxt=findViewById(R.id.textView_slogan);
    }
}