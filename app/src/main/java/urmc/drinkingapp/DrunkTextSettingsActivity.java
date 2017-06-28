package urmc.drinkingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import mehdi.sakout.fancybuttons.FancyButton;

public class DrunkTextSettingsActivity extends AppCompatActivity {

    private FancyButton mYes;
    private FancyButton mNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drunk_text_settings);


        mYes = (FancyButton) findViewById(R.id.button_text_settings_yes);
        mNo = (FancyButton) findViewById(R.id.button_text_settings_no);


        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DrunkTextSettingsActivity.this, MainActivity.class);

                i.putExtra("ANALYZETEXT", 1);

                startActivity(i);
                finish();
            }
        });

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DrunkTextSettingsActivity.this, MainActivity.class);

                i.putExtra("ANALYZETEXT", 0);

                startActivity(i);
                finish();
            }
        });
    }
}
