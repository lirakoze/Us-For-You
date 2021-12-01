package com.project.us4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ImageButton backBtn;

    EditText emailEDT; Button resetPasswordBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailEDT=findViewById(R.id.edit_text_email_reset);
        resetPasswordBTN=findViewById(R.id.button_reset_password);
        backBtn=findViewById(R.id.backBtn);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ResetPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        resetPasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEDT.getText().toString();
                if(email.isEmpty()) {
                    emailEDT.setError(" Email is Required");
                    return;
                }
                else{
                    emailEDT.setText("");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassword.this,"A link to "+email+" for password reset",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String errorMsg="There is no account registered under that email. Input a valid email";
                                Toast.makeText(ResetPassword.this,errorMsg,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}