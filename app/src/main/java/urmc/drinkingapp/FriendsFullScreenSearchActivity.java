package urmc.drinkingapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

/**
 * NOT CURRENTLY BEING USED. Activity displaying a SearchView to search for users on the database
 * The fragment version (FullScreenSearchTab) is being used instead to be able to include it in the FriendsViewPager
 */
public class FriendsFullScreenSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_full_screen_search);

        Log.d("FULLSCREEN SEARCH","ON CREATE");

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.search_view_friends);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    }

    @Override
    public boolean onSearchRequested() {
        this.finish();
        return super.onSearchRequested();
    }

    /*
    @Override
    public void onBackPressed() {
        Intent i = new Intent(FriendsFullScreenSearchActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
    */
}
