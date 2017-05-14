package urmc.drinkingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity hosting the ExpandedProfileFragment
 * The user key needs to be passed when this activity is launch
 * Consequently the key is passed to the fragment that is hosted by this activity
 */
public class ExpandedProfileActivity extends AppCompatActivity {

    private Bundle mUserKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_profile);

        //set up arguments to pass the email of the user being pressed
        //Intent intent = getIntent();
        //Bundle extras = intent.getExtras();



        // Get post key from intent
        mUserKey = getIntent().getExtras();
        if (mUserKey == null) {
            throw new IllegalArgumentException("Must pass USER_KEY");
        }

        ExpandedProfileFragment mExpandedProfileFragment = new ExpandedProfileFragment();
        mExpandedProfileFragment.setArguments(mUserKey);

        //sets the ExpandedProfile Fragment
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.activity_expanded_profile, mExpandedProfileFragment)
                .commit();
    }
}
