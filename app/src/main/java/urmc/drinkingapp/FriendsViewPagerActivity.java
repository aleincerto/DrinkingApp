package urmc.drinkingapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * View pager activity that contains two tabs: one for the fullscreenSearchFragment to perform a search for a user and another to display
 * all the user's friends
 */
public class FriendsViewPagerActivity extends AppCompatActivity {

    private static final String TAG = "ViewPagerActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_view_pager);



        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new FullScreenSearchTabFragment(),
                    new MyFriendsTabFragment(),
                    //new FriendsBuddyTabFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_search_friends),
                    getString(R.string.heading_friends),
                    //getString(R.string.heading_buddies)
            };
            @Override
            public Fragment getItem(int position) {
                /*
                if (mFragments[position] instanceof MyFriendsTabFragment){
                    ((MyFriendsTabFragment)mFragments[position]).updateView();
                }else if (mFragments[position] instanceof FriendsBuddyTabFragment){
                    ((FriendsBuddyTabFragment)mFragments[position]).updateView();
                }*/
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }

            /*@Override
            public int getItemPosition(Object object){
                if (object instanceof MyFriendsTabFragment){
                    ((MyFriendsTabFragment)object).updateView();
                }else if (object instanceof FriendsBuddyTabFragment){
                    ((FriendsBuddyTabFragment)object).updateView();
                }
                return super.getItemPosition(object);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }*/
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
