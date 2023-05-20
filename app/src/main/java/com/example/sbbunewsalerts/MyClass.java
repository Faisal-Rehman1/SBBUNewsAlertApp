package com.example.sbbunewsalerts;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.telephony.SmsManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyClass {

    public MyClass() {
        getPhoneNumber();
    }

    public List<String> numerList = new ArrayList<>();

    public void sendSms(String newsTitle, String newsDesc, Context context, String category) {
        getPhoneNumber();
        //        sending message
        for (String s : numerList) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(s, null, " SBBU NEWS ALERTS \n"+category+"\n\n" + " Title : " + newsTitle + "\n Description : " + newsDesc + "\n\n Visit Official : https://www.sbbusba.edu.pk/", null, null);
                Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "Some fields is Empty", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getPhoneNumber() {
        final FirebaseDatabase databasee;
        DatabaseReference reff;
        databasee = FirebaseDatabase.getInstance();
        reff = databasee.getReference().child("Users");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                numerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String phone = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                    numerList.add(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public String getfileExt(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}