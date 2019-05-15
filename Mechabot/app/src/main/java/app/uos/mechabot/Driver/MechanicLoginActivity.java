package app.uos.mechabot.Driver;

import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.kaopiz.kprogresshud.KProgressHUD;

import app.uos.mechabot.HomeActivity;
import app.uos.mechabot.R;
import app.uos.mechabot.Driver.SignupActivity;

public class MechanicLoginActivity extends AppCompatActivity  {



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
            startActivity(new Intent(MechanicLoginActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_login);
        mAuth = FirebaseAuth.getInstance();
        registerReference = FirebaseDatabase.getInstance().getReference("Drivers");


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

                    progressDialog= KProgressHUD.create(MechanicLoginActivity.this)
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

                                        startActivity(new Intent(MechanicLoginActivity.this, DriverMapActivity.class));
                                        finish();
                                        progressDialog.dismiss();


                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MechanicLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(MechanicLoginActivity.this, SignupActivity.class));
            }
        });


//    public void logtheUserIn() {
//        incomingEmail = edtTxtEmail.getText().toString();
//        incomingPass = edtTxtPassword.getText().toString();
//        if (hasActiveInternetConnection()) {
//            if (incomingEmail.isEmpty() || incomingPass.isEmpty() || incomingPass.length() < 6) {
//                if (incomingEmail.isEmpty() || incomingPass.isEmpty()) {
//                    edtTxtEmail.setError("please fill all fields");
//                    edtTxtPassword.setError("please fill all fields");
//                } else if (incomingPass.length() < 6) {
//                    edtTxtPassword.setError("password should be atleast 6 digit long");
//                }
//            } else {
//                ConnectToServer.getSingletonInstance(getApplicationContext()).addRequestToTheRequstQeue(logTheUserInRequest(incomingEmail,incomingPass));
//            }
//
//        } else {
//            Snackbar snackbar;
//            View v = findViewById(R.id.btn_login);
//            snackbar = Snackbar.make(v, "No Internet", Snackbar.LENGTH_LONG);
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundColor(ContextCompat.getColor(MechanicLoginActivity.this, R.color.FailuerAlert));
//            snackbar.show();
//
//        }
//    }


//    ------------------INTERNET CONNECTIVITY METHODS------------------------- //

//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null;
//    }

//    public boolean hasActiveInternetConnection() {
//        if (isNetworkAvailable()) {
//
//            Thread thread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    try {
//                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//                        urlc.setRequestProperty("User-Agent", "Test");
//                        urlc.setRequestProperty("Connection", "close");
//                        urlc.setConnectTimeout(1500);
//                        urlc.connect();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            thread.start();
//            return (true);
//        } else {
//            Log.d("internet Checker", "No network available!");
//            return false;
//        }
//
//    }
        //    ------------------INTERNET CONNECTIVITY METHODS------------------------- //


        //    stringRequestMaker for basic loginURl
    }
}
