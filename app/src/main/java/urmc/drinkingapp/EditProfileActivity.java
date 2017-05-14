package urmc.drinkingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity hosting the OnlineEditProfile Fragment
 */
public class EditProfileActivity extends AppCompatActivity {

    //instance of the fragment
    //private EditProfileFragment mEditProfileFragment;
    private OnlineEditProfileFragment mEditProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //gets extras coming from the profile activity - user's email - and sets it to the fragment
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //mEditProfileFragment = new EditProfileFragment();
        mEditProfileFragment = new OnlineEditProfileFragment();
        mEditProfileFragment.setArguments(extras);
        //onSavedInstanceState standard procedure
        if (savedInstanceState!=null){
            //mEditProfileFragment = (EditProfileFragment) getSupportFragmentManager().getFragment(savedInstanceState,"FRAGMENT");
            mEditProfileFragment = (OnlineEditProfileFragment) getSupportFragmentManager().getFragment(savedInstanceState,"FRAGMENT");
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frame_layout_edit_profile_activity, mEditProfileFragment)
                .commit();
    }

    //onSavedInstanceState storing the fragment
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "FRAGMENT", mEditProfileFragment);
    }
}
