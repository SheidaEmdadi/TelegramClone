package com.example.telegramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtProfileUserName, edtProfileBio;
    Button btnUpdateProfile;
    private ImageView imgProfile;
    Bitmap receivedImageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        edtProfileUserName = findViewById(R.id.edtProfileUserName);
        edtProfileBio = findViewById(R.id.edtProfileBio);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        imgProfile = findViewById(R.id.imgProfile);



        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(ProfileActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                } else {
                    getChosenImage();
                }
            }
        });

        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("username") == null) {
            edtProfileUserName.setText("");
        } else {
            edtProfileUserName.setText(parseUser.get("username").toString());
        }
        if (parseUser.get("profileBio") == null) {
            edtProfileBio.setText("");
        } else {
            edtProfileBio.setText(parseUser.get("profileBio").toString());
        }





        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edtProfileUserName.getText().toString().equals("")) {

                    } else {
                        parseUser.setUsername(edtProfileUserName.getText().toString());
                    }
                    parseUser.put("profileBio", edtProfileBio.getText().toString());

                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {


                            } else {
                                FancyToast.makeText(ProfileActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });

                    if (receivedImageBitmap != null) {

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            ParseFile parseFile = new ParseFile("pic.png", bytes);
                        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");

                        parseQuery.whereEqualTo("username", parseUser.getUsername());

                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (objects.size() > 0 && e == null) {

                                    for (ParseObject post : objects) {

                                        post.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
//                                                FancyToast.makeText(ProfileActivity.this,"Deleted !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                            }
                                        });


                                    }
                                }
                            }
                        });

                        ParseObject parseObject = new ParseObject("Photo");
                            parseObject.put("picture", parseFile);
                            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                            final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        FancyToast.makeText(ProfileActivity.this,"Done !",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                                    } else {
                                        FancyToast.makeText(ProfileActivity.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
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
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", parseUser.getUsername());

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {

                    for (ParseObject post : objects) {

                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (data != null && e == null) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageViewParams.setMargins(5, 5, 5, 5);
                                    imgProfile.setImageBitmap(bitmap);



                                }
                            }
                        });

                    }
                }else if(objects.size() == 0){
                    imgProfile.setImageResource(R.drawable.ic_user);
                }
                else {
                    FancyToast.makeText(ProfileActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }

                dialog.dismiss();
            }
        });


    }


    private void getChosenImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imgProfile.setImageBitmap(selectedImage);
                    }

                    break;
                case 2000:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = ProfileActivity.this.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                                imgProfile.setImageBitmap(receivedImageBitmap);
                            }
                        }

                    }
                    break;
            }
        }

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