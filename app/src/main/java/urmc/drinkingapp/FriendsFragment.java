package urmc.drinkingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import urmc.drinkingapp.R;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
//fragment hosting the recycler view of users
public class FriendsFragment extends Fragment {

    //instance of the recylcer view
    private RecyclerView mRecyclerView;

    private DrinkingAppCollection mCollection;
    private FriendsAdapter mAdapter;

    private Context mContext;

    private String mQuery;

    //private NoResultsProcess mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public interface NoResultsProcess{
        void NoResultStarted();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);




        mContext = getActivity();

        //gets the database collection
        mCollection = DrinkingAppCollection.get(mContext);

        //sets up the recycler view
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_friends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //getting arguments being passed - email of the user to be displayed
        Bundle args = getArguments();
        if(args!=null){
            mQuery = args.getString("QUERY");
            UpdateUI(mQuery);
        }else {
            UpdateUI();
        }

        return view;
    }

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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mListener = (NoResultsProcess) context;
    }

}
