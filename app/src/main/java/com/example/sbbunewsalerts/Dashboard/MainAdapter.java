package com.example.sbbunewsalerts.Dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter <MainModel,MainAdapter.myViewHelper>{


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHelper holder, @SuppressLint("RecyclerView") final int position, @NonNull MainModel model) {
        holder.Name.setText(model.fullname);
        holder.Phone.setText(model.phone);
        holder.Email.setText(model.email);
        holder.Dept.setText(model.department);

        Glide.with(holder.img.getContext())
                .load(model.getImageUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);



        holder.Name.setOnClickListener(view -> {
            AlertDialog.Builder bu = new AlertDialog.Builder(holder.Name.getContext());
            bu.setTitle("Are you sure?");
            bu.setMessage("Deleted Data Can't be Undo");

            bu.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    FirebaseDatabase.getInstance().getReference().child("Users").child(getRef(position).getKey()).removeValue();
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            });
        bu.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        bu.show();
        });
    }

    @NonNull
    @Override

    public myViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sigle_item_users,parent,false); //return view


        return new myViewHelper(view);
    }

    class myViewHelper extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView Name,Phone,Email,Dept;

        public myViewHelper(@NonNull View itemView) {
            super(itemView);


            Name= itemView.findViewById(R.id.textName);
            Phone= itemView.findViewById(R.id.txtPhone);
            Email= itemView.findViewById(R.id.textEmail);
            Dept= itemView.findViewById(R.id.txtDept);

            img=(CircleImageView) itemView.findViewById(R.id.mainItem_circleImg);

        }
    }
}
