package urmc.drinkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    //private Button mProfile;
    //private Button mFriends;
    private FancyButton mProfile;
    private FancyButton mFriends;
    private Switch mDrunkMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mProfile = (Button) findViewById(R.id.button_profile_main_activity);
        mProfile = (FancyButton) findViewById(R.id.button_profile_main_activity);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        //mFriends = (Button) findViewById(R.id.button_friends_main_activity);
        mFriends = (FancyButton) findViewById(R.id.button_friends_main_activity);
        mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(i);
            }
        });

        mDrunkMode = (Switch) findViewById(R.id.switch_main_activity);
        mDrunkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent i = new Intent(MainActivity.this, DrunkModeDefaultActivity.class);
                startActivity(i);
            }
        });

    }
}
