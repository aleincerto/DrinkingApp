package urmc.drinkingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import urmc.drinkingapp.model.PhoneNumbers;

/**
 * Created by litchiyang on 6/27/17.
 */

public class PhoneNumberViewHolder extends RecyclerView.ViewHolder{

    private TextView mNumberView;

    private PhoneNumbers mPhoneNumber;

    //view holder
    public PhoneNumberViewHolder(View v) {
        super(v);
        mNumberView = (TextView)v.findViewById(R.id.view_phone_numbers);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("PHONENUMBER", mPhoneNumber.getNumber());
            }
        });
    }

    //Bind phone numbers
    public void bindPhoneNumbers(PhoneNumbers numbers) {
        mPhoneNumber = numbers;
        mNumberView.setText(numbers.getNumber());
        Log.d("YANG", "bindPhoneNumbers: " + numbers.getNumber());
    }

}
