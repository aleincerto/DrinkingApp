package urmc.drinkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import mehdi.sakout.fancybuttons.FancyButton;
import ng.max.slideview.SlideView;

public class MainActivity extends AppCompatActivity {

    //private Button mProfile;
    //private Button mFriends;
    private FancyButton mProfile;
    private FancyButton mFriends;
    //private Switch mDrunkMode;
    private SlideView mDrunkMode;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "test");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Alessandro Incerto");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

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
                //Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                Intent i = new Intent(MainActivity.this, FriendsFullScreenSearchActivity.class);
                startActivity(i);
            }
        });

        /*
        mDrunkMode = (Switch) findViewById(R.id.switch_main_activity);
        mDrunkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent i = new Intent(MainActivity.this, DrunkModeDefaultActivity.class);
                startActivity(i);
            }
        });

        */

        ((SlideView) findViewById(R.id.switch_main_activity)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Intent i = new Intent(MainActivity.this, DrunkModeDefaultActivity.class);
                startActivity(i);
            }
        });

    }
}
