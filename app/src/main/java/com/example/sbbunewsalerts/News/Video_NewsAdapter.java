package com.example.sbbunewsalerts.News;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_VideoNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Video_NewsModel;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

public class Video_NewsAdapter extends FirebaseRecyclerAdapter<Video_NewsModel, Video_NewsAdapter.myViewHelper> {

    public Video_NewsAdapter(@NonNull FirebaseRecyclerOptions<Video_NewsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHelper holder, int position, @NonNull Video_NewsModel model) {
        holder.newTitle.setText(model.getVideoTitle());
//            holder.videoView(model.getVideoUri(),holder.videoView.getContext());
//            holder.videoView.setVideoURI(Uri.parse(model.getVideoUri()));



//
//        MediaController mediaController = new MediaController(holder.videoView.getContext());
//        mediaController.setAnchorView(holder.videoView);
//        holder.videoView.setMediaController(mediaController);
//        holder.videoView.setVideoURI(Uri.parse(model.getVideoUri()));
//        holder.videoView.requestFocus();
//        holder.videoView.pause();


        Uri uri = Uri.parse(model.getVideoUri());

        Toast.makeText(holder.videoView.getContext(), "video URL" + uri.toString(), Toast.LENGTH_SHORT).show();
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());

        MediaController mediaController = new MediaController(holder.videoView.getContext());
        holder.videoView.setVideoURI(uri);
        mediaController.setAnchorView(holder.videoView);
        mediaController.setMediaPlayer(holder.videoView);
        holder.videoView.setMediaController(mediaController);
//        holder.videoView.start();


        holder.newTitle.setOnClickListener(view -> {
            AlertDialog.Builder bu = new AlertDialog.Builder(holder.newTitle.getContext());
            bu.setTitle("Are you sure?");
            bu.setMessage("Deleted Data Can't be Undo");

            bu.setPositiveButton("Delete", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child(new Fragment_VideoNews().VideoDb_Refernce).child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            bu.setNegativeButton("Cancel", (dialogInterface, i) -> {
            });
            bu.show();
        });
    }


    @NonNull
    @Override

    public myViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_item_video_news, parent, false); //return view
        return new myViewHelper(view);
    }

    static class myViewHelper extends RecyclerView.ViewHolder {
        //        CircleImageView img;
        TextView newTitle, time, date;
        VideoView videoView;

        String currentUserID;

        public myViewHelper(@NonNull View itemView) {
            super(itemView);


            newTitle = itemView.findViewById(R.id.view_videoTitle);
            videoView = itemView.findViewById(R.id.view_videoView);
            time = itemView.findViewById(R.id.view_videoTime);
            date = itemView.findViewById(R.id.view_videoDate);

            //setting drawable ic delete
            @SuppressLint("UseCompatLoadingForDrawables") Drawable img = newTitle.getContext().getResources().getDrawable(R.drawable.ic_delete);

            newTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            newTitle.setEnabled(false);


            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

            usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("phone")) {
                            String phone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();

                            if (phone.equals("03073135966")) {
                                newTitle.setEnabled(true);
                                newTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                            } else {
                                newTitle.setEnabled(false);
                                newTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        }

        public void videoView(String videoUrl, Context context) {

            Uri uri = Uri.parse(videoUrl);

            videoView.setVideoURI(uri);

            MediaController mediaController = new MediaController(context);

            mediaController.setAnchorView(videoView);

            videoView.setMediaController(mediaController);

            videoView.start();

        }
    }
}
