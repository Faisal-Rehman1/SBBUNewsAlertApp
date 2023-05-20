package com.example.sbbunewsalerts.Create_News_Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.sbbunewsalerts.Dashboard.Dashboard;
import com.example.sbbunewsalerts.MyClass;
import com.example.sbbunewsalerts.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragment_VideoNews extends Fragment {

    public String VideoDb_Refernce = "News_Videos";
    private AppCompatButton uploadbtnNews;
    private VideoView videoView;
    //    private Uri videoUri;
    MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private EditText videoname;
    private ProgressBar progressBar;
    private MyClass myClass = new MyClass();
    ProgressDialog progressDialog;


    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.createe_video_news_fragment, null);


        videoView = root.findViewById(R.id.Video_newsVideo);
        progressBar = root.findViewById(R.id.videoProgressBar);
        videoname = root.findViewById(R.id.Video_newsTitle);
        uploadbtnNews = root.findViewById(R.id.Video_postNews);

        storageReference = FirebaseStorage.getInstance().getReference(VideoDb_Refernce);
        databaseReference = FirebaseDatabase.getInstance().getReference(VideoDb_Refernce);

        videoView.setMediaController(mediaController);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.start();
        videoView.setOnClickListener(view -> ChooseVideo());

        uploadbtnNews.setOnClickListener(view -> {
            Uploadvideo();
        });


        return root;
    }

    private void ChooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 5);
    }

    Uri videoUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            videoUri = data.getData();

            videoView.setVideoURI(videoUri);

        } else {
            Toast.makeText(getContext(), "You haven't picked Video", Toast.LENGTH_LONG).show();
        }
    }

    private void Uploadvideo() {
        progressBar.setVisibility(View.VISIBLE);
        String VideoTitle = videoname.getText().toString().trim();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(VideoTitle)) {
            Toast.makeText(getContext(), "Please write your News Title...", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

        } else if (videoUri != null) {
            progressBar.setVisibility(View.VISIBLE);

            StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + myClass.getfileExt(videoUri, getContext()));
            reference.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            // get the link of video
                            String downloadUri = uriTask.getResult().toString();
                            Video_NewsModel member = new Video_NewsModel(
                                    VideoTitle,
                                    downloadUri,
                                    time, date);
                            String upload = databaseReference.push().getKey();
                            databaseReference.child(upload).setValue(member);

                            myClass.sendSms(VideoTitle, "", getContext(), "Video News");

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Video Uploaded!!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getContext(), Dashboard.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No Video Selected", Toast.LENGTH_SHORT).show();
        }
    }
}