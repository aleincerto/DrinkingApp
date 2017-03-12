package urmc.drinkingapp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    //widgets
    private ImageView mProfilePicImageView;
    private EditText mFullnameEditText;
    private EditText mEmailEditText;
    //private Button mOkButton;
    //private Button mChangePicButton;
    private FancyButton mOkButton;
    private FancyButton mChangePicButton;
    //collection
    private DrinkingAppCollection mCollection;

    private User mUser;

    private String mEmail;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        //getting instance of the collection
        mCollection = DrinkingAppCollection.get(getContext());

        //getting arguments being passed - email of the user to be displayed
        Bundle args = getArguments();
        mEmail = args.getString("EMAIL");

        //gets the user from the database
        mUser = mCollection.getUser(mEmail);

        //wiring up the widgets
        mProfilePicImageView = (ImageView)view.findViewById(R.id.image_view_profile_pic_edit_profile);
        mFullnameEditText = (EditText)view.findViewById(R.id.edit_text_fullname_profile);
        mEmailEditText = (EditText)view.findViewById(R.id.edit_text_email_profile);

        //setting all the standard info according to the obtained user
        mFullnameEditText.setText(mUser.getFullname());
        mEmailEditText.setText(mUser.getEmail());

        //onSavedInstanceState standard procedure to persist the state
        if (savedInstanceState!=null){
            mFullnameEditText.setText(savedInstanceState.getString("FULLNAME"));
            mEmailEditText.setText(savedInstanceState.getString("EMAIL"));
        }

        //get the path to the profile picture
        String mPath = mUser.getProfilePic();

        //check if the user actually has a picture and sets it
        if (!mPath.matches("none")){
            Bitmap photo = getScaledBitmap(mPath, 200, 200);
            mProfilePicImageView.setImageBitmap(photo);
            mProfilePicImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        //ok button to save changes
        //mOkButton = (Button)view.findViewById(R.id.button_ok_edit_profile);
        mOkButton = (FancyButton) view.findViewById(R.id.button_ok_edit_profile);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFullname(mFullnameEditText.getText().toString());
                mUser.setEmail(mEmailEditText.getText().toString());
                mCollection.updateUser(mUser);
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });


        return view;
    }

    //onSavedInstanceState standard procedure to persist the state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("FULLNAME", mFullnameEditText.getText().toString());
        outState.putString("EMAIL",mEmailEditText.getText().toString());
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
