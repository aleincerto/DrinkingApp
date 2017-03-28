package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
//fragment to display the profile of the main user
public class OnlineProfileFragment extends Fragment {


    public interface EditProfileProcess{
        void EditProfileStarted();
    }

    private EditProfileProcess mListener;

    private String mEmail;

    //widgets
    private ImageView mProfilePicture;
    private TextView mFullnameTextView;
    private TextView mEmailTextView;
    //private Button mEditProfileButton;
    private FancyButton mEditProfileButton;
    final String TAG = "PROFILE FRAGMENT";

    private User mUser;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private StorageReference mUserStorageRef;


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


    public OnlineProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_profile, container, false);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        mUserStorageRef = FirebaseStorage.getInstance().getReference().child(getUid());

        mProfilePicture = (ImageView)view.findViewById(R.id.image_view_profile_pic);
        mFullnameTextView = (TextView)view.findViewById(R.id.text_view_fullname_profile);
        mEmailTextView = (TextView)view.findViewById(R.id.text_view_email_profile);


        showProgressDialog();
        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Log.d("PROFILE",dataSnapshot.toString());
                        mUser = dataSnapshot.getValue(User.class);
                        hideProgressDialog();
                        // [START_EXCLUDE]
                        if (mUser == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //setting appropriate information in the widgets according to the user's attributes
                            mFullnameTextView.setText(mUser.getFullname());
                            mEmailTextView.setText(mUser.getEmail());
                            //setting profile picture
                            String mPath = mUser.getProfilePic();

                            if (!mPath.matches("none")){
                                loadPic();
                                /*
                                Bitmap photo = getScaledBitmap(mPath, 200, 200);
                                mProfilePicture.setImageBitmap(photo);
                                */
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
        // [END single_value_read]

        //starting the EditProfileActivity when EditProfile Button is pressed
        //mEditProfileButton = (Button)view.findViewById(R.id.button_edit_profile);
        mEditProfileButton = (FancyButton) view.findViewById(R.id.button_edit_profile);
        mEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.EditProfileStarted();

                /*
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("EMAIL", mEmail);
                getActivity().startActivityForResult(intent, 0);
                updateUI();*/
            }
        });



        return view;
    }



    //method to update the UI after the EditProfile Activity Returns
    public void updateUI(){
        showProgressDialog();
        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        mUser = dataSnapshot.getValue(User.class);
                        hideProgressDialog();

                        // [START_EXCLUDE]
                        if (mUser == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //setting appropriate information in the widgets according to the user's attributes
                            mFullnameTextView.setText(mUser.getFullname());
                            mEmailTextView.setText(mUser.getEmail());
                            //setting profile picture
                            String mPath = mUser.getProfilePic();

                            if (!mPath.matches("none")){
                                loadPic();
                                /*
                                Bitmap photo = getScaledBitmap(mPath, 200, 200);
                                mProfilePicture.setImageBitmap(photo);
                                */
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
        mListener = (OnlineProfileFragment.EditProfileProcess)context;
    }


    private void loadPic(){
        Glide.with(getActivity() /* context */)
                .using(new FirebaseImageLoader())
                .load(mUserStorageRef)
                .into(mProfilePicture);
    }

}
