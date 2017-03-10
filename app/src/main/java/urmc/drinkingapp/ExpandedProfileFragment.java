package urmc.drinkingapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedProfileFragment extends Fragment {


    private DrinkingAppCollection mCollection;

    private Context mContext;

    private String mEmail;
    private String mPassword;

    //widgets
    private ImageView mProfilePicture;
    private TextView mFullnameTextView;

    private User mUser;


    public ExpandedProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expanded_profile, container, false);

        mContext = getActivity();

        //instance of the database collection
        mCollection = DrinkingAppCollection.get(mContext);


        //get arguments being passed - email to ge the user
        Bundle args = getArguments();
        mEmail = args.getString("EMAIL");

        //getting the user
        mUser = mCollection.getUser(mEmail);
        //wiring up widgets
        mProfilePicture = (ImageView)view.findViewById(R.id.image_view_profile_pic_expanded_profile_fragment);
        mFullnameTextView = (TextView)view.findViewById(R.id.text_view_fullname_profile_expanded_profile_fragment);

        //setting appropriate information in the widgets according to the user's attributes
        mFullnameTextView.setText(mUser.getFullname());

        //setting profile picture
        String mPath = mUser.getProfilePic();

        if (!mPath.matches("none")){
            Bitmap photo = User.getScaledBitmap(mPath, 200, 200);
            mProfilePicture.setImageBitmap(photo);
            mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return view;
    }


}
