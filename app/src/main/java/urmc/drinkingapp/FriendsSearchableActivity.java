package urmc.drinkingapp;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendsSearchableActivity extends AppCompatActivity implements FriendsFragment.NoResultsProcess {

    public void NoResultStarted(){
        finish();
    }


    //instance of the fragment
    private FriendsFragment mFriendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_searchable);



        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
            Intent i = new Intent();
            i.putExtra("QUERY", query);
            Bundle extras = i.getExtras();
            mFriendsFragment = new FriendsFragment();
            mFriendsFragment.setArguments(extras);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.activity_friends_searchable, mFriendsFragment)
                    .commit();
        }
    }


    //Updates the UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFriendsFragment.UpdateUI();
    }
}
