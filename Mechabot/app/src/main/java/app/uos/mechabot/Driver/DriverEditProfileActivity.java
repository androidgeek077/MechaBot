package app.uos.mechabot.Driver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import app.uos.mechabot.Models.MechanicModel;
import app.uos.mechabot.R;

public class DriverEditProfileActivity extends AppCompatActivity {


    EditText mEdtTxtASName, mEdtTxtASEmail, mEdtTxtASPhone, mPassword;

    String mStrASName, mStrASEmail, mStrASPhone;

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    StorageReference profilePicRef;
    MechanicModel mechanicModel;
    Button updateBtn, mSelectBtn;


    LinearLayout linearLayout;
    View view;
    Button mBtnSignup;


    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;

    private StorageReference mProfilePicStorageReference;
    private static final int RC_PHOTO_PICKER = 1;
    private Uri selectedProfileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_profile);


        auth = FirebaseAuth.getInstance();
        mProfilePicStorageReference = FirebaseStorage.getInstance().getReference().child("ProfilePictures");
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        mEdtTxtASName = findViewById(R.id.edt_txt_as_name);
        mEdtTxtASEmail =findViewById(R.id.edt_txt_as_email);
        mEdtTxtASPhone =findViewById(R.id.edt_txt_as_phone);

        profileImageView = findViewById(R.id.selected_img_vw);

        mSelectBtn = findViewById(R.id.select_iamge_btn);
        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePicture();
            }
        });
        updateBtn = findViewById(R.id.btn_a_signup);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrASName = mEdtTxtASName.getText().toString();
                mStrASEmail = mEdtTxtASEmail.getText().toString();
                mStrASPhone = mEdtTxtASPhone.getText().toString();
                uploadProduct();


//                    profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
//                    profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    downloadUri = uri.toString();
//                                    uploadProduct(downloadUri);
//                                }
//                            });
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });

            }
        });


    }

    public void getProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            profileImageView.setImageURI(selectedImageUri);
            profileImageView.setVisibility(View.VISIBLE);
        }

    }

    public void uploadProduct() {

        mechanicModel = new MechanicModel(mStrASName, mStrASEmail, mStrASPhone);
        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(mechanicModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mEdtTxtASName.getText().clear();
                mEdtTxtASEmail.getText().clear();
                mEdtTxtASPhone.getText().clear();
                profileImageView.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
