package urmc.drinkingapp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineEditProfileFragment extends Fragment {


    //widgets
    private ImageView mProfilePicImageView;
    private EditText mFullnameEditText;
    //private Button mOkButton;
    //private Button mChangePicButton;
    private FancyButton mOkButton;
    private FancyButton mChangePicButton;
    private TextView mEmailTextView;

    private User mUser;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    final String TAG = "EDIT PROFILE";

    private EditProfileFinished mListener;

    public interface EditProfileFinished{
        void EditProfileOK();
    }


    public OnlineEditProfileFragment() {
        // Required empty public constructor
    }

    public String getUid() {return FirebaseAuth.getInstance().getCurrentUser().getUid();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_edit_profile, container, false);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]



        //wiring up the widgets
        mProfilePicImageView = (ImageView)view.findViewById(R.id.image_view_profile_pic_edit_profile);
        mFullnameEditText = (EditText)view.findViewById(R.id.edit_text_fullname_profile);
        mEmailTextView = (TextView) view.findViewById(R.id.edit_text_email_profile);


        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        mUser = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (mUser == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //setting appropriate information in the widgets according to the user's attributes
                            mFullnameEditText.setText(mUser.getFullname());
                            mEmailTextView.setText(mUser.getEmail());
                            //setting profile picture
                            String mPath = mUser.getProfilePic();

                            if (!mPath.matches("none")){
                                Bitmap photo = getScaledBitmap(mPath, 200, 200);
                                mProfilePicImageView.setImageBitmap(photo);
                                mProfilePicImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                        }

                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        //onSavedInstanceState standard procedure to persist the state
        if (savedInstanceState!=null){
            mFullnameEditText.setText(savedInstanceState.getString("FULLNAME"));
        }


        //ok button to save changes
        //mOkButton = (Button)view.findViewById(R.id.button_ok_edit_profile);
        mOkButton = (FancyButton) view.findViewById(R.id.button_ok_edit_profile);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFullname(mFullnameEditText.getText().toString());
                mDatabase.child("users").child(userId).setValue(mUser);
                mListener.EditProfileOK();
                /*
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();*/
            }
        });


        return view;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnlineEditProfileFragment.EditProfileFinished)context;

    }

}
