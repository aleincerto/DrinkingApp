package urmc.drinkingapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    int READ_SMS_REQUEST_CODE = 77;

    private FirebaseAnalytics mFirebaseAnalytics;

    Cursor cursor;


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
                //Intent i = new Intent(MainActivity.this, FriendsFullScreenSearchActivity.class);
                Intent i = new Intent(MainActivity.this, FriendsViewPagerActivity.class);
                startActivity(i);
            }
        });


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            //Check texts
            readTexts();

        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_SMS},
                    READ_SMS_REQUEST_CODE);
        }





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


    // public static final String INBOX = "content://sms/inbox";
    // public static final String SENT = "content://sms/sent";
    // public static final String DRAFT = "content://sms/draft";
    public void readTexts() {
        cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    if (cursor.getColumnName(idx).equals("body")) {
                        Toast.makeText(MainActivity.this,
                                cursor.getString(idx),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
            Toast.makeText(MainActivity.this,
                    "No texts available",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_SMS_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.READ_SMS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    //read messages
                    readTexts();
                } else {
                    // Permission was denied. Display an error message.
                }
            }
        }
    }
}
