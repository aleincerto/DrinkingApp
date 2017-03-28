package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
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
    private FancyButton mAddFriendButton;

    private User mUser;
    private String mKey;
    private boolean mAreWeFriends;

    final String TAG = "EXPANDED PROFILE";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private DatabaseReference mUserReference;
    private DatabaseReference mFriendReference;
    private StorageReference mUserStorageRef;


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

        mUserStorageRef = FirebaseStorage.getInstance().getReference().child(mKey);

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
                        /*
                        Bitmap photo = getScaledBitmap(mPath, 200, 200);
                        mProfilePicture.setImageBitmap(photo);
                        */
                        loadPic();
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


        mAddFriendButton = (FancyButton) view.findViewById(R.id.button_add_friend_expanded_profile_fragment);
        mFriendReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(getUid()).child("friends").child(mKey);

        showProgressDialog();
        mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                Log.d("DISABLED BUTTON",dataSnapshot.toString());
                if (dataSnapshot.getValue()==null){
                    mAreWeFriends = false;
                }else {
                    mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                }
                hideProgressDialog();
                // [START_EXCLUDE]
                if (mAreWeFriends) {
                    mAddFriendButton.setText("Delete Friend");
                    mAddFriendButton.setBackgroundColor(Color.parseColor("#ffffff"));
                    mAddFriendButton.setTextColor(Color.parseColor("#000000"));
                    //mAddFriendButton.setEnabled(false);
                    /*
                    // User is null, error out
                    Log.e(TAG, "User is unexpectedly null");
                    Toast.makeText(getActivity(),
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();*/

                } else {
                    Log.d(TAG, "We are not friends");
                }

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });


        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAreWeFriends){
                    //mDatabase.child("users").child(getUid()).child("friends").child(mKey).setValue(false);
                    mDatabase.child("users").child(getUid()).child("friends").child(mKey).removeValue();
                    mDatabase.child("users").child(mKey).child("friends").child(getUid()).removeValue();
                    mAddFriendButton.setText("Add Friend");
                    mAreWeFriends = false;
                    mAddFriendButton.setBackgroundColor(Color.parseColor("#ff5a5f"));
                    mAddFriendButton.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    mDatabase.child("users").child(getUid()).child("friends").child(mKey).setValue(true);
                    mDatabase.child("users").child(mKey).child("friends").child(getUid()).setValue(true);
                    mAddFriendButton.setText("Delete Friend");
                    mAreWeFriends = true;
                    mAddFriendButton.setBackgroundColor(Color.parseColor("#ffffff"));
                    mAddFriendButton.setTextColor(Color.parseColor("#000000"));
                }

                //mAddFriendButton.setEnabled(false);
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

    private void loadPic(){
        Glide.with(getActivity() /* context */)
                .using(new FirebaseImageLoader())
                .load(mUserStorageRef)
                .into(mProfilePicture);
    }


}
