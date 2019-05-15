package app.uos.mechabot.Driver;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.uos.mechabot.Models.MechanicModel;
import app.uos.mechabot.R;


public class SignupActivity extends AppCompatActivity {


    DatabaseReference AddDriverRef;
    EditText driverNameEdtTxt, driverEmailEdtTxt, driverPasswordEdtTxt, driverEducationEdtTxt, driverPhoneEditTxt;
    private Button btnAddDriver;

    String name, driverEmail, driverPassword, driverEdu, driverPhone;
    FirebaseAuth mAuth;

    MechanicModel mechanicModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Mechanic");

        setContentView(R.layout.activity_signup);

        AddDriverRef = FirebaseDatabase.getInstance().getReference("Mechanic");
        mAuth = FirebaseAuth.getInstance();

        driverNameEdtTxt = findViewById(R.id.ed_signup_name);
        driverEmailEdtTxt = findViewById(R.id.driver_email_edt_txt);
        driverPasswordEdtTxt = findViewById(R.id.ed_driver_password);
        driverEducationEdtTxt = findViewById(R.id.edt_txt_education);
        driverPhoneEditTxt = findViewById(R.id.edt_txt_phone);

        btnAddDriver = findViewById(R.id.btn_add_driver);
        btnAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                name = driverNameEdtTxt.getText().toString();

                driverEmail = driverEmailEdtTxt.getText().toString();
                driverPassword = driverPasswordEdtTxt.getText().toString();
                driverEdu = driverPasswordEdtTxt.getText().toString();
                driverPhone = driverPasswordEdtTxt.getText().toString();


                if (name.isEmpty()) {
                    driverNameEdtTxt.setError("Please Enter Name First");
                } else if (driverEmail.isEmpty()) {
                    driverEmailEdtTxt.setError("please fill Email");
                } else if (driverPassword.length() < 8 && driverPassword.isEmpty()) {
                    driverPasswordEdtTxt.setError("please fill Password");
                } else if (driverEdu.isEmpty()) {
                    driverEducationEdtTxt.setError("please fill ");
                } else if (driverPhone.length() < 11 && driverPhone.isEmpty()) {
                    driverPhoneEditTxt.setError("please properly fill phone");
                } else {

                    mechanicModel = new MechanicModel(name, driverEmail, driverPassword, driverEdu, driverPhone);


                    mAuth.createUserWithEmailAndPassword(driverEmail, driverPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        AddDriverRef.child(mAuth.getCurrentUser().getUid()).setValue(mechanicModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar snackbar = Snackbar
                                                                    .make(v, "Driver added successfully", Snackbar.LENGTH_INDEFINITE)
                                                                    .setAction("OK", new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            startActivity(new Intent(SignupActivity.this, MechanicLoginActivity.class));
                                                                            finish();
                                                                        }
                                                                    });

                                                            snackbar.show();
                                                        }
                                                    }
                                                });
                                        mAuth.signOut();
                                    } else {
                                        Snackbar snackbar = Snackbar
                                                .make(v, "Some Error occured", Snackbar.LENGTH_INDEFINITE)
                                                .setAction("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
//                                                        startActivity(new Intent(SignupActivity.this, DriverActivity.class));
                                                        finish();
                                                    }
                                                });
                                        View v = snackbar.getView();
                                        v.setBackgroundColor(Color.RED);

                                        snackbar.show();
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


//                   AddDriverRef.push().setValue(MechanicModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//
//                            Snackbar snackbar = Snackbar
//                                    .make(v, "Driver added successfully", Snackbar.LENGTH_INDEFINITE)
//                                    .setAction("OK", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            startActivity(new Intent(SignupActivity.this, AdminHomeActivity.class));
//                                            finish();
//                                        }
//                                    });
//
//                            snackbar.show();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });


                }

            }
        });


    }
}
