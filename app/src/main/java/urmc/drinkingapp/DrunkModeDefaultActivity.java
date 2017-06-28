package urmc.drinkingapp;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;
import ng.max.slideview.SlideView;
import urmc.drinkingapp.R;
import urmc.drinkingapp.database.DrinkingAppCollection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Drunk Mode activity currently being used
 * Attempts to get any phone number and text message that was preset for texting
 * Also attempts to get any phone number that was pretext for calling
 */
public class DrunkModeDefaultActivity extends AppCompatActivity {

 //   private FancyButton settingsButton;
    private FancyButton emergencyButton;
    private FancyButton cabButton;
    private FancyButton textButton;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private final int MY_PERMISSIONS_CALL = 5;
    private String phoneNo;
    private String message;
    private String call;

    boolean buddyLocation;
    boolean useText;
    boolean useCall;

    //Wires up buttons and sets event listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunk_mode_default);
        Toast.makeText(DrunkModeDefaultActivity.this, "DOES THIS SHOW UP?" , Toast.LENGTH_LONG);

        //Get intents if anything is preset
        phoneNo = getIntent().getStringExtra("PRESETPHONENUMBER");
        message = getIntent().getStringExtra("PRESETTEXT");
        call = getIntent().getStringExtra("PRESETCALL");

        buddyLocation = getIntent().getBooleanExtra("BUDDYCHECKED", false);
        useText = getIntent().getBooleanExtra("TEXTCHECKED", false);
        useCall = getIntent().getBooleanExtra("CALLCHECKED", false);


        //return to the main activity of slider is used
        ((SlideView) findViewById(R.id.switch_drunk_mode_default_activity)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                finish();
            }
        });




        textButton = (FancyButton) findViewById(R.id.button_text_default_drunk_mode);

        if(useCall) {
            //Send text message when button is pressed
            textButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSMSMessage();
                }
            });
        } else {
            textButton.setVisibility(View.GONE);
        }




        cabButton = (FancyButton) findViewById(R.id.button_cab_default_drunk_mode);

        if(useText) {

            //if no information is preset ask user to set it
            //check for permission to call --> if there is no permission then ask user for permission
            cabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(DrunkModeDefaultActivity.this, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        //call
                        if (phoneNo == null) {
                            Toast.makeText(getApplicationContext(), "Please go to settings and fill up the information",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("TRIED CALLING", phoneNo);
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phoneNo));
                            startActivity(callIntent);
                        }
                    } else {
                        // Show rationale and request permission.
                        ActivityCompat.requestPermissions(DrunkModeDefaultActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_CALL);
                    }


                    //begin to call cab

                    //Toast.makeText(DrunkModeDefaultActivity.this, "Calling" + phoneNoCAB, Toast.LENGTH_LONG);

                    /*
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0377778888"));

                    if (ActivityCompat.checkSelfPermission(DrunkModeDefaultActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);

               */


                }

            });
        } else {
            cabButton.setVisibility(View.GONE);

        }


        FancyButton mLocation = (FancyButton) findViewById(R.id.button_location);
        //start mapsActivity
        if(buddyLocation) {
            mLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent i = new Intent(MainActivity.this, FriendsActivity.class);
                    //Intent i = new Intent(MainActivity.this, FriendsFullScreenSearchActivity.class);
                    Intent i = new Intent(DrunkModeDefaultActivity.this, MapsActivity.class);
                    startActivity(i);
                }
            });
        } else {
            mLocation.setVisibility(View.GONE);
        }



    }


    //Check for permission to text, if no permission then ask for permission
    protected void sendSMSMessage() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

            /*
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
            */
        }else{
            //no info about text message
            if (phoneNo == null || message == null){
                Toast.makeText(getApplicationContext(), "Please go to settings and fill up the information",
                        Toast.LENGTH_LONG).show();
            }else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS to " + phoneNo + " failed, please try again.", Toast.LENGTH_LONG).show();
                }
            }
            case MY_PERMISSIONS_CALL:
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.CALL_PHONE &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+phoneNo));
                        startActivity(callIntent);
                    }
                } else {
                    // Permission was denied. Display an error message.
                }
        }

    }

}
