package app.uos.mechabot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseUserActivity extends AppCompatActivity {
    Button btn_signup, btn_signup_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        btn_signup=findViewById(R.id.btn_signup);
        btn_signup_user=findViewById(R.id.btn_signup_user);
        btn_signup_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseUserActivity.this, SignUpActivity.class);
                intent.putExtra("usettype", "user");
                startActivity(intent);
            }
        });btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseUserActivity.this, SignUpActivity.class);
                intent.putExtra("usettype", "driver");
                startActivity(intent);
            }
        });
    }
}