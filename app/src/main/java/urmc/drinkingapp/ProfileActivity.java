package urmc.drinkingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Activity hosting the OnlineProfileFragment and the OnlineEditProfileFragment
 */
public class ProfileActivity extends AppCompatActivity implements OnlineProfileFragment.EditProfileProcess, OnlineEditProfileFragment.EditProfileFinished {

    private OnlineProfileFragment mProfileFragment;
    private OnlineEditProfileFragment mEditProfileFragment;
    int counter = 0;


    public void EditProfileStarted(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 1;
        mEditProfileFragment = new OnlineEditProfileFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_profile_activity,mEditProfileFragment)
                .commit();
    }

    public void EditProfileOK(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 0;
        mProfileFragment = new OnlineProfileFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_profile_activity,mProfileFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frame_layout_profile_activity,new OnlineProfileFragment())
                .commit();

        /*
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frame_layout_profile_activity,new ProfileFragment())
                .commit();
*/
    }
}
