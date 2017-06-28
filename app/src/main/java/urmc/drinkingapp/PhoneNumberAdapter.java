package urmc.drinkingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import urmc.drinkingapp.model.PhoneNumbers;

/**
 * Created by litchiyang on 6/27/17.
 */

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberViewHolder> {

    private List<PhoneNumbers> mNumber;


    //Constructor
    public PhoneNumberAdapter(List<PhoneNumbers> numbers) {
        mNumber = numbers;
    }


    //Create the view for the holder
    @Override
    public PhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_phone_numbers, parent, false);
        PhoneNumberViewHolder holder = new PhoneNumberViewHolder(v);
        return holder;
    }


    @Override
    public int getItemCount() {
        return mNumber.size();
    }


    //Bind the view holder
    @Override
    public void onBindViewHolder(PhoneNumberViewHolder holder, int position) {
        holder.bindPhoneNumbers(mNumber.get(position));
    }

    public void setPhoneNumber(List<PhoneNumbers> pn) {
        mNumber = pn;
        notifyDataSetChanged();
    }
}
