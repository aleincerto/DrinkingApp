package urmc.drinkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ng.max.slideview.SlideView;
import urmc.drinkingapp.R;

public class
DrunkModeDefaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunk_mode_default);


        ((SlideView) findViewById(R.id.switch_drunk_mode_default_activity)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                finish();
            }
        });
    }



}
