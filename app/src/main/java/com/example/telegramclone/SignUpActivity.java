package com.example.telegramclone;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView imgProfile;
    private TextInputEditText edtEmailSignUp, edtPasswordSignUp, edtUserNameSignUp;
    Button btnSignUp;

    boolean imageControl = false;


    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                imageChooser();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ParseUser user = new ParseUser();
                    if (edtUserNameSignUp.getText().toString().equals("") || edtPasswordSignUp.getText().toString().equals("") ||
                            edtEmailSignUp.getText().toString().equals("")) {
                        Toast.makeText(SignUpActivity.this, "please fill the boxes", Toast.LENGTH_LONG).show();

                    } else {
                        user.setUsername(edtUserNameSignUp.getText().toString());
                        user.setPassword(edtPasswordSignUp.getText().toString());
                        user.setEmail(edtEmailSignUp.getText().toString());

                        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                        dialog.setMessage("Loading...");
                        dialog.show();

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
//                                    Toast.makeText(SignUpActivity.this, "Welcome " + user.get("username") + "! \n you Signed up successfully. ", Toast.LENGTH_LONG).show();

//                                TransitionToMainActivity();
                                    showAlert("Account created successfully!", "please verify your email.", false);
                                } else {
                                    ParseUser.logOut();
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void findViews() {

        imgProfile = findViewById(R.id.imgProfileSignUp);
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtUserNameSignUp = findViewById(R.id.edtUserNameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

    }


//    public void imageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//
//    }

//    private void TransitionToMainActivity() {
//
//        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }

    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    // don't forget to change the line below with the names of your Activities
                    if (!error) {
                        Intent intent = new Intent(SignUpActivity.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}