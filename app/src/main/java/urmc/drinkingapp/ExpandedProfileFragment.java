package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedProfileFragment extends Fragment {


    //private DrinkingAppCollection mCollection;

    private Context mContext;

    private String mEmail;
    private String mPassword;

    //widgets
    private ImageView mProfilePicture;
    private TextView mFullnameTextView;

    private User mUser;
    private String mKey;

    final String TAG = "EXPANDED PROFILE";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private DatabaseReference mUserReference;


    public ExpandedProfileFragment() {
        // Required empty public constructor
    }

    public String getUid() {return FirebaseAuth.getInstance().getCurrentUser().getUid();}

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expanded_profile, container, false);

        mContext = getActivity();

        //instance of the database collection
        //mCollection = DrinkingAppCollection.get(mContext);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        //get arguments being passed - Key to get the user
        Bundle args = getArguments();
        mKey = args.getString("KEY");

        // Initialize Database
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mKey);

        //getting the user
        //mUser = mCollection.getUser(mEmail);
        //wiring up widgets
        mProfilePicture = (ImageView)view.findViewById(R.id.image_view_profile_pic_expanded_profile_fragment);
        mFullnameTextView = (TextView)view.findViewById(R.id.text_view_fullname_profile_expanded_profile_fragment);
        showProgressDialog();
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                Log.d("PROFILE",dataSnapshot.toString());
                mUser = dataSnapshot.getValue(User.class);
                hideProgressDialog();
                // [START_EXCLUDE]
                if (mUser == null) {
                    // User is null, error out
                    Log.e(TAG, "User is unexpectedly null");
                    Toast.makeText(getActivity(),
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //setting appropriate information in the widgets according to the user's attributes
                    mFullnameTextView.setText(mUser.getFullname());
                    //setting profile picture
                    String mPath = mUser.getProfilePic();

                    if (!mPath.matches("none")){
                        Bitmap photo = getScaledBitmap(mPath, 200, 200);
                        mProfilePicture.setImageBitmap(photo);
                        mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
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


}
