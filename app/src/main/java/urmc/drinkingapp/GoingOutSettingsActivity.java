package urmc.drinkingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.Collection;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.PhoneNumberCollection;
import urmc.drinkingapp.model.PhoneNumbers;

public class GoingOutSettingsActivity extends AppCompatActivity {

    private FriendsFragment mBuddyFragment;
    private PhoneNumbersFragment mPhoneNumberFragment;
    private CheckBox mBuddyCheckBox;
    private CheckBox mTextCheckBox;
    private CheckBox mCallCheckBox;
    private EditText mText;
    private EditText mCall;
    private EditText mPhoneNumber;
    private FancyButton mAddNumber;
    private FancyButton mAddCall;
    private FancyButton mSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_out_settings);

        //Set the buddy checkbox
 //       mBuddyCheckBox = (CheckBox)findViewById(R.id.check_box_bubby_settings);

//        mBuddyCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    //sets the UserFragment
//                    mFragment = new FriendsFragment();
//                    FragmentManager fm = getSupportFragmentManager();
//                    fm.beginTransaction()
//                            .add(R.id.frame_layout_friends, mFragment)
//                            .commit();
//                } else {
//
//                }
//
//            }
//        });

        mTextCheckBox = (CheckBox)findViewById(R.id.check_box_text_settings);
        mCallCheckBox = (CheckBox)findViewById(R.id.check_box_call_settings);

//        //Set the BuddyFragment
//        mBuddyFragment = new FriendsFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction()
//                .add(R.id.frame_layout_friends, mBuddyFragment)
//                .commit();



        mPhoneNumber = (EditText) findViewById(R.id.edit_text_phone_number);

        //Set the phone numbers Fragment
        FragmentManager manager1 = getSupportFragmentManager();
        mPhoneNumberFragment = new PhoneNumbersFragment();
        manager1.beginTransaction()
                .add(R.id.frame_layout_out_settings_numbers, mPhoneNumberFragment)
                .commit();

        mAddNumber = (FancyButton) findViewById(R.id.button_out_settings_add_number);
        mAddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumbers mNumber = new PhoneNumbers();
                mNumber.setNumber(mPhoneNumber.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(), "BUTTON" + mPhoneNumber.getText().toString(), Toast.LENGTH_LONG);
                toast.show();
                PhoneNumberCollection.get().addNumbers(mNumber);
            }
        });


        mText = (EditText) findViewById(R.id.edit_text_text);
        mCall = (EditText) findViewById(R.id.edit_text_call);

        FragmentManager manager2 = getSupportFragmentManager();
        mPhoneNumberFragment = new PhoneNumbersFragment();
        manager2.beginTransaction()
                .add(R.id.frame_layout_out_settings_calls, mPhoneNumberFragment)
                .commit();

        mAddCall = (FancyButton) findViewById(R.id.button_out_settings_add_calls);
        mAddCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumbers mNumber = new PhoneNumbers();
                mNumber.setNumber(mCall.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(), "BUTTON" + mCall.getText().toString(), Toast.LENGTH_LONG);
                toast.show();
                PhoneNumberCollection.get().addNumbers(mNumber);
            }
        });

        mSave = (FancyButton) findViewById(R.id.button_out_settings_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String presetPhoneNumber = mPhoneNumber.getText().toString();
                String presetText = mText.getText().toString();
                String presetCall = mCall.getText().toString();

                Intent i = new Intent(GoingOutSettingsActivity.this, DrunkModeDefaultActivity.class);

                i.putExtra("PRESETPHONENUMBER", presetPhoneNumber);
                i.putExtra("PRESETTEXT", presetText);
                i.putExtra("PRESETCALL", presetCall);
                i.putExtra("BUDDYCHECKED", mBuddyCheckBox.isChecked());
                i.putExtra("TEXTCHECKED", mTextCheckBox.isChecked());
                i.putExtra("CALLCHECKED", mCallCheckBox.isChecked());

                startActivity(i);
                finish();
            }
        });

    }


}
