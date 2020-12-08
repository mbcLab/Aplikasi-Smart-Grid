package com.example.appsabdimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrasi extends AppCompatActivity {

    EditText FullName, Email, Password, Phone;
    Button RegisterButton;
    TextView LoginText;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        FullName           = findViewById(R.id.fullName);
        Email              = findViewById(R.id.Email);
        Password           = findViewById(R.id.password);
        Phone              = findViewById(R.id.phone);
        RegisterButton     = findViewById(R.id.RegisterButton);
        LoginText          = findViewById(R.id.createText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                final String email    = Email.getText().toString().trim();
                final String password = Password.getText().toString().trim();
                final String fullName = FullName.getText().toString();
                final String phone    = Phone.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Email.setError("Email is Required!");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Password.setError("Password is Required!");
                    return;
                }

                if(password.length() < 6) {
                    Password.setError("Password must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sent verification link
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registrasi.this, "Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Tag", "onFailure: Email not Sent!"+e.getMessage());
                                }
                            });

                            Toast.makeText(Registrasi.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String, Object > user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email", email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(Registrasi.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    };
                });
                LoginText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                });
            }
        });
    }
}