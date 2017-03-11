package urmc.drinkingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Alessandro on 3/10/17.
 */

public class NoResultsDialog extends DialogFragment {

    private TextView mMessage;


    public NoResultsDialog() {
        // Required empty public constructor
    }

    public static NoResultsDialog newInstance() {


        NoResultsDialog fragment = new NoResultsDialog();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_no_result_dialog, null);


        mMessage = (TextView)view.findViewById(R.id.text_view_no_result);


        //displaying the AlertDialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Oops!")
                .setPositiveButton("Ok", null)
 //               .setNegativeButton("Really Cool", null)
                .create();
    }
}
