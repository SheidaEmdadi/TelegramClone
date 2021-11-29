package com.example.telegramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


public class Login extends AppCompatActivity {
    private TextInputEditText edtUserName, edtPassword;
    private Button btnLogin, btnSignUp;
    private TextView txtForgotPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        FindViews();

        if (ParseUser.getCurrentUser() != null) {
            TransitionToMainActivity();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(Login.this);
                dialog.setMessage("Loading...");
                dialog.show();

                ParseUser.logInInBackground(edtUserName.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            Toast.makeText(Login.this, " welcome "+ user.get("username") , Toast.LENGTH_LONG).show();


                            TransitionToMainActivity();

                        } else {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Forgot.class);
                startActivity(intent);

            }
        });
    }

    private void FindViews() {

        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        txtForgotPass = findViewById(R.id.txtForgotPass);

    }

    private void TransitionToMainActivity() {

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}