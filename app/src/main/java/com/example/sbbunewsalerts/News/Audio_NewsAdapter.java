package com.example.sbbunewsalerts.News;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sbbunewsalerts.Create_News_Fragments.Audio_NewsModel;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_AudioNews;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Audio_NewsAdapter extends FirebaseRecyclerAdapter<Audio_NewsModel, Audio_NewsAdapter.myViewHelper> {


    public Audio_NewsAdapter(@NonNull FirebaseRecyclerOptions<Audio_NewsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHelper holder, int position, @NonNull Audio_NewsModel model) {
        holder.Audio_newsTitle.setText(model.getAudioTitle());
        holder.createMediaPlayer(model.getAudioUri(), holder.Audio_newsTitle.getContext(), model.getAudioTitle());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());

        //seekbar
        holder.seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (holder.mediaPlayer != null) {
                    int millis = holder.mediaPlayer.getCurrentPosition();
                    long total_secs = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
                    long mins = TimeUnit.MINUTES.convert(total_secs, TimeUnit.SECONDS);
                    long secs = total_secs - (mins * 60);
                    holder.Audio_newsDuration.setText(mins + ":" + secs + " / " + holder.duration);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (holder.mediaPlayer != null) {
                    holder.mediaPlayer.seekTo(holder.seekbar1.getProgress());
                }
            }
        });


        //deleting
        holder.Audio_newsTitle.setOnClickListener(view -> {
            AlertDialog.Builder bu = new AlertDialog.Builder(holder.Audio_newsTitle.getContext());
            bu.setTitle("Are you sure?");
            bu.setMessage("Deleted Data Can't be Undo");

            bu.setPositiveButton("Delete", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child(new Fragment_AudioNews().AudioDb_Refernce).child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            bu.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            bu.show();
        });


//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.ButtonTestPlayPause) {
//                /**
//                 * ImageButton onClick event handler. Method which start/pause
//                 * mediaplayer playing
//                 */
//                try {
//                    mediaPlayer.setDataSource(audioName); // setup
//                    // song
//                    // from
//                    // http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3
//                    // URL
//                    // to
//                    // mediaplayer
//                    // data
//                    // source
//                    mediaPlayer.prepare(); // you must call this method after setup
//                    // the datasource in setDataSource
//                    // method. After calling prepare() the
//                    // instance of MediaPlayer starts load
//                    // data from URL to internal buffer.
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets
//                // the
//                // song
//                // length
//                // in
//                // milliseconds
//                // from
//                // URL
//
//                if (!mediaPlayer.isPlaying()) {
//                    mediaPlayer.start();
//                    buttonPlayPause.setImageResource(R.drawable.pause_button);
//
//                } else {
//                    mediaPlayer.pause();
//                    buttonPlayPause.setImageResource(R.drawable.play_button);
//                }
//                primarySeekBarProgressUpdater();
//            }
//        }
        //play and pause
        holder.btnPlay.setOnClickListener(view -> {
            holder.etc();
        });
    }


    @NonNull
    @Override

    public myViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_item_audio_news, parent, false); //return view
        return new myViewHelper(view);
    }

    static class myViewHelper extends RecyclerView.ViewHolder {
        TextView Audio_newsTitle, Audio_newsDuration, time, date;
        SeekBar seekbar1;
        String duration;
        ImageButton btnPlay;
        ProgressBar progressBar;


        MediaPlayer mediaPlayer;
        ScheduledExecutorService timer;

        String currentUserID;

        public myViewHelper(@NonNull View itemView) {
            super(itemView);


            Audio_newsTitle = (TextView) itemView.findViewById(R.id.ViewAudioNews_tv_title);
            Audio_newsDuration = (TextView) itemView.findViewById(R.id.ViewAudioNews_tv_duration);
            time = (TextView) itemView.findViewById(R.id.ViewAudioNew_time);
            date = (TextView) itemView.findViewById(R.id.ViewAudioNew_date);
            seekbar1 = (SeekBar) itemView.findViewById(R.id.ViewAudioNews_seekbar1);
            btnPlay = (ImageButton) itemView.findViewById(R.id.ViewAudioNews_btn_play);

            //setting drawable ic delete
            Drawable img = Audio_newsTitle.getContext().getResources().getDrawable(R.drawable.ic_delete);

            Audio_newsTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            Audio_newsTitle.setEnabled(false);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

            usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("admin_gmail")) {
                            String phone = Objects.requireNonNull(dataSnapshot.child("admin_gmail").getValue()).toString();

                            if (phone.equals("sbbunewsalert@gmail.com")) {
                                Audio_newsTitle.setEnabled(true);
                                Audio_newsTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                            } else {
                                Audio_newsTitle.setEnabled(false);
                                Audio_newsTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        }




        private void etc() {


            Drawable ic_play = itemView.getResources().getDrawable(R.drawable.ic_play);
            Drawable ic_pause= itemView.getContext().getResources().getDrawable(R.drawable.ic_pause);

            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setBackground(ic_play);
                    timer.shutdown();
                } else {
                    mediaPlayer.start();
                    btnPlay.setBackground(ic_pause);

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


        private void releaseMediaPlayer() {
            if (timer != null) {
                timer.shutdown();
            }
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Audio_newsTitle.setText("TITLE");
            Audio_newsDuration.setText("00:00 / 00:00");
            seekbar1.setMax(100);
            seekbar1.setProgress(0);
        }

        private void createMediaPlayer(String url, Context context, String title) {

            Uri uri = Uri.parse(url);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();

                Audio_newsTitle.setText(title);

                int millis = mediaPlayer.getDuration();
                long total_secs = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
                long mins = TimeUnit.MINUTES.convert(total_secs, TimeUnit.SECONDS);
                long secs = total_secs - (mins * 60);
                duration = mins + ":" + secs;
                Audio_newsDuration.setText("00:00 / " + duration);
                seekbar1.setMax(millis);
                seekbar1.setProgress(0);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPlayer();
                    }
                });
            } catch (IOException e) {
                Audio_newsTitle.setText(e.toString());
            }
        }



    }


}
