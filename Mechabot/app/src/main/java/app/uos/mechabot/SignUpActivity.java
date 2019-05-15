package app.uos.mechabot;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import app.uos.mechabot.Models.UserModel;


public class SignUpActivity extends AppCompatActivity {

    KProgressHUD progressDialog;

    DatabaseReference registerStudent;
    FirebaseAuth mAuth;
    EditText edName,edEmail,edPassword, edLat, edLong;
    private Button btnSignUp;

    Double StdLatDouble=0.0;
    Double StdLongDouble=0.0;

    String name,email, password, latitude, longitude;

    UserModel Model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerStudent= FirebaseDatabase.getInstance().getReference("user");
        mAuth=FirebaseAuth.getInstance();

        edName= findViewById(R.id.ed_signup_name);
        edEmail=findViewById(R.id.edt_txt_email);
        edPassword=findViewById(R.id.ed_signup_password);

        edLat=findViewById(R.id.ed_lat);
        edLong= findViewById(R.id.ed_long);


        btnSignUp=findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                name = edName.getText().toString();
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                latitude = edLat.getText().toString();
                longitude = edLong.getText().toString();

                StdLatDouble = Double.parseDouble(latitude);
                StdLongDouble = Double.parseDouble(longitude);


                if (name.isEmpty()) {
                    edName.setError("Please Enter Name First");
                } else if (email.isEmpty()) {
                    edEmail.setError("please fill email");
                } else if (password.isEmpty()) {
                    edPassword.setError("Please enter Password");
                } else if (password.length() < 8) {
                    edPassword.setError("Password must contain at least 8 characters ");
                } else if (latitude.isEmpty()) {
                    edPassword.setError("Please enter latitude");
                } else if (longitude.isEmpty()) {
                    edPassword.setError("Please enter longitude");
                } else {

                    progressDialog= KProgressHUD.create(SignUpActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();


//
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Model = new UserModel(mAuth.getCurrentUser().getUid(), email, name, password, StdLatDouble, StdLongDouble);

                                        registerStudent.child(mAuth.getCurrentUser().getUid()).setValue(Model)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar snackbar = Snackbar
                                                                    .make(v, "User added successfully", Snackbar.LENGTH_INDEFINITE)
                                                                    .setAction("OK", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                                        }
                                                                    });

                                                            snackbar.show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                        mAuth.signOut();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                }

            }
        });





    }


}
