package urmc.drinkingapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.database.DrinkingAppCollection;
import urmc.drinkingapp.model.User;

/**
 * Created by Alessandro on 3/10/17.
 */

/**
 * Buddy View Holder for a list of buddies --> NOT CURRENTLY BEING USED
 * The current implementation does not have a separate list for buddies but it was an idea
 * especially if the user is going to have multiple buddies
 */
//View holder for the recycler view displaying all Buddies
public class BuddyViewHolder extends RecyclerView.ViewHolder{

    //widgets
    public ImageView mProfilePic;
    public TextView mUserName;
    public FancyButton mAddBuddyButton;

    private User mUser;
    private DrinkingAppCollection mCollection;

    //constructor - wires up all the widgets
    public BuddyViewHolder(View view){
        super(view);
        mProfilePic = (ImageView)view.findViewById(R.id.image_view_profile_pic_friends_view_holder);
        mUserName = (TextView)view.findViewById(R.id.text_view_friend_name_friends_view_holder);
        mAddBuddyButton = (FancyButton) view.findViewById(R.id.button_add_buddy_view_holder);

        final AppCompatActivity c = (AppCompatActivity)view.getContext();
        //mCollection = DrinkingAppCollection.get(c);

        //when an item in the recyclerView is pressed the ExpandedProfileActivity is launched
        /*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ExpandedProfileActivity.class);
                intent.putExtra("EMAIL", mUser.getEmail());
                c.startActivity(intent);
            }
        });*/
    }

    //bind a user to the viewHolder
    public void bindUser(User user){
        mUser = user;

        /*
        //set up the picture
        String mPath = user.getProfilePic();
        if (!mPath.matches("none")){
            Bitmap photo = User.getScaledBitmap(mPath, 200, 200);
            mProfilePic.setImageBitmap(photo);
            mProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }*/

        //set all the other attributes
        mUserName.setText(user.getFullname());

    }
}
