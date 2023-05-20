package com.example.sbbunewsalerts.Gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class GallerFragment extends Fragment {
    //variables
    RecyclerView recyclerView;
    GalleryAdapter mainAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.view_gallery_images_fragment, null);
        recyclerView = root.findViewById(R.id.recV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<GalleryModel> options =
                new FirebaseRecyclerOptions.Builder<GalleryModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Gallery"), GalleryModel.class)
                        .build();

        mainAdapter = new GalleryAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        mainAdapter.startListening();
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}