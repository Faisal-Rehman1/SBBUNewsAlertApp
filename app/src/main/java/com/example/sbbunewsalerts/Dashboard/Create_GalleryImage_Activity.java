package com.example.sbbunewsalerts.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.sbbunewsalerts.MyClass;
import com.example.sbbunewsalerts.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Create_GalleryImage_Activity extends AppCompatActivity {

    String Storage_Path = "Gallery/";
    public static final String Database_Path = "Gallery";
    Bitmap selectedImage;
    ImageView SelectImage;
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    Button btnUploadGalleryImg;
    AppCompatEditText ImageName;
    private MyClass myClass = new MyClass();


    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_gallery_image_activity);

        btnUploadGalleryImg = findViewById(R.id.Gallery_saveBtn);
        ImageName = findViewById(R.id.edit_imageTitle);
        SelectImage = findViewById(R.id.Gallery_uploadIMG);

        progressDialog = new ProgressDialog(getApplicationContext());
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        // Adding click listener to Choose image button.
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Image_Request_Code);
            }
        });


        // Adding click listener to Upload image button.
        btnUploadGalleryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                uploadFile(selectedImage);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                SelectImage.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadFile(Bitmap bitmap) {

        StorageReference ImagesRef = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + myClass.getfileExt(imageUri, getApplicationContext()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = ImagesRef.putBytes(data);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("whatTheFuck:", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.i("problem", task.getException().toString());
                        }

                        return ImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String TempImageName = ImageName.getText().toString().trim();
                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            HashMap userMap = new HashMap();
                            userMap.put("imgTitle", TempImageName);
                            userMap.put("currentDate", currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                            userMap.put("imageURLS", downloadUri.toString());

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(userMap);

                            Toast.makeText(Create_GalleryImage_Activity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Create_GalleryImage_Activity.this, "wentWrong" + "downloadUri failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}