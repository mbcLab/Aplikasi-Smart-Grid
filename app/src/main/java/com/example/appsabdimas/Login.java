package com.example.appsabdimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Login extends AppCompatActivity {

    EditText Email, Password;
    Button LoginButton;
    TextView CreateButton, forgotTextLink;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email              = findViewById(R.id.email);
        Password           = findViewById(R.id.password);
        progressBar        = findViewById(R.id.progressBar);
        firebaseAuth       = FirebaseAuth.getInstance();
        LoginButton        = findViewById(R.id.loginButton);
        CreateButton       = findViewById(R.id.createText);
        forgotTextLink     = findViewById(R.id.forgotPassword);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

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

                //authenticate user

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        } else {
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registrasi.class));
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {

                final EditText resetEmail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your Mail to Received Reset Link.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link

                        String mail = resetEmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}