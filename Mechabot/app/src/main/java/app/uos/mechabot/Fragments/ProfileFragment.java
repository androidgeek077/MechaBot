package app.uos.mechabot.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import app.uos.mechabot.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Dialog dialog;


    private TextView mNameTV,mPhoneTV, mEmailTV;
    CircleImageView mProfilePic;
    ImageView mBgPic;
    private String Username, UserPhone, UserEmail, UserImgUrl;
    ArrayList mLocationList, mLongList;
    Button mEditProfile;
    EditText mnameET, mPhoneET, mEmailET;
    // 03002578829


    DatabaseReference UserRef;
    DatabaseReference databaseReference;


    MapView mMapView;
    private GoogleMap googleMap;
    private LatLng mylocation;
    private ImageView UserImage;
    FirebaseAuth auth;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");

        UserRef= FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        mNameTV=view.findViewById(R.id.nameTV);
        mPhoneTV=view.findViewById(R.id.contactTV);
        mEmailTV=view.findViewById(R.id.emailTV);
        mBgPic=view.findViewById(R.id.bg_img);



        mProfilePic=view.findViewById(R.id.profilepic);
        mEditProfile=view.findViewById(R.id.edit_profile);
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentLoadinManagerNoBackStack(new EditProfileFragment());
            }
        });
        getStudentInfo();
        return view;
    }



    private void getStudentInfo() {
//        mLocationList = new ArrayList<>();
//        mLongList = new ArrayList<>();
        UserRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Username = dataSnapshot.child("name").getValue().toString();
                UserPhone = dataSnapshot.child("phone").getValue().toString();
                UserEmail = dataSnapshot.child("email").getValue().toString();
                UserImgUrl = dataSnapshot.child("imageUrl").getValue().toString();

                mNameTV.setText(Username);
                mPhoneTV.setText(UserPhone);
                mEmailTV.setText(UserEmail);
                Glide.with(getContext())
                        .load( UserImgUrl)
                        .into(mProfilePic);
                Glide.with(getContext())
                        .load( UserImgUrl)
                        .into(mBgPic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void FragmentLoadinManagerNoBackStack(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }


}
