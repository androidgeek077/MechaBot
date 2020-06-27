package app.uos.mechabot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import app.uos.mechabot.Admin.AdminBottonNavActivity;
import app.uos.mechabot.Driver.DriverMapActivity;

public class MainActivity extends AppCompatActivity  {


    String UserType;

    KProgressHUD progressDialog;


    DatabaseReference registerReference;
    FirebaseAuth mAuth;


    TextView mSignupTxt;
    EditText edLoginUserEmail, edLoginPassword;
    Button btnLogin;
    private String email;
    private String password;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth= FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, AdminBottonNavActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        registerReference = FirebaseDatabase.getInstance().getReference("user");


        edLoginUserEmail = (EditText) findViewById(R.id.ed_login_useremail);
        edLoginPassword = (EditText) findViewById(R.id.ed_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edLoginUserEmail.getText().toString();
                password = edLoginPassword.getText().toString();

                if (email.isEmpty()) {
                    edLoginUserEmail.setError("Please Enter your Email First");
                } else if (password.isEmpty()) {
                    edLoginPassword.setError("Please enter password First");
                } else {

                    progressDialog= KProgressHUD.create(MainActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        getUserType();



                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mSignupTxt = findViewById(R.id.signup_txt_vw);
        mSignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


    }

    void getUserType() {
        registerReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    UserType = dataSnapshot.child("usertype").getValue().toString();
                Toast.makeText(MainActivity.this, UserType, Toast.LENGTH_SHORT).show();

                if (UserType.equals("driver")) {
                    startActivity(new Intent(MainActivity.this, DriverMapActivity.class));
                    finish();
                } else if (UserType.equals("admin")){
                    startActivity(new Intent(MainActivity.this, AdminBottonNavActivity.class));

                }
                else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
                finish();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
