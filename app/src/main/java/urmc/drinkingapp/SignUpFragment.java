package urmc.drinkingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * NOT CURRENTLY BEING USED. Fragment to create a new user in the offline database. OnlineSignUpFragment is being used instead.
 */
public class SignUpFragment extends Fragment {

    //private Button mSignUpButton;
    //private Button mCancelButton;
    private FancyButton mSignUpButton;
    private FancyButton mCancelButton;
    private EditText mNameEditText;
    private EditText mLastNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private String mLoginEmail;
    private String mLoginPassword;
    private String mLoginName;
    private String mLoginLastName;
    private User mLoginUser;

    private DrinkingAppCollection mCollection;
    private LoginActivity mActivity;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "SIGN UP PROCEDURE";

    public interface SignUpProcessCancel{
        void SignUpCancel();
    }

    private SignUpProcessCancel mListener;


    public SignUpFragment() {
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

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

        //getting instance of the database collection
        mCollection = DrinkingAppCollection.get(getContext());

        //mCancelButton = (Button)view.findViewById(R.id.button_cancel_sign_up);
        mCancelButton = (FancyButton) view.findViewById(R.id.button_cancel_sign_up);
        mNameEditText = (EditText)view.findViewById(R.id.edit_text_name_sign_up);
        mLastNameEditText = (EditText)view.findViewById(R.id.edit_text_last_name_sign_up);
        mEmailEditText = (EditText)view.findViewById(R.id.edit_text_enter_email_sign_up);
        mPasswordEditText = (EditText)view.findViewById(R.id.edit_text_enter_password_sign_up);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignUpCancel();
                /*
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_login_activity,new LoginFragment())
                        .commit();
                        */
            }
        });

        //mSignUpButton = (Button)view.findViewById(R.id.button_sign_up_sing_up);
        mSignUpButton = (FancyButton) view.findViewById(R.id.button_sign_up_sing_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailEditText.getText().length() < 6){
                    Toast.makeText(getActivity(), "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else if(mPasswordEditText.getText().length()<6){
                    Toast.makeText(getActivity(), "Enter a valid password - more than 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
                else if(mNameEditText.getText().length()<1){
                    Toast.makeText(getActivity(), "Enter a valid name",
                            Toast.LENGTH_SHORT).show();
                }
                else if(mLastNameEditText.getText().length()<1){
                    Toast.makeText(getActivity(), "Enter a valid last name",
                            Toast.LENGTH_SHORT).show();
                }
                else if (mEmailEditText.getText().length() < 6 &&
                        mPasswordEditText.getText().length()<6){
                    Toast.makeText(getActivity(), "Enter valid login information",
                            Toast.LENGTH_SHORT).show();
                }
                //checks for existence of the user and else creates a new one and starts the profile activity
                else{


                    mLoginEmail = mEmailEditText.getText().toString();
                    mLoginPassword = mPasswordEditText.getText().toString();
                    mLoginName = mNameEditText.getText().toString();
                    mLoginLastName = mLastNameEditText.getText().toString();


                    /*
                    mAuth.createUserWithEmailAndPassword(mLoginEmail, mLoginPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });


                    */












                    mLoginEmail = mEmailEditText.getText().toString();
                    mLoginPassword = mPasswordEditText.getText().toString();
                    mLoginName = mNameEditText.getText().toString();
                    mLoginLastName = mLastNameEditText.getText().toString();
                    mLoginUser = mCollection.getUser(mLoginEmail, mLoginPassword);
                    User mLoginUser2 = mCollection.getUser(mLoginEmail);
                    if (mLoginUser != null){
                        Toast.makeText(getActivity(), "User already exists",
                                Toast.LENGTH_SHORT).show();
                        mPasswordEditText.setText("");
                        mEmailEditText.setText("");
                    }
                    else if (mLoginUser2 != null){
                        Toast.makeText(getActivity(), "Email already being used",
                                Toast.LENGTH_SHORT).show();
                        mPasswordEditText.setText("");
                        mEmailEditText.setText("");
                    }
                    else{
                        User signUpUser = new User();
                        signUpUser.setEmail(mLoginEmail);
                        signUpUser.setPassword(mLoginPassword);
                        signUpUser.setFullname(mLoginName + " " + mLoginLastName);
                        mCollection.addUser(signUpUser);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        DrinkingAppCollection.mMainUser = signUpUser;
                        //intent.putExtra("EMAIL", mLoginEmail);
                        //intent.putExtra("PASSWORD", mLoginPassword);

                        getActivity().startActivity(intent);
                    }
                }
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (SignUpFragment.SignUpProcessCancel)context;

    }

}
