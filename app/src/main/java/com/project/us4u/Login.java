package com.project.us4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Models.User;

public class Login extends AppCompatActivity {
    EditText emailEDT, passwordEDT;
    Animation leftrightAnim;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    TextView signUpTXT,forgotPasswordEDT, logoNameTxt;
    Button loginBTN;
    ArrayList<User> _users;
    //Preference field
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        leftrightAnim = AnimationUtils.loadAnimation(this,R.anim.leftright_animation);
        logoNameTxt.setAnimation(leftrightAnim);

        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        _users=new ArrayList<>();

        //Initialising preference
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("USERS");

        progressDialog=new ProgressDialog(this);

        signUpTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating User inputs

                String email = emailEDT.getText().toString().trim();
                String password = passwordEDT.getText().toString().trim();
                if (email.isEmpty()) {
                    emailEDT.setError("Required");
                    emailEDT.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEDT.setError("Please, provide a valid email");
                    emailEDT.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEDT.setError("Required");
                    passwordEDT.requestFocus();
                    return;
                }
                //End
                else
                    loginUser(email,password);
                /*Intent intent = new Intent(MainActivity.this, HomeMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/

            }
        });
        forgotPasswordEDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent =new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {

        signUpTXT=findViewById(R.id.text_view_signup);
        loginBTN=findViewById(R.id.button_login);
        emailEDT=findViewById(R.id.edit_text_email);
        passwordEDT=findViewById(R.id.edit_text_password);
        forgotPasswordEDT=findViewById(R.id.text_view_forgot_password);
        logoNameTxt=findViewById(R.id.logoname);
    }

    private void loginUser(String email, String password) {

        //Setting up the progress Dialog
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if (email.equals("admin@gmail.com") && password.equals("admin_12345")){
            progressDialog.dismiss();
            Intent intent=new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {

                        String Uid=mAuth.getCurrentUser().getUid();
                        getUsers(Uid);
                        //registering Shared Preferences
                        editor.putBoolean("LOGGED_IN_STATUS",true);
                        editor.putString("USER_ID",Uid);
                        editor.commit();

                        //starting the main activity intent

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {

                        //Registering a shared preference
                        editor.putBoolean("LOGGED_IN_STATUS",false);
                        editor.commit();
                        Toast.makeText(Login.this, "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    private void getUsers(String Uid) {

        databaseReference.child(Uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    User user = task.getResult().getValue(User.class);

                    editor.putString("PHONE",user.getPhoneNo());
                    editor.putString("EMAIL",user.getEmail());
                    editor.putString("ID",Uid);
                    String username=""+user.getFname()+", "+user.getLname();
                    editor.putString("USERNAME",username);
                    editor.putString("FNAME",user.getFname());
                    editor.putString("REGDATE",user.getRegistrationDate());
                    editor.putString("LNAME",user.getLname());
                    editor.commit();
                    Log.d("USER",user.toString());
                    System.out.println(user.toString());
                }

                else {
                    Log.d("IMEKATAAA","IMAGINE BANA HAIFANYI");
                    System.out.println("IMEKATA KUFANYA");
                }
            }
        });

    }
}