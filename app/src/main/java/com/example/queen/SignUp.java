package com.example.queen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class SignUp extends AppCompatActivity {

    private TextInputLayout username;
    private TextInputLayout password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.usernametext_signup);
        password = findViewById(R.id.passwordtext_signup);
        auth = FirebaseAuth.getInstance();
        Button signUp = findViewById(R.id.button_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getEditText().toString().trim();
                String txt_password = password.getEditText().toString().trim();

                if(!(txt_username.equals("")&&txt_password.equals(""))) {
                    signUp_User(txt_username, txt_password);
                }else{
                    Toast.makeText(SignUp.this, "please fill all the information", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void signUp_User(String username, String password) {

        FirebaseUser user = auth.getCurrentUser();
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){


                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SignUp.this, "Verification Email Has been Sent. ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this , LoginScreen.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(SignUp.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (user.isEmailVerified()){
            // Go yo main Screen
            startActivity(new Intent(SignUp.this , MainActivity.class));
            finish();
        }
    }
}