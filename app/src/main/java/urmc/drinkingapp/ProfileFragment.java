package urmc.drinkingapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * NOT CURRENTLY BEING USED. Fragment to display the profile of the main user (Uses Offline Database).
 * The OnlineProfileFragment is being utilized instead.
 */
public class ProfileFragment extends Fragment {


    private String mEmail;

    //widgets
    private ImageView mProfilePicture;
    private TextView mFullnameTextView;
    private TextView mEmailTextView;
    //private Button mEditProfileButton;
    private FancyButton mEditProfileButton;

    private DrinkingAppCollection mCollection;

    private User mUser;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        //getting instance of the database collection
        mCollection = DrinkingAppCollection.get(getContext());


        mEmail = DrinkingAppCollection.mMainUser.getEmail();
        //using the email to get the user from the database
        mUser = mCollection.getUser(mEmail);

        //setting the main user
        DrinkingAppCollection.mMainUser = mUser;
        //wiring up widgets
        mProfilePicture = (ImageView)view.findViewById(R.id.image_view_profile_pic);
        mFullnameTextView = (TextView)view.findViewById(R.id.text_view_fullname_profile);
        mEmailTextView = (TextView)view.findViewById(R.id.text_view_email_profile);

        //setting appropriate information in the widgets according to the user's attributes
        mFullnameTextView.setText(mUser.getFullname());
        mEmailTextView.setText(mUser.getEmail());

        //setting profile picture
        String mPath = mUser.getProfilePic();

        if (!mPath.matches("none")){
            Bitmap photo = getScaledBitmap(mPath, 200, 200);
            mProfilePicture.setImageBitmap(photo);
            mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        //starting the EditProfileActivity when EditProfile Button is pressed
        //mEditProfileButton = (Button)view.findViewById(R.id.button_edit_profile);
        mEditProfileButton = (FancyButton) view.findViewById(R.id.button_edit_profile);
        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("EMAIL", mEmail);
                getActivity().startActivityForResult(intent, 0);
                updateUI();
            }
        });


        return view;
    }


    //method to update the UI after the EditProfile Activity Returns
    public void updateUI(){
        User updateUIUser = mCollection.getUser(mEmail);
        mFullnameTextView.setText(updateUIUser.getFullname());
        mEmailTextView.setText(updateUIUser.getEmail());

        String mPath = updateUIUser.getProfilePic();

        if (!mPath.matches("none")){
            Bitmap photo = getScaledBitmap(mPath, 200, 200);
            mProfilePicture.setImageBitmap(photo);
            mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    //method to fix pictures to be displayed in the app
    public Bitmap getScaledBitmap(String path, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int sampleSize = 1;
        if(srcHeight > height || srcWidth > width ) {
            if(srcWidth > srcHeight) {
                sampleSize = Math.round(srcHeight / height);
            } else {
                sampleSize = Math.round(srcWidth / width);
            }
        }
        BitmapFactory.Options scaledOptions = new BitmapFactory.Options(); scaledOptions.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(path, scaledOptions);
    }

}
