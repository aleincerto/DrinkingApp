package urmc.drinkingapp;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import urmc.drinkingapp.database.*;


import java.util.List;

import urmc.drinkingapp.database.PhoneNumberCollection;
import urmc.drinkingapp.model.PhoneNumbers;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneNumbersFragment extends Fragment {

    private RecyclerView mPhoneNumberRecyclerView;
    private PhoneNumberCollection mCollection;
    private PhoneNumberAdapter mAdapter;



    public PhoneNumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mCollection = PhoneNumberCollection.get(getContext());
        View view =  inflater.inflate(R.layout.fragment_phone_numbers, container, false);

        mPhoneNumberRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_phone_numbers);
        mPhoneNumberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<PhoneNumbers> numbers = PhoneNumberCollection.get().getCollection();
        mPhoneNumberRecyclerView.setAdapter(new PhoneNumberAdapter(numbers));

        updateUI();

        return view;
    }

    public void updateUI() {
        List<PhoneNumbers> pn = mCollection.getCollection();

        if(mAdapter == null) {
            mAdapter = new PhoneNumberAdapter(pn);
            mPhoneNumberRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setPhoneNumber(pn);
        }
    }



}
