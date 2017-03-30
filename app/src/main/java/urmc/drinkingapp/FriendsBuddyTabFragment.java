package urmc.drinkingapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import urmc.drinkingapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsBuddyTabFragment extends Fragment {

    //instance of the recylcer view
    private RecyclerView mRecyclerView;

    //private DrinkingAppCollection mCollection;
    private FirebaseRecyclerAdapter mAdapter;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private DatabaseReference mFriendReference;
    public DatabaseReference mUserReference;
    private StorageReference mUserStorageRef;

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

    public FriendsBuddyTabFragment() {
        // Required empty public constructor
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // All my friends
        Query q = databaseReference.child("users").child(getUid()).child("friends");
        Log.d("QUERY",q.toString());
        return q;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_buddy_tab, container, false);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        //sets up the recycler view
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_my_friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase.child("users").child(getUid()).child("buddies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Log.d("BUDDY",dataSnapshot.toString());
                }else{
                    OnlineUpdateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void OnlineUpdateUI(){
        showProgressDialog();
        //mAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(User.class, R.layout.friends_view_holder, FriendsViewHolder.class, getQuery(mDatabase))
        mAdapter = new FirebaseRecyclerAdapter<User, BuddyViewHolder>(User.class, R.layout.friends_view_holder_my_buddy, BuddyViewHolder.class, getQuery(mDatabase)) {

            @Override
            public void populateViewHolder(BuddyViewHolder FriendProfileHolder, User user, int position) {
                //FriendProfileHolder.bindUser(user);
                Log.d("LOAD BUDDY","Friends Tab");
                hideProgressDialog();

                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                //Log.d("FrList",postKey);

                final BuddyViewHolder myView = FriendProfileHolder;

                mFriendReference = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(getUid()).child("buddies").child(postKey);
                mUserStorageRef = FirebaseStorage.getInstance().getReference().child(postKey);
                showProgressDialog();
                mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Log.d("List Dis BUTTON",dataSnapshot.toString());
                        if (dataSnapshot.getValue()==null){
                            //mAreWeFriends = false;
                        }else {
                            //mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                            if (dataSnapshot.getValue(Boolean.class)){
                                myView.mAddBuddyButton.setText("Buddy down");
                                myView.mAddBuddyButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                myView.mAddBuddyButton.setTextColor(Color.parseColor("#000000"));
                            }



                            FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (mFriend == null) {
                                        // User is null, error out
                                        Log.e("FriendsTAB", "User is unexpectedly null");
                                        Toast.makeText(getActivity(),
                                                "Error: could not fetch user.",
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        myView.bindUser(user);
                                        if(!user.getProfilePic().equals("none")) {
                                            Glide.with(getActivity() /* context */)
                                                    .using(new FirebaseImageLoader())
                                                    .load(mUserStorageRef)
                                                    .into(myView.mProfilePic);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("FriendsTAB", "getUser:onCancelled", databaseError.toException());
                                }
                            });
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

                FriendProfileHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), ExpandedProfileActivity.class);
                        intent.putExtra("KEY", postKey);
                        startActivity(intent);
                    }
                });

                FriendProfileHolder.mAddBuddyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFriendReference = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(getUid()).child("buddies").child(postKey);
                        showProgressDialog();
                        mFriendReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                Log.d("List Dis BUTTON",dataSnapshot.toString());
                                if (dataSnapshot.getValue()==null){
                                    mDatabase.child("users").child(getUid()).child("buddies").child(postKey).setValue(true);
                                    mDatabase.child("users").child(postKey).child("buddies").child(getUid()).setValue(true);
                                    myView.mAddBuddyButton.setText("Buddy down");
                                    myView.mAddBuddyButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                    myView.mAddBuddyButton.setTextColor(Color.parseColor("#000000"));
                                }else {
                                    //mAreWeFriends = dataSnapshot.getValue(Boolean.class);
                                    if (dataSnapshot.getValue(Boolean.class)){
                                        //mDatabase.child("users").child(getUid()).child("friends").child(postKey).setValue(false);
                                        mDatabase.child("users").child(getUid()).child("buddies").child(postKey).removeValue();
                                        mDatabase.child("users").child(postKey).child("buddies").child(getUid()).removeValue();
                                        myView.mAddBuddyButton.setText("Buddy up");
                                        myView.mAddBuddyButton.setBackgroundColor(Color.parseColor("#ff5a5f"));
                                        myView.mAddBuddyButton.setTextColor(Color.parseColor("#ffffff"));
                                    }else{
                                        mDatabase.child("users").child(getUid()).child("buddies").child(postKey).setValue(true);
                                        mDatabase.child("users").child(postKey).child("buddies").child(getUid()).setValue(true);
                                        myView.mAddBuddyButton.setText("Buddy down");
                                        myView.mAddBuddyButton.setBackgroundColor(Color.parseColor("#ffffff"));
                                        myView.mAddBuddyButton.setTextColor(Color.parseColor("#000000"));

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


    @Override
    public void onResume() {
        super.onResume();

        mDatabase.child("users").child(getUid()).child("buddies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Log.d("BUDDY",dataSnapshot.toString());
                }else{
                    OnlineUpdateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateView(){
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        mDatabase.child("users").child(getUid()).child("buddies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Log.d("BUDDY",dataSnapshot.toString());
                }else{
                    OnlineUpdateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
