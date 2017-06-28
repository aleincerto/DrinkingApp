package urmc.drinkingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * NOT CURRENTLY BEING USED. Original fragment to Log into the app. Current version uses OnlineSignInFragment instead
 */
public class LoginFragment extends Fragment {

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

    //email and password for the user logging in
    private String mLoginEmail;
    private String mLoginPassword;
    private User mLoginUser;

    private DrinkingAppCollection mCollection;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String TAG = "Login Procedure";

    public LoginFragment() {
        // Required empty public constructor
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











        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //getting instance of the database collection
        mCollection = DrinkingAppCollection.get(getContext());

        //wiring up widgets
        mEmailEditText = (EditText)view.findViewById(R.id.edit_text_enter_email);
        mPasswordEditText = (EditText)view.findViewById(R.id.edit_text_enter_password);

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
                    mLoginUser = mCollection.getUser(mLoginEmail,mLoginPassword);
                    if (mLoginUser == null){
                        Toast.makeText(getActivity(), "Incorrect login information",
                                Toast.LENGTH_SHORT).show();
                        mPasswordEditText.setText("");
                    }
                    else{
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        DrinkingAppCollection.mMainUser = mLoginUser;
                        intent.putExtra("EMAIL", mLoginEmail);
                        intent.putExtra("PASSWORD", mLoginPassword);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

        //listener for the signUp button - checks for validity of information inputted
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
