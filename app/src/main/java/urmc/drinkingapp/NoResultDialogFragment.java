package urmc.drinkingapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import urmc.drinkingapp.R;

/**
 * NOT CURRENTLY BEING USED. Dialog to be displayed when there is no result in a search for a user. Was implemented but then dropped, could be placed again.
 */
public class NoResultDialogFragment extends Fragment {


    public NoResultDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_result_dialog, container, false);
    }

}
