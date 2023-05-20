package com.example.sbbunewsalerts.Gallery;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

public class GalleryAdapter extends FirebaseRecyclerAdapter<GalleryModel, GalleryAdapter.myViewHelper> {


    public GalleryAdapter(@NonNull FirebaseRecyclerOptions<GalleryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHelper holder, int position, @NonNull GalleryModel model) {

      holder.tvImgTtitle.setText(model.imgTitle);
      holder.date.setText(model.currentDate);

        Glide.with(holder.img.getContext())
                .load(model.getimageURLS())
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .error(R.drawable.ic_news_error)
                .into(holder.img);

        holder.tvImgTtitle.setOnClickListener(view -> {
            AlertDialog.Builder bu = new AlertDialog.Builder(holder.tvImgTtitle.getContext());
            bu.setTitle("Are you sure?");
            bu.setMessage("Deleted Data Can't be Undo");

            bu.setPositiveButton("Delete", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child("Gallery").child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            bu.setNegativeButton("Cancel", (dialogInterface, i) -> {

            });
            bu.show();
        });
    }



    @NonNull
    @Override

    public myViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_gallery_image,parent,false); //return view
        return new myViewHelper(view);
    }


    class myViewHelper extends RecyclerView.ViewHolder{
        ImageView img;
        TextView tvImgTtitle;
        TextView date,Gallery_ImageDelete;
        public myViewHelper(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.Gallery_Image);
            tvImgTtitle=itemView.findViewById(R.id.Gallery_ImageTItle);
            date=itemView.findViewById(R.id.Gallery_ImageDate);
            Gallery_ImageDelete=itemView.findViewById(R.id.Gallery_ImageDelete);
        }
    }
}
