package urmc.drinkingapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import urmc.drinkingapp.R;

/**
 * A simple {@link Fragment} subclass.
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
