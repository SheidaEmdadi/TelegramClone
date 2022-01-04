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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtProfileUserName, edtProfileBio;
    Button btnUpdateProfile;
    private ImageView imgProfile;
    Bitmap receivedImageBitmap;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;


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
//        btnImgProfile = findViewById(R.id.btnImgProfile);
        imgProfile = findViewById(R.id.imgProfile);

        ParseObject parseObject = new ParseObject("Photo");

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




//        btnImgProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (receivedImageBitmap != null) {
//
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                    byte[] bytes = byteArrayOutputStream.toByteArray();
//                    ParseFile parseFile = new ParseFile("pic.png", bytes);
//                    parseObject.put("picture", parseFile);
//                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
//                    final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
//                    dialog.setMessage("Loading...");
//                    dialog.show();
//                    parseObject.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                FancyToast.makeText(ProfileActivity.this, "Done !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//
//                            } else {
//                                FancyToast.makeText(ProfileActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//
//                } else {
//                    FancyToast.makeText(ProfileActivity.this, "you gotta select an image!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
//                }
//            }
//        });


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edtProfileUserName.getText().toString().equals("")) {

                    } else {
                        parseUser.setUsername(edtProfileUserName.getText().toString());
                    }
                    if (edtProfileBio.getText().toString().equals("")) {
                        parseUser.put("profileBio", "Hey! I'm using " + getString(R.string.app_name) + ".");
                    } else {
                        parseUser.put("profileBio", edtProfileBio.getText().toString());
                    }

                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(ProfileActivity.this, "profile updated!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();


                            } else {
                                FancyToast.makeText(ProfileActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
//                            dialog.dismiss();
                        }
                    });


//
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (receivedImageBitmap != null) {

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("pic.png", bytes);
                    parseObject.put("picture", parseFile);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
//                                FancyToast.makeText(ProfileActivity.this, "Done !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                            } else {
                                FancyToast.makeText(ProfileActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            dialog.dismiss();
                        }
                    });

                } else {
                    FancyToast.makeText(ProfileActivity.this, "you gotta select an image!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }

            }
        });


    }


    private void getChosenImage() {
//        Toast.makeText(getContext(), "now we can access the images", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
//        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_IMAGE);

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

//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
//            imageUri = data.getData();
//            imgProfile.setImageURI(imageUri);


//        if (requestCode == 2000) {
//            if (requestCode == Activity.RESULT_OK) {
//                try {
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
//                    imgShare.setImageBitmap(receivedImageBitmap);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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
//                                LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                imageViewParams.setMargins(1, 1, 1, 1);
//                                imgProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                                imgProfile.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.imgProfile, 100, 100));
                              //todo: it crashes when image size is large
                                imgProfile.setImageBitmap(receivedImageBitmap);
                            }
                        }

                    }
                    break;
            }
        }


//        private void selectImage (Context context){
//            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Choose your profile picture");
//
//            builder.setItems(options, new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int item) {
//
//                    if (options[item].equals("Take Photo")) {
//                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(takePicture, 0);
//
//                    } else if (options[item].equals("Choose from Gallery")) {
//                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(pickPhoto, 1);
//
//                    } else if (options[item].equals("Cancel")) {
//                        dialog.dismiss();
//                    }
//                }
//            });
//            builder.show();
//    }
    }

//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }

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