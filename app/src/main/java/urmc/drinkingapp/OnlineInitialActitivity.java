package urmc.drinkingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;

public class OnlineInitialActitivity extends AppCompatActivity implements OnlineSignInFragment.SignUpProcess, OnlineSignUpFragment.SignUpProcessCancel {

    private OnlineSignInFragment mLoginFragment;
    private OnlineSignUpFragment mSignUpFragment;
    int counter = 0;


    public void SignUpStarted(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 1;
        mSignUpFragment = new OnlineSignUpFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mSignUpFragment)
                .commit();
    }

    public void SignUpCancel(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 0;
        mLoginFragment = new OnlineSignInFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mLoginFragment)
                .commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_initial_actitivity);

        FragmentManager fm =  getSupportFragmentManager();
        mLoginFragment = new OnlineSignInFragment();
        //get persisted fragment if it exists
        if (savedInstanceState!=null){
            mLoginFragment = (OnlineSignInFragment) getSupportFragmentManager().getFragment(savedInstanceState,"FRAGMENT");
        }
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mLoginFragment)
                .commit();

    }


    //persisting fragment
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (counter==0){
            getSupportFragmentManager().putFragment(outState,"FRAGMENT",mLoginFragment);
        }else{
            getSupportFragmentManager().putFragment(outState,"FRAGMENT",mSignUpFragment);
        }

    }
}
