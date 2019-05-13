package app.uos.mechabot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.uos.mechabot.Fragments.FindMechanicFragment;
import app.uos.mechabot.Fragments.HistoryFragment;
import app.uos.mechabot.Fragments.UserHomeFragment;

public class HomeActivity extends AppCompatActivity {


    ConstraintLayout MainFragmentContainer;


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        MainFragmentContainer = findViewById(R.id.main_fragment_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        BottomNavigationView MainBottomNavigationView;
        MainBottomNavigationView = findViewById(R.id.bottom_navigationer);


        MainBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:

                        loadFragWithoutBackStack(new UserHomeFragment());
                        break;
                    case R.id.nav_track_bus:
                        loadFragWithBackStack(new FindMechanicFragment());
                        break;
                    case R.id.nav_bus_routes:
                        loadFragWithBackStack(new HistoryFragment());
                        break;

                }
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

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
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_about_us) {
//            loadFragWithoutBackStack(new AboutUsFragment());
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadFragWithoutBackStack(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fragment_container, fragment);
        mFragmentTransaction.commit();
    }

    public void loadFragWithBackStack(Fragment fragment) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fragment_container, fragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


}
