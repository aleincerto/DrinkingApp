package urmc.drinkingapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;
import ng.max.slideview.SlideView;

/**
 * Main Activity that is displayed after a successful login into the app. From this activity it is possible
 * to activate drunk mode, go to my profile and go to friends.
 * This activity also performs the drunk text analysis and displays the drunk texting behavior in a graph
 */
public class MainActivity extends AppCompatActivity {

    private FancyButton mProfile;
    private FancyButton mFriends;
    private SlideView mDrunkMode;
    int READ_SMS_REQUEST_CODE = 77;

    private FirebaseAnalytics mFirebaseAnalytics;

    Cursor cursor;

    private GraphView mGraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Obtain the FirebaseAnalytics instance - Initial tests with the Firabase framework
        //More useful information can be placed here to analyze how the user is using the app
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "test");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Alessandro Incerto");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //Set up initial empty graph
        mGraph = (GraphView) findViewById(R.id.main_activity_graph);
        mGraph.getGridLabelRenderer().setNumVerticalLabels(3);
        mGraph.getGridLabelRenderer().setGridColor(Color.WHITE);
        mGraph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        mGraph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        mGraph.setTitleColor(Color.WHITE);
        mGraph.setTitle("Drunk Texting Behavior");
        /*
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        mGraph.getLegendRenderer().setBackgroundColor(16777215);
        mGraph.getLegendRenderer().setTextColor(Color.WHITE);
        */

        //start profile activity
        mProfile = (FancyButton) findViewById(R.id.button_profile_main_activity);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        //start friends activity
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


        //If permission to read text messages has been granted then proceed to do so and analyze the drunk texting behavior
        //otherwise show rationale and request permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            //Check texts
            readTexts();

        } else {
            // Show rationale and request permission.
            Toast.makeText(MainActivity.this,
                    "This App requires your permission to access your texts and evaluate your drunk texting behavior",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,
                    "This App requires your permission to access your texts and evaluate your drunk texting behavior",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,
                    "This App requires your permission to access your texts and evaluate your drunk texting behavior",
                    Toast.LENGTH_LONG).show();
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

        //start drunk mode when slide is completed
        ((SlideView) findViewById(R.id.switch_main_activity)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Intent i = new Intent(MainActivity.this, DrunkModeDefaultActivity.class);
                startActivity(i);
            }
        });

    }


    //Other options - Currently using SENT
    // public static final String INBOX = "content://sms/inbox";
    // public static final String SENT = "content://sms/sent";
    // public static final String DRAFT = "content://sms/draft";

    /**
     * This method is capable of reading the SMS that the user has sent and perform the drunk texting analysis using Nabil's algorithm
     * The function stores the number of drunk texts that were sent on a given day and stores this information in a hashMap that is passed
     * displayGraph() function to show this information in a nice graph.
     */
    public void readTexts() {
        //get parameters from .txt file stored in assets folder
        HashMap<String, Float> params = readParameters();
        //get Bag of words from .txt file stored in assets folder
        ArrayList<String> BOW = getDrunkWords();

        Date initialDate = null;
        Date finalDate = null;

        cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        HashMap<Date,Integer> drunkDays = new HashMap<Date,Integer>();
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                Date messageDate = millisToDate(Long.parseLong(cursor.getString(4)));
                Log.e("DATE-TO-STRING", getSimpleDate(messageDate));
                Log.e("STRING-TO-DATE", stringToDate(getSimpleDate(messageDate)).toString());
                messageDate = stringToDate(getSimpleDate(messageDate));
                if (finalDate == null){
                    finalDate = messageDate;
                }
                //Log.e("INITIALDATE",finalDate.toString());
                initialDate = messageDate;
                //Log.e("FINALDATE",initialDate.toString());

                //using drunk text analysis to determine whether a given text is considered drunk texting or not
                //depending on the results add the date to the drunkDays hashMap and increase the counter for that day
                if (isDrunk(cursor.getString(12),params,BOW)) {
                    if (drunkDays.containsKey(messageDate)) {
                        Log.e("ADDING DATE", messageDate.toString());
                        drunkDays.put(messageDate, drunkDays.get(messageDate) + 1);
                        Log.e("TOTAL DRUNKDAYS", drunkDays.get(messageDate).toString());
                        //Toast.makeText(getBaseContext(), drunkDays.get(messageDate), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("ADDING DATE", messageDate.toString());
                        drunkDays.put(messageDate, 1);
                    }
                }

                /*

                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    //Date messageDate = null;

                    /*
                    if (cursor.getColumnName(idx).equals("date")) {
                        messageDate = millisToDate(Long.parseLong(cursor.getString(idx)));
                        if (initialDate==null){
                            initialDate = messageDate;
                        }
                        finalDate = messageDate;
                    }

                    if (cursor.getColumnName(idx).equals("body")) {
                        if (isDrunk(cursor.getString(idx),params,BOW)){
                            if (drunkDays.containsKey(messageDate)){
                                Log.e("ADDING DATE",messageDate.toString());
                                drunkDays.put(messageDate,drunkDays.get(messageDate)+1);
                            }else{
                                Log.e("ADDING DATE",messageDate.toString());
                                drunkDays.put(messageDate,1);
                            }
                        }
                        /*
                        Toast.makeText(MainActivity.this,
                                cursor.getString(idx),
                                Toast.LENGTH_SHORT).show();

                    }

                    /*
                    if (cursor.getColumnName(idx).equals("date")) {
                        Toast.makeText(MainActivity.this,
                                millisToDate(Long.parseLong(cursor.getString(idx))),
                                Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(MainActivity.this,
                            cursor.getString(12),
                            Toast.LENGTH_SHORT).show();

                } */
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
            Toast.makeText(MainActivity.this,
                    "No texts available",
                    Toast.LENGTH_SHORT).show();
        }

        //Display the drunk texting behavior on a graph
        displayGraph(drunkDays, initialDate, finalDate);
    }

    //uses graph repository: http://www.android-graphview.org/
    //to be able to display the different dates and the number of drunk texts on that day on a nice graph
    public void displayGraph(HashMap<Date,Integer> drunkDays, Date initialDate, Date finalDate){
        if (drunkDays == null || drunkDays.isEmpty()){
            Toast.makeText(MainActivity.this,
                    "Congrats! You have no drunk texts",
                    Toast.LENGTH_LONG).show();
        }else{
            DataPoint[] dataPoints = new DataPoint[drunkDays.size()];
            ArrayList<Date> dateArray = new ArrayList<>();
            int index = 0;
            for(Date date:drunkDays.keySet()){
                //Log.e("DATE",date.toString());
                if (date != null){
                    //dataPoints[index] = new DataPoint(date,drunkDays.get(date));
                    //index++;
                    dateArray.add(date);
                    //System.out.println(date +" "+drunkDays.get(date));
                }
            }
            Collections.sort(dateArray);

            for (Date date:dateArray){
                dataPoints[index] = new DataPoint(date,drunkDays.get(date));
                index++;
                //System.out.println(date +" "+drunkDays.get(date));
            }


            /*
            System.out.println(dataPoints);
            System.out.println(dateArray);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            Date d2 = calendar.getTime();

            calendar.add(Calendar.DATE, 1);
            Date d3 = calendar.getTime();

            calendar.add(Calendar.DATE, 1);
            Date d4 = calendar.getTime();

            dataPoints[dataPoints.length-3] = new DataPoint(d2,5);
            dataPoints[dataPoints.length-2] = new DataPoint(d3,1);
            dataPoints[dataPoints.length-1] = new DataPoint(d4,7);
            */


/*
            // generate Dates
            Calendar calendar = Calendar.getInstance();
            Date d1 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d2 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d3 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d4 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d5 = calendar.getTime();

            System.out.println(d5);



// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(d1, 1),
                    new DataPoint(d2, 5),
                    new DataPoint(d3, 3),
                    new DataPoint(d4, 7),
                    new DataPoint(d5, 4)
            });


            mGraph.addSeries(series);
            // set manual x bounds to have nice steps
            mGraph.getViewport().setMinX(d1.getTime());
            mGraph.getViewport().setMaxX(d3.getTime());

            */


            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            series.setTitle("Drunk Texts");
            series.setDrawDataPoints(true);
            mGraph.addSeries(series);

            // set date label formatter
            mGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(MainActivity.this));
            mGraph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

            // set manual x bounds to have nice steps

            mGraph.getViewport().setMinX(initialDate.getTime());
            mGraph.getViewport().setMaxX(finalDate.getTime());
            mGraph.getViewport().setXAxisBoundsManual(true);

            mGraph.getViewport().setScalable(true);
            mGraph.getViewport().setScrollable(true);


            //mGraph.getViewport().setMaxY(5);
            mGraph.getViewport().setMinY(0);
            mGraph.getViewport().setYAxisBoundsManual(true);



            mGraph.getGridLabelRenderer().setNumVerticalLabels(3);
            mGraph.getGridLabelRenderer().setGridColor(Color.WHITE);
            mGraph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            mGraph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            mGraph.setTitleColor(Color.WHITE);
            mGraph.setTitle("Drunk Texting Behavior");
            mGraph.getLegendRenderer().setVisible(true);
            mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            mGraph.getLegendRenderer().setBackgroundColor(16777215);
            mGraph.getLegendRenderer().setTextColor(Color.WHITE);


            // as we use dates as labels, the human rounding to nice readable numbers is not necessary
            mGraph.getGridLabelRenderer().setHumanRounding(false);
        }
    }


    //If the user gave permission then proceed to read SMS and compute the drunk texting behavior
    //if the user didn't give permission then show rationale why we need permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_SMS_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals( Manifest.permission.READ_SMS )&&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    //read messages
                    readTexts();
                }
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(MainActivity.this,
                        "Can't Analyze your drunk texting behavior without your permission. Go to phone settings to update your permissions",
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,
                        "Can't Analyze your drunk texting behavior without your permission. Go to phone settings to update your permissions",
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,
                        "Can't Analyze your drunk texting behavior without your permission. Go to phone settings to update your permissions",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Read parameters from .txt file in the assets folder
    public HashMap<String, Float> readParameters(){
        HashMap<String, Float> params = new HashMap<String, Float>();
        //try (BufferedReader br = new BufferedReader(new FileReader("par1.txt"))) {
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open("par1.txt"));
            try (BufferedReader br = new BufferedReader(is)) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.e("READFILE",line);
                    String[] parpr = line.split("	");
                    Float reg = Float.parseFloat(parpr[0]);
                    params.put(parpr[1], reg);
                }
            }
        }catch (Exception e){
            Log.e("FILEREADER","Exception Params");
        }


        return params;
    }

    //Read drunk words (bag of words) from .txt file in the assets folder
    public ArrayList<String> getDrunkWords(){
        ArrayList<String> BOW = new ArrayList<String>();
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open("bank_of_words.txt"));
            try (BufferedReader br = new BufferedReader(is)) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.e("READFILE BOW",line);
                    BOW.add(line);
                }
            }
        }catch (Exception e){
            Log.e("FILEREADER","Exception Drunk Words");
        }
        return BOW;
    }

    //Nabil's algorithm to determine if a given text is considered as drunk texting or not
    public static boolean isDrunk(String text,HashMap<String, Float> params , ArrayList<String> BOW) {
        text = text.toLowerCase();
        boolean first_test  = false;
        for(String test: BOW)
        {
            if(text.contains(test))
            {
                first_test = true;
                break;
            }
        }
        if(first_test)
        {
            float threshold = 0;
            for(String key: params.keySet())
            {
                if(text.contains(key))
                {
                    threshold += params.get(key);
                }
            }
            if(threshold > 0)
            {
                Log.e("DRUNKTEST","true"+" "+threshold);
                return true;
            }
            else
            {
                Log.e("DRUNKTEST","false"+" "+threshold);
                return false;
            }
        }
        else
        {
            Log.e("DRUNKTEST","false no threshold");
            return false;
        }

    }

    //convert the millisecond format from the SMS attributes to a date
    public Date millisToDate(long currentTime) {
        String finalDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        //Date date = calendar.getTime();
        return calendar.getTime();
    }

    //Get a string of a simple date from a regular long date Wed Oct 16 00:00:00 CEST 2013
    //Conversion back and forth being made so we can get rid of the seconds and milliseconds and compare dates by day
    public String getSimpleDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        String dateInString = "Wed Oct 16 00:00:00 CEST 2013";
        try {
            SimpleDateFormat parse = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
            Date datef = parse.parse(date.toString());

            //System.out.println(date);
            Log.e("FORMATTEDDATE",datef.toString());
            //System.out.println(formatter.format(date));
            Log.e("FORMATTEDDATE",formatter.format(datef));

            String date_to_format = formatter.format(datef);

            return formatter.format(datef);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Convert simple string date back to long date
    //Conversion back and forth being made so we can get rid of the seconds and milliseconds and compare dates by day
    public Date stringToDate(String sdate){
        //Date format_date = formatter.format(datef);
        SimpleDateFormat parse2 = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);
        try{
            Date format_date = parse2.parse(sdate);
            Log.e("FINALFORMAT",format_date.toString());
            return format_date;
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;

        //Date final_date = formatter.parse(datef.toString());


        //Date format_date = new SimpleDateFormat("MM-dd").parse(formatter.format(datef));
    }
}
