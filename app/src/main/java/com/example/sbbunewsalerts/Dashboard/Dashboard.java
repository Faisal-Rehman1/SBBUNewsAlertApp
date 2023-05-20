package com.example.sbbunewsalerts.Dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_AudioNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_ImageNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_VideoNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Image_NewsModel;
import com.example.sbbunewsalerts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Permission;


public class Dashboard extends AppCompatActivity {
    Button  btnCreateNews,btn_GallryIMG;
    TextView tv_totalUsers, tv_totalNews ,tv_totalAudioNews,tv_totalVideoNews;
    Image_NewsModel newsModel;
    FirebaseDatabase database;
    DatabaseReference ref1,ref2,ref3,ref4;
    CardView btn_ViewUser;
    String[] PERMISSIONS;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        PERMISSIONS = new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if (!hasPermission(Dashboard.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(Dashboard.this, PERMISSIONS, 1);
        }

        btn_ViewUser = findViewById(R.id.btn_ViewUser);
        btnCreateNews = findViewById(R.id.btn_Create_News);
        tv_totalUsers = findViewById(R.id.tv_totalUsers);
        tv_totalAudioNews = findViewById(R.id.tv_totalAudioPost);
        tv_totalVideoNews = findViewById(R.id.tv_totalVideoPost);
        tv_totalNews = findViewById(R.id.tv_totalNewPost);
        btn_GallryIMG = findViewById(R.id.btn_GalleryImg);
        newsModel = new Image_NewsModel();



        database = FirebaseDatabase.getInstance();
        ref1 = database.getReference().child("Users");
        ref2 = database.getReference().child(new Fragment_ImageNews().ImageDB_refernece);
        ref3 = database.getReference().child(new Fragment_AudioNews().AudioDb_Refernce);
        ref4 = database.getReference().child(new Fragment_VideoNews().VideoDb_Refernce);

        // getting total users and total news Posted
        if (savedInstanceState == null) {
            counter(ref1, tv_totalUsers, " ");
            counter(ref2, tv_totalNews," ");
            counter(ref3, tv_totalAudioNews," ");
            counter(ref4, tv_totalVideoNews," ");
        }

        tv_totalNews = findViewById(R.id.tv_totalNewPost);
        btn_ViewUser.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Users.class));
        });

        btnCreateNews.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Create_News.class));
        });

        btn_GallryIMG.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Create_GalleryImage_Activity.class)));
    }

    private void counter(DatabaseReference ref, TextView tv, String s) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                tv.setText(s + String.valueOf(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);

    }

    //Checking Permissiona
    private boolean hasPermission(Dashboard c, String... PERMISSIONS) {
        if (c != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(c, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

}