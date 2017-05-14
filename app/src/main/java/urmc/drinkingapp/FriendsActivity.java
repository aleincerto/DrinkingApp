package urmc.drinkingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Class hosting the FriendsFragment - NOT CURRENTLY BEING USED
 */
public class FriendsActivity extends AppCompatActivity {

    private FriendsFragment mFragment;
    private ImageButton mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);


        mSearch = (ImageButton) findViewById(R.id.image_button_search_friends_activity);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsActivity.this.onSearchRequested();
            }
        });

        //sets the UserFragment
        mFragment = new FriendsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frame_layout_activity_friends, mFragment)
                .commit();
    }

    //Updates the UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mFragment.UpdateUI();
        mFragment.OnlineUpdateUI();
    }
}
