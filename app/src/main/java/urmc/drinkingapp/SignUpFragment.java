package urmc.drinkingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Button mSignUpButton;
    private Button mCancelButton;
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

    public interface SignUpProcessCancel{
        void SignUpCancel();
    }

    private SignUpProcessCancel mListener;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        //getting instance of the database collection
        mCollection = DrinkingAppCollection.get(getContext());

        mCancelButton = (Button)view.findViewById(R.id.button_cancel_sign_up);
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

        mSignUpButton = (Button)view.findViewById(R.id.button_sign_up_sing_up);
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
