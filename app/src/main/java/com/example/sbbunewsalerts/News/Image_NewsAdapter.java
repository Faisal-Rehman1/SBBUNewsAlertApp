package com.example.sbbunewsalerts.News;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_ImageNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Image_NewsModel;
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

public class Image_NewsAdapter extends FirebaseRecyclerAdapter<Image_NewsModel, Image_NewsAdapter.myViewHelper> {

    public Image_NewsAdapter(@NonNull FirebaseRecyclerOptions<Image_NewsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHelper holder, @SuppressLint("RecyclerView") final int position, @NonNull Image_NewsModel newsModel) {
        holder.newTitle.setText(newsModel.getNewsTitle());
        holder.newsDesription.setText(newsModel.getNewsDescription());
        holder.time.setText(newsModel.getTime());
        holder.date.setText(newsModel.getdate());

        Glide.with(holder.img.getContext())
                .load(newsModel.getimageUri())
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .error(R.drawable.ic_news_error)
                .into(holder.img);

        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder bu = new AlertDialog.Builder(holder.newTitle.getContext());
            bu.setTitle("Are you sure?");
            bu.setMessage("Deleted Data Can't be Undo");

            bu.setPositiveButton("Delete", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child(new Fragment_ImageNews().ImageDB_refernece).child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            bu.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            bu.show();
        });
    }

    @NonNull
    @Override

    public myViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_item_image_news, parent, false); //return view
        return new myViewHelper(view);
    }

    static class myViewHelper extends RecyclerView.ViewHolder {
        //        CircleImageView img;
        TextView newTitle, newsDesription, time, date, btnDelete;
        ImageView img;

        String currentUserID;

        public myViewHelper(@NonNull View itemView) {
            super(itemView);

            newTitle =  itemView.findViewById(R.id.txttnewsTitle);
            newsDesription =  itemView.findViewById(R.id.txttDescription);
            time =  itemView.findViewById(R.id.txttTime);
            date =  itemView.findViewById(R.id.texttDate);

            img = itemView.findViewById(R.id.newssImg);

            btnDelete =  itemView.findViewById(R.id.btn__delete);
            btnDelete.setVisibility(View.GONE);

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
                                btnDelete.setVisibility(View.VISIBLE);
                            } else {
                                btnDelete.setVisibility(View.GONE);
                            }
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

        }
    }
}
