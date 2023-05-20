package com.example.sbbunewsalerts.Create_News_Fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Fragment_AudioNews extends Fragment {

    public String AudioDb_Refernce = "News_Audio";
    Button btn_postNews, btn_audioPlayPause;
    EditText etNewsTitle;
    ImageView newsImg;
    TextView tv_duration, tv_title;
    private Uri AudioUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    MyClass myClass = new MyClass();

    SeekBar seekbar1;

    String duration;
    MediaPlayer mediaPlayer;
    ScheduledExecutorService timer;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.createe_audio_news_fragment, null);

        progressBar = root.findViewById(R.id.AudioProgressBar);
        etNewsTitle = root.findViewById(R.id.Audio_newsTitle);
        btn_postNews = root.findViewById(R.id.Audio_postNews);
        newsImg = root.findViewById(R.id.Audio_newsImg);
        btn_audioPlayPause = root.findViewById(R.id.Audio_PlayPauseBtn);
        seekbar1 = root.findViewById(R.id.seekbar1);
        tv_title = root.findViewById(R.id.tv_title);
        tv_duration = root.findViewById(R.id.tv_duration);


        storageReference = FirebaseStorage.getInstance().getReference(AudioDb_Refernce);
        databaseReference = FirebaseDatabase.getInstance().getReference(AudioDb_Refernce);

        newsImg.setOnClickListener(view -> ChooseAudio());

        btn_postNews.setOnClickListener(view -> UploadAudio());

        btn_audioPlayPause.setOnClickListener(view -> etc());

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (mediaPlayer != null) {
                    int millis = mediaPlayer.getCurrentPosition();
                    long total_secs = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
                    long mins = TimeUnit.MINUTES.convert(total_secs, TimeUnit.SECONDS);
                    long secs = total_secs - (mins * 60);
                    tv_duration.setText(mins + ":" + secs + " / " + duration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekbar1.getProgress());
                }
            }
        });

        btn_audioPlayPause.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                AudioUri = data.getData();
                createMediaPlayer(AudioUri);
            }
        }
    }

    private void createMediaPlayer(Uri uri) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getContext(), uri);
            mediaPlayer.prepare();

            tv_title.setText(getNameFromUri(uri));
            btn_audioPlayPause.setVisibility(View.VISIBLE);

            int millis = mediaPlayer.getDuration();
            long total_secs = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
            long mins = TimeUnit.MINUTES.convert(total_secs, TimeUnit.SECONDS);
            long secs = total_secs - (mins * 60);
            duration = mins + ":" + secs;
            tv_duration.setText("00:00 / " + duration);
            seekbar1.setMax(millis);
            seekbar1.setProgress(0);

            mediaPlayer.setOnCompletionListener(mp -> releaseMediaPlayer());
        } catch (IOException e) {
            tv_title.setText(e.toString());
        }
    }

    private void releaseMediaPlayer() {
        if (timer != null) {
            timer.shutdown();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        btn_audioPlayPause.setVisibility(View.GONE);
        tv_title.setText("TITLE");
        tv_duration.setText("00:00 / 00:00");
        seekbar1.setMax(100);
        seekbar1.setProgress(0);
    }

    @SuppressLint("Range")
    public String getNameFromUri(Uri uri) {
        String fileName = "";
        Cursor cursor = null;
        cursor = getActivity().getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void etc() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btn_audioPlayPause.setText("PLAY");
                timer.shutdown();
            } else {
                mediaPlayer.start();
                btn_audioPlayPause.setText("PAUSE");

                timer = Executors.newScheduledThreadPool(1);
                timer.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            if (!seekbar1.isPressed()) {
                                seekbar1.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        }
                    }
                }, 10, 10, TimeUnit.MILLISECONDS);
            }
        }
    }

    private void ChooseAudio() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }

    private void UploadAudio() {
        progressBar.setVisibility(View.VISIBLE);
        String AudioTitle = etNewsTitle.getText().toString().trim();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        if (TextUtils.isEmpty(AudioTitle)) {
            Toast.makeText(getContext(), "Please write your News Title...", Toast.LENGTH_SHORT).show();
        } else if (AudioUri != null) {
            StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + myClass.getfileExt(AudioUri, getContext()));
            reference.putFile(AudioUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            // get the link of video
                            String downloadUri = uriTask.getResult().toString();

                            Audio_NewsModel member = new Audio_NewsModel(
                                    AudioTitle,
                                    downloadUri,
                                    time, date);

                            String upload = databaseReference.push().getKey();
                            databaseReference.child(upload).setValue(member);


                            Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();

                            myClass.sendSms(AudioTitle, "", getContext(), "Audio News");

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Video Uploaded!!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getContext(), Dashboard.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    });
        } else {
            Toast.makeText(getActivity(), "No Audio Selected", Toast.LENGTH_SHORT).show();
        }
    }
}