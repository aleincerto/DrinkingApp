package urmc.drinkingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity implements LoginFragment.SignUpProcess {

    private LoginFragment mLoginFragment;
    private SignUpFragment mSignUpFragment;
    int counter = 0;

    public void SignUpStarted(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 1;
        mSignUpFragment = new SignUpFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mSignUpFragment)
                .commit();
    }

    public void signUpCancel(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 0;
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,new LoginFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fm =  getSupportFragmentManager();
        mLoginFragment = new LoginFragment();
        //get persisted fragment if it exists
        if (savedInstanceState!=null){
            mLoginFragment = (LoginFragment) getSupportFragmentManager().getFragment(savedInstanceState,"FRAGMENT");
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
