package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

import urmc.drinkingapp.R;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;

/**
 * Friends fragment displays a list of the user's friends. It is used to display the resulting users when a search is performed.
 * It is not currently being used to display the user's friends because the MyFriendsTabFragment is being used. However, this fragment
 * continues to be used to display the results of a search for a user
 */

public class FriendsFragment extends Fragment {

    //instance of the recylcer view
    private RecyclerView mRecyclerView;

    //private DrinkingAppCollection mCollection;
    private FirebaseRecyclerAdapter mAdapter;


    private String mQuery;

    private Context mContext;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    //Reference to the user's friends
    private DatabaseReference mFriendReference;

    //reference to the database storage to load the profile pictures of the different users
    private StorageReference mUserStorageRef;

    private boolean mAreWeFriends;

    //private NoResultsProcess mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public interface NoResultsProcess{
        void NoResultStarted();
    }

    //Process dialog to be displayed while the app loads the user's friends
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
        View view = inflater.inflate(R.layout.fragment_friends, container, false);


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        mContext = getActivity();

        //gets the database collection - OFFLINE DATABASE
        //mCollection = DrinkingAppCollection.get(mContext);

        //sets up the recycler view
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //getting arguments being passed - Used when we have to display the results of a search
        Bundle args = getArguments();
        if(args!=null){
            mQuery = args.getString("QUERY");
            OnlineUpdateUI(doMySearch(mQuery));
        }else {
            OnlineUpdateUI();
        }

        return view;
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // All my users

        Query q = databaseReference.child("users");
        Log.d("QUERY",q.toString());
        return q;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void OnlineUpdateUI(){
        mAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(User.class, R.layout.friends_view_holder, FriendsViewHolder.class, getQuery(mDatabase)) {
            @Override
            public void populateViewHolder(FriendsViewHolder FriendProfileHolder, User user, int position) {
                FriendProfileHolder.bindUser(user);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    //Load Friends and populate the list
    public void OnlineUpdateUI(Query query){
        showProgressDialog();
        mAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(User.class, R.layout.friends_view_holder, FriendsViewHolder.class, query) {
            @Override
            public void populateViewHolder(FriendsViewHolder FriendProfileHolder, User user, int position) {
                FriendProfileHolder.bindUser(user);
                hideProgressDialog();

                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                //Log.d("FrList",postKey);

                final FriendsViewHolder myView = FriendProfileHolder;

                //try to obtain that user from list of friends
                mFriendReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(getUid()).child("friends").child(postKey);
                //get that user's profile picture
                mUserStorageRef = FirebaseStorage.getInstance().getReference().child(postKey);

                showProgressDialog();
                if(!user.getProfilePic().equals("none")) {
                    Glide.with(getActivity() /* context */)
                            .using(new FirebaseImageLoader())
                            .load(mUserStorageRef)
                            .into(myView.mProfilePic);
                }

                //check if user is a friend and update view accordingly
                mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Log.d("List Dis BUTTON",dataSnapshot.toString());
                        if (dataSnapshot.getValue()==null){
                            mAreWeFriends = false;
                        }else {
                            mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                            if (dataSnapshot.getValue(Boolean.class)){
                                myView.mAddFriendButton.setText("-");
                                myView.mAddFriendButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                myView.mAddFriendButton.setTextColor(Color.parseColor("#000000"));
                            }
                        }
                        hideProgressDialog();
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("IsFrList", "getUser:onCancelled", databaseError.toException());
                    }
                });

                //expand profile onClick
                FriendProfileHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), ExpandedProfileActivity.class);
                        intent.putExtra("KEY", postKey);
                        startActivity(intent);
                    }
                });

                //check if user is a friend and react accordingly when addfriend button is clicked - It converts to a deleteFriend button is user is already a friend
                FriendProfileHolder.mAddFriendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFriendReference = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(getUid()).child("friends").child(postKey);
                        showProgressDialog();
                        mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                Log.d("List Dis BUTTON",dataSnapshot.toString());
                                if (dataSnapshot.getValue()==null){
                                    mDatabase.child("users").child(getUid()).child("friends").child(postKey).setValue(true);
                                    mDatabase.child("users").child(postKey).child("friends").child(getUid()).setValue(true);
                                    myView.mAddFriendButton.setText("-");
                                    myView.mAddFriendButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                    myView.mAddFriendButton.setTextColor(Color.parseColor("#000000"));
                                }else {
                                    mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                                    if (dataSnapshot.getValue(Boolean.class)){
                                        //mDatabase.child("users").child(getUid()).child("friends").child(postKey).setValue(false);
                                        mDatabase.child("users").child(getUid()).child("friends").child(postKey).removeValue();
                                        mDatabase.child("users").child(postKey).child("friends").child(getUid()).removeValue();
                                        myView.mAddFriendButton.setText("+");
                                        myView.mAddFriendButton.setBackgroundColor(Color.parseColor("#ff5a5f"));
                                        myView.mAddFriendButton.setTextColor(Color.parseColor("#ffffff"));
                                    }else{
                                        mDatabase.child("users").child(getUid()).child("friends").child(postKey).setValue(true);
                                        mDatabase.child("users").child(postKey).child("friends").child(getUid()).setValue(true);
                                        myView.mAddFriendButton.setText("-");
                                        myView.mAddFriendButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                        myView.mAddFriendButton.setTextColor(Color.parseColor("#000000"));
                                    }
                                }
                                hideProgressDialog();
                                // [START_EXCLUDE]
                                // [END_EXCLUDE]
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("IsFrList", "getUser:onCancelled", databaseError.toException());
                            }
                        });
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    //Perform search on the database using the queryText
    public Query doMySearch(String queryText){
        return mDatabase.child("users")
                .orderByChild("fullname")
                .startAt(queryText)
                .endAt(queryText+"\uf8ff");

    }

    /*
    //sets the adapter and updates the UI
    public void UpdateUI(){
        mCollection = DrinkingAppCollection.get(getActivity());
        List<User> users = mCollection.getAllUsersButMyself(mCollection.mMainUser.getEmail());
        if (mAdapter == null){
            mAdapter = new FriendsAdapter(users);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setUsers(users);
            mAdapter.notifyDataSetChanged();
        }

    }

    //sets the adapter and updates the UI
    public void UpdateUI(String Query){
        mCollection = DrinkingAppCollection.get(getActivity());
        List<User> users = mCollection.getAllUsersBasedOnQuery(Query);
        if (users.isEmpty()){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            FragmentManager manager = getFragmentManager();
            NoResultsDialog dialog = NoResultsDialog.newInstance();
            dialog.show(manager, "NoResultsDialog");
            //mListener.NoResultStarted();
        }else{
        if (mAdapter == null){
            mAdapter = new FriendsAdapter(users);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setUsers(users);
            mAdapter.notifyDataSetChanged();
        }}

    }*/

    public void isFriend(String key){
        final String myKey = key;
        mFriendReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(getUid()).child("friends").child(key);
        showProgressDialog();
        mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                Log.d("List Dis BUTTON",dataSnapshot.toString());
                if (dataSnapshot.getValue()==null){
                    mAreWeFriends = false;
                }else {
                    mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                }
                hideProgressDialog();
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("IsFrList", "getUser:onCancelled", databaseError.toException());
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mListener = (NoResultsProcess) context;
    }




}
