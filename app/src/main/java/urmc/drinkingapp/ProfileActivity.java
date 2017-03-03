package urmc.drinkingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frame_layout_profile_activity,new ProfileFragment())
                .commit();

    }
}
