package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * Fragment to sign in into the app. Uses online authentication against the firebase database
 */
public class OnlineSignInFragment extends Fragment {

    public interface SignUpProcess{
        void SignUpStarted();
    }

    private SignUpProcess mListener;

    //widgets
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    //private Button mSignInButton;
    private FancyButton mSignInButton;
    //private Button mSignUpButton;
    private FancyButton mSignUpButton;

    private FancyButton mTwitterSignIn;
    private FancyButton mGoogleSignIn;
    private FancyButton mFacebookSignIn;


    //email and password for the user logging in
    private String mLoginEmail;
    private String mLoginPassword;
    private User mLoginUser;

    String TAG = "LOGIN PROCEDURE";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public OnlineSignInFragment() {
        // Required empty public constructor
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        //wiring up widgets
        mEmailEditText = (EditText)view.findViewById(R.id.edit_text_enter_email);
        mPasswordEditText = (EditText)view.findViewById(R.id.edit_text_enter_password);
        mTwitterSignIn = (FancyButton)view.findViewById(R.id.button_twitter_online_sign_in);
        mFacebookSignIn = (FancyButton)view.findViewById(R.id.button_facebook_online_sign_in);
        mGoogleSignIn = (FancyButton)view.findViewById(R.id.button_gplus_online_sign_in);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        //getting persisted state
        if (savedInstanceState!=null){
            mEmailEditText.setText(savedInstanceState.getString("EMAIL"));
            mPasswordEditText.setText(savedInstanceState.getString("PASSWORD"));
        }



        //onClick listener for the signIn button - checks for valid login information
        //mSignInButton = (Button)view.findViewById(R.id.button_sign_in);
        mSignInButton = (FancyButton)view.findViewById(R.id.button_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailEditText.getText().length() < 6){
                    Toast.makeText(getActivity(), "Enter a valid email",Toast.LENGTH_SHORT).show();
                }
                else if(mPasswordEditText.getText().length()<6){
                    Toast.makeText(getActivity(), "Enter a valid password - more than 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
                else if (mEmailEditText.getText().length() < 6 &&
                        mPasswordEditText.getText().length()<6){
                    Toast.makeText(getActivity(), "Enter valid login information",
                            Toast.LENGTH_SHORT).show();
                }
                //check for valid user and start the profile activity
                else{
                    mLoginEmail = mEmailEditText.getText().toString();
                    mLoginPassword = mPasswordEditText.getText().toString();


                    showProgressDialog();

                    //Try to authenticate the user's credentials
                    mAuth.signInWithEmailAndPassword(mLoginEmail, mLoginPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    hideProgressDialog();

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail", task.getException());
                                        Toast.makeText(getActivity().getBaseContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    else{
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        DrinkingAppCollection.mMainUser = mLoginUser;
                                        //intent.putExtra("EMAIL", mLoginEmail);
                                        //intent.putExtra("PASSWORD", mLoginPassword);
                                        startActivity(intent);
                                    }
                                }
                            });



                }
            }
        });

        //listener for the signUp button - starts the signUp Fragment
        //mSignUpButton = (Button)view.findViewById(R.id.button_sign_up);
        mSignUpButton = (FancyButton) view.findViewById(R.id.button_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignUpStarted();
            }
        });


        return view;
    }

    

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (SignUpProcess)context;
    }

    //persisting the state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EMAIL",mEmailEditText.getText().toString());
        outState.putString("PASSWORD",mPasswordEditText.getText().toString());
    }

}
