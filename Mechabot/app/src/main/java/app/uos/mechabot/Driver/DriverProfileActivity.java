package app.uos.mechabot.Driver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.uos.mechabot.Fragments.EditProfileFragment;
import app.uos.mechabot.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DriverProfileActivity extends AppCompatActivity {


    private TextView mNameTV,mPhoneTV, mEmailTV, mMechincEdu;
    CircleImageView mProfilePic;
    ImageView mBgPic;
    private String Username, UserPhone, UserEmail, UserImgUrl, MechanicEducation;
    ArrayList mLocationList, mLongList;
    Button mEditProfile;
    EditText mnameET, mPhoneET, mEmailET;
    // 03002578829


    DatabaseReference UserRef;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        UserRef= FirebaseDatabase.getInstance().getReference().child("Mechanic");
        databaseReference = FirebaseDatabase.getInstance().getReference("Mechanic");

        mNameTV=findViewById(R.id.nameTV);
        mPhoneTV=findViewById(R.id.contactTV);
        mEmailTV=findViewById(R.id.emailTV);
        mBgPic=findViewById(R.id.bg_img);
        mMechincEdu=findViewById(R.id.educationTV);



        mProfilePic=findViewById(R.id.profilepic);
        mEditProfile=findViewById(R.id.edit_profile);


        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DriverProfileActivity.this, DriverEditProfileActivity.class));
//                FragmentLoadinManagerNoBackStack(new EditProfileFragment());
            }
        });
        getStudentInfo();
    }

    private void getStudentInfo() {
        mLocationList = new ArrayList<>();
        mLongList = new ArrayList<>();
        UserRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Username = dataSnapshot.child("name").getValue().toString();
                UserPhone = dataSnapshot.child("phone").getValue().toString();
                UserEmail = dataSnapshot.child("email").getValue().toString();
                MechanicEducation = dataSnapshot.child("education").getValue().toString();
                UserImgUrl = dataSnapshot.child("imageUrl").getValue().toString();

                mNameTV.setText(Username);
                mPhoneTV.setText(UserPhone);
                mEmailTV.setText(UserEmail);
                mMechincEdu.setText(MechanicEducation);
                Glide.with(DriverProfileActivity.this)
                        .load( UserImgUrl)
                        .into(mProfilePic);
                Glide.with(DriverProfileActivity.this)
                        .load( UserImgUrl)
                        .into(mBgPic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
