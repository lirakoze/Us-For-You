package com.project.us4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Models.User;
import Services.Utils;

public class UserProfile extends AppCompatActivity {
    EditText fnameEdt, lnameEdt, phoneEdt,emailEdt;
    String Uid="";
    ProgressDialog progressDialog;
    CardView card;
    TextView usernameEdt, logOutTxt;
    Button editBtn, updateBtn;
    Animation leftRightAnim, rightLeftAnim, bottomAnim;
    LinearLayout layout;

    private FirebaseFirestore firestore;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initViews();
        firestore=FirebaseFirestore.getInstance();
        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        progressDialog=new ProgressDialog(this);

        String fname=sharedPreferences.getString("FNAME","");
        String lname=sharedPreferences.getString("LNAME","");
        String email=sharedPreferences.getString("EMAIL","");
        String phone=sharedPreferences.getString("PHONE","");
        String username=sharedPreferences.getString("USERNAME","");
        String regDate=sharedPreferences.getString("REGDATE","");
        Uid=sharedPreferences.getString("ID","");
        System.out.println(fname+" "+lname);

        fnameEdt.setText(fname);
        lnameEdt.setText(lname);
        phoneEdt.setText(phone);
        emailEdt.setText(email);
        usernameEdt.setText(username);


        leftRightAnim= AnimationUtils.loadAnimation(this,R.anim.leftright_animation);
        rightLeftAnim= AnimationUtils.loadAnimation(this,R.anim.rightleft_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        usernameEdt.setAnimation(leftRightAnim);
        card.setAnimation(leftRightAnim);
        layout.setAnimation(bottomAnim);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        logOutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent intent= new Intent(UserProfile.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnameEdt.setEnabled(true);
                lnameEdt.setEnabled(true);
                phoneEdt.setEnabled(true);
                usernameEdt.setText(username);
                updateBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname=fnameEdt.getText().toString();
                String lname=lnameEdt.getText().toString();
                String phone= Utils.sanitizePhoneNumber(phoneEdt.getText().toString());

                if (fname.length()<3){
                    fnameEdt.setError("Too short");
                    return;
                }
                if (lname.length()<3){
                    lnameEdt.setError("Too short");
                    return;
                }
                if (phone.length()<10 || phone.length()>13){
                    phoneEdt.setError("Invalid Number");
                    return;
                }

                progressDialog.setMessage("Updating Account Info...");
                progressDialog.show();
                CollectionReference colRef=firestore.collection("US4U").document("USERS")
                        .collection(Uid);



               String id= colRef.getId();
               System.out.println("ID = "+id);
                User user=new User(fname,lname,phone,email,regDate);

                Task<Void> docRef=firestore.collection("US4U").document("USERS")
                        .collection(Uid).document(id).set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Toast.makeText(UserProfile.this,"User Info is Updated",Toast.LENGTH_SHORT).show();
                                    editor.putString("PHONE",phone);
                                    String username=""+fname+", "+lname;
                                    editor.putString("USERNAME",username);
                                    editor.putString("FNAME",fname);
                                    editor.putString("LNAME",lname);
                                    editor.commit();

                                    fnameEdt.setText(fname);
                                    lnameEdt.setText(lname);
                                    phoneEdt.setText(phone);
                                    usernameEdt.setText(username);
                                }
                                else
                                    System.out.println("Imekata ku Update");
                            }
                        });

                fnameEdt.setEnabled(false);
                lnameEdt.setEnabled(false);
                phoneEdt.setEnabled(false);
                editBtn.setVisibility(View.VISIBLE);
                updateBtn.setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {

        fnameEdt=findViewById(R.id.edit_text_prof_fname);
        lnameEdt=findViewById(R.id.edit_text_prof_lname);
        emailEdt=findViewById(R.id.edit_text_prof_email);
        phoneEdt=findViewById(R.id.edit_text_prof_phone);
        editBtn=findViewById(R.id.edit_button);
        updateBtn=findViewById(R.id.update_button);
        usernameEdt=findViewById(R.id.profile_username);
        layout=findViewById(R.id.linearProfile);
        card=findViewById(R.id.cardprofile);
        logOutTxt=findViewById(R.id.textView_logout);
    }
}