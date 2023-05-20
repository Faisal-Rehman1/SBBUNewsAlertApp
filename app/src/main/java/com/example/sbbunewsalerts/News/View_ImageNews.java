package com.example.sbbunewsalerts.News;

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

import com.example.sbbunewsalerts.Create_News_Fragments.Fragment_ImageNews;
import com.example.sbbunewsalerts.Create_News_Fragments.Image_NewsModel;
import com.example.sbbunewsalerts.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class View_ImageNews extends Fragment {


    RecyclerView recyclerView;
    Image_NewsAdapter mainAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        @SuppressLint("InflateParams") ViewGroup root = (ViewGroup) inflater.inflate(R.layout.view_image_news_fragment, null);

        recyclerView = root.findViewById(R.id.View_ImageNews_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Image_NewsModel> options =
                new FirebaseRecyclerOptions.Builder<Image_NewsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(new Fragment_ImageNews().ImageDB_refernece), Image_NewsModel.class)
                        .build();

        mainAdapter = new Image_NewsAdapter(options);
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
