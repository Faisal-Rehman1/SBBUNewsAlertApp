package com.example.sbbunewsalerts.Create_News_Fragments;


import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sbbunewsalerts.Dashboard.Dashboard;
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
import java.util.Locale;

public class Fragment_ImageNews extends Fragment {

    public String ImageDB_refernece = "News_Images";
    Button btn_postNews;
    EditText etNewsTitle, etNewsDescription;
    ImageView newsImg;
    Bitmap selectedImage;
    private Uri ImageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    int Image_Request_Code = 7;
    private MyClass myClass = new MyClass();

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.createe_image_news_fragment, null);

        btn_postNews = root.findViewById(R.id.Image_postNews);
        etNewsTitle = root.findViewById(R.id.Image_newsTitle);
        progressBar = root.findViewById(R.id.ImageProgressBar);
        etNewsDescription = root.findViewById(R.id.Image_newsDescription);
        newsImg = root.findViewById(R.id.Image_newsImg);

        storageReference = FirebaseStorage.getInstance().getReference(ImageDB_refernece);
        databaseReference = FirebaseDatabase.getInstance().getReference(ImageDB_refernece);

        btn_postNews.setOnClickListener(view -> {
            uploadFile(selectedImage);
        });

        newsImg.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Image_Request_Code);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                ImageUri = data.getData();
                final InputStream imageStream = requireActivity().getContentResolver().openInputStream(ImageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);

                newsImg.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void uploadFile(Bitmap bitmap) {

        progressBar.setVisibility(View.VISIBLE);

        StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + myClass.getfileExt(ImageUri, getContext()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = reference.putBytes(data);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                        }

                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.VISIBLE);

                            Uri downloadUri = task.getResult();


                            String newsTitle = etNewsTitle.getText().toString();
                            String newsDescription = etNewsDescription.getText().toString();
                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                            if (TextUtils.isEmpty(newsTitle)) {
                                Toast.makeText(getContext(), "Please Enter News Title", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else if (TextUtils.isEmpty(newsDescription)) {
                                Toast.makeText(getContext(), "Please Enter News Description", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Image_NewsModel member = new Image_NewsModel(
                                        newsTitle,
                                        newsDescription,
                                        currentDate,
                                        currentTime,
                                        downloadUri.toString()
                                );
                                String upload = databaseReference.push().getKey();
                                databaseReference.child(upload).setValue(member);


                                //sending message
                                myClass.sendSms(newsTitle, newsDescription, getContext(), "Text/Image News");
                                progressBar.setVisibility(View.GONE);

                                startActivity(new Intent(getContext(), Dashboard.class));
                            }

                            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "wentWrong" + "downloadUri failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}