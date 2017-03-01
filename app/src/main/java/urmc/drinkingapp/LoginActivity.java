package urmc.drinkingapp;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements LoginFragment.SignUpProcess, SignUpFragment.SignUpProcessCancel {

    private LoginFragment mLoginFragment;
    private SignUpFragment mSignUpFragment;
    int counter = 0;

    private TextView B;
    private TextView A;
    private TextView C1;
    private TextView C2;
    private TextView H;
    private TextView U;
    private TextView S;

    private Handler mHandler = new Handler();

    public void SignUpStarted(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 1;
        mSignUpFragment = new SignUpFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mSignUpFragment)
                .commit();
    }

    public void SignUpCancel(){
        FragmentManager fm = getSupportFragmentManager();
        counter = 0;
        mLoginFragment = new LoginFragment();
        fm.beginTransaction()
                .replace(R.id.frame_layout_login_activity,mLoginFragment)
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


        /* Animation for the Title */
/*
        mHandler.postDelayed(new Runnable() {
            public void run() {
                B = (TextView) findViewById(R.id.b);
                B.setVisibility(TextView.VISIBLE);
            }
        }, 500);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                A = (TextView) findViewById(R.id.a);
                A.setVisibility(TextView.VISIBLE);
            }
        }, 1000);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                C1 = (TextView) findViewById(R.id.c1);
                C1.setVisibility(TextView.VISIBLE);
            }
        }, 1500);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                C2 = (TextView) findViewById(R.id.c2);
                C2.setVisibility(TextView.VISIBLE);
            }
        }, 2000);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                H = (TextView) findViewById(R.id.h);
                H.setVisibility(TextView.VISIBLE);
            }
        }, 2500);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                U = (TextView) findViewById(R.id.u);
                U.setVisibility(TextView.VISIBLE);
            }
        }, 3000);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                S = (TextView) findViewById(R.id.s);
                S.setVisibility(TextView.VISIBLE);
            }
        }, 3500);
*/
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
