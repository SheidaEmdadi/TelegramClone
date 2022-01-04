package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class Forgot extends AppCompatActivity {

    private TextInputEditText edtForgotPass;
    private Button btnForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        findViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarForgot);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.requestPasswordResetInBackground(edtForgotPass.getText().toString(),
                        new RequestPasswordResetCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(Forgot.this, " An email was successfully sent with reset instructions. ", Toast.LENGTH_LONG).show();

                                } else {

                                    Toast.makeText(Forgot.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                    // Something went wrong. Look at the ParseException to see what's up.
                                }
                            }
                        });

            }


        });


    }

    private void findViews() {

        edtForgotPass = findViewById(R.id.edtForgotPass);
        btnForgotPass = findViewById(R.id.btnForgotPass);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}