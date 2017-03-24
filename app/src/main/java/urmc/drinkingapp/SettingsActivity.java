package urmc.drinkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

import static android.R.attr.duration;
import static urmc.drinkingapp.R.id.text;

public class SettingsActivity extends AppCompatActivity {


    EditText phonenumberforTEXTING;
    EditText messageforTEXTING;
    EditText phonenumberforCALLING;
    FancyButton saveButton;

    String numberforcab;

    int customnumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        phonenumberforTEXTING = (EditText) findViewById(R.id.phone_number_editText);
        messageforTEXTING = (EditText) findViewById(R.id.message_editText);
        phonenumberforCALLING = (EditText) findViewById(R.id.other_editText);

        saveButton = (FancyButton) findViewById(R.id.button_settings_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phonenumber_to_text = phonenumberforTEXTING.getText().toString();
                String message_to_text = messageforTEXTING.getText().toString();
                String phonenumber_to_call = phonenumberforCALLING.getText().toString();

                Intent i = new Intent(SettingsActivity.this, DrunkModeDefaultActivity.class);

                if (customnumber ==1){
                    numberforcab= phonenumberforCALLING.getText().toString();

                }


                i.putExtra("PHONE_NUMBER_TEXT", phonenumber_to_text);
                i.putExtra("MESSAGE_TEXT", message_to_text);
               i.putExtra("PHONE_NUMBER_CALL", phonenumber_to_call);

                startActivity(i);
                //need to add way to save the state
            }
        });






    }







    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();



        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cab1:
                if (checked)

                {
                    numberforcab ="5856479970";
                    break;
                }
            case R.id.radio_cab2:
                if (checked)
                    numberforcab ="5852353333";
                    break;
            case R.id.radio_othercab:
                if (checked)

                {


                    phonenumberforCALLING.setVisibility(View.VISIBLE);
                    customnumber=1;

                }



                    break;
        }}
}
