package com.example.sbbunewsalerts.Login_Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sbbunewsalerts.Dashboard.Dashboard;
import com.example.sbbunewsalerts.HomeScreen.HomeScreen;
import com.example.sbbunewsalerts.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    private EditText UserName, UserPhone, UserDept;
    private Button SaveInformationbuttion;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;
    Bitmap selectedImage;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        UserName =  findViewById(R.id.setup_UserName);
        UserPhone =  findViewById(R.id.setup_UserPhone);
        UserDept =  findViewById(R.id.setup_UserDept);
        SaveInformationbuttion = findViewById(R.id.setup_saveBtn);

        ProfileImage =findViewById(R.id.setup_userPrifile);
        loadingBar = new ProgressDialog(this);


        SaveInformationbuttion.setOnClickListener(view -> SaveAccountSetupInformation());

        ProfileImage.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Gallery_Pick);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                ProfileImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }


    }


    private void uploadFile(Bitmap bitmap) {

        StorageReference ImagesRef = UserProfileImageRef.child(currentUserID + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = ImagesRef.putBytes(data);

        uploadTask.addOnFailureListener(exception -> Log.i("whatTheFuck:", exception.toString())).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    Log.i("problem", Objects.requireNonNull(task.getException()).toString());
                }

                return ImagesRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i("seeThisUri", downloadUri.toString());// This is the one you should store
                    UsersRef.child("imageUrl").setValue(downloadUri.toString());
                } else {
                    Log.i("wentWrong", "downloadUri failure");
                }
            });
        });

    }





//
//         photoRef.putFile(selectedImageUri)
//            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//        @Override
//        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//            // Download file From Firebase Storage
//            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri downloadPhotoUrl) {
//                    //Now play with downloadPhotoUrl
//                    //Store data into Firebase Realtime Database
//                    FriendlyMessage friendlyMessage = new FriendlyMessage
//                            (null, mUsername, downloadPhotoUrl.toString());
//                    mDatabaseReference.push().setValue(friendlyMessage);
//                }
//            });
//        }
//    });










    private void SaveAccountSetupInformation() {
        String username = UserName.getText().toString();
        String userPhone = UserPhone.getText().toString();
        String userDept = UserDept.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userEmail = user.getEmail();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userPhone)) {
            Toast.makeText(this, "Please write your phone Number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userDept)) {
            Toast.makeText(this, "Please write your Department...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are Saving Infromation...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("fullname", username);
            userMap.put("phone", userPhone);
            userMap.put("department", userDept);
            userMap.put("email", userEmail);

            UsersRef.updateChildren(userMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    SendUserToMainActivity();
                    uploadFile(selectedImage);
                    loadingBar.dismiss();
                } else {
                    String message = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            });
        }
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this, HomeScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
