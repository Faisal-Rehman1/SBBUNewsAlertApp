package com.example.sbbunewsalerts.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sbbunewsalerts.Dashboard.Dashboard;
import com.example.sbbunewsalerts.Login_Registration.Login_Activity;
import com.example.sbbunewsalerts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    //variables
    TextView tvName, tvPhone, tvGmail, tvDept;
    CircleImageView imgProfile;
    Button btnDashboard, btnLgout;

    //firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String currentUserID;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, null);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid(); //getting current user id
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users"); //here Users is  table name


        //refernces // finding id's
        tvName = root.findViewById(R.id.userName);
        tvPhone = root.findViewById(R.id.userPhone);
        tvGmail = root.findViewById(R.id.userGmail);
        tvDept = root.findViewById(R.id.userDept);
        imgProfile = root.findViewById(R.id.Circle_profileImge);
        btnDashboard = root.findViewById(R.id.btnDashboard);
        btnLgout = root.findViewById(R.id.btnLogout);

        //hiding the dashboard button
        btnDashboard.setVisibility(View.GONE);

        //getting current user to satisfy admins
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("phone")) {
                        String phone = dataSnapshot.child("phone").getValue().toString();

                        if (phone.equals("03073135966")) {
                            Toast.makeText(getContext(), "Welcome MR " + dataSnapshot.child("fullname").getValue().toString(), Toast.LENGTH_SHORT).show();
                            btnDashboard.setVisibility(View.VISIBLE);
                            tvName.setText(dataSnapshot.child("fullname").getValue().toString());
                            tvPhone.setText(dataSnapshot.child("phone").getValue().toString());
                            tvGmail.setText(dataSnapshot.child("email").getValue().toString());
                            tvDept.setText(dataSnapshot.child("department").getValue().toString());
                        }
                    }
                    if (dataSnapshot.hasChild("fullname")) {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        tvName.setText(fullname);
                    }
                    if (dataSnapshot.hasChild("imageUrl")) {
                        String image = dataSnapshot.child("imageUrl").getValue().toString();
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile).into(imgProfile);
                    }
                    if (dataSnapshot.hasChild("phone")) {
                        tvPhone.setText(dataSnapshot.child("phone").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("email")) {
                        tvGmail.setText(dataSnapshot.child("email").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("department")) {
                        tvDept.setText(dataSnapshot.child("department").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        btnDashboard.setOnClickListener(view -> startActivity(new Intent(getActivity(), Dashboard.class)));

        btnLgout.setOnClickListener(view -> {
            mAuth.signOut();
            SendUserToLoginActivity();
        });
        return root;
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), Login_Activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }
}