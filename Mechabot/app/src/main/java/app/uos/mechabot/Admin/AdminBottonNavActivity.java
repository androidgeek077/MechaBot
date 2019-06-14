package app.uos.mechabot.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import app.uos.mechabot.Fragments.AboutUsFragment;
import app.uos.mechabot.HomeActivity;
import app.uos.mechabot.MainActivity;
import app.uos.mechabot.R;

public class AdminBottonNavActivity extends AppCompatActivity {
    private TextView mTextMessage;
    FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentLoadinManagerNoBackStack(new ViewUsersFragment());
                    return true;
                case R.id.navigation_dashboard:
                    FragmentLoadinManagerNoBackStack(new ViewMechanicFragment());
//                   ViewMechanicFragment
                    return true;
                case R.id.navigation_notifications:
                    FragmentLoadinManagerNoBackStack(new AboutUsFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_botton_nav);

        mAuth = FirebaseAuth.getInstance();
        FragmentLoadinManagerNoBackStack(new ViewUsersFragment());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(AdminBottonNavActivity.this, MainActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_about_us) {
            FragmentLoadinManagerNoBackStack(new AboutUsFragment());
        }

        return super.onOptionsItemSelected(item);
    }



    public void FragmentLoadinManagerNoBackStack(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
}
