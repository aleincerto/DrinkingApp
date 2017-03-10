package urmc.drinkingapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.UUID;

/**
 * Created by Alessandro on 2/25/17.
 */

//Object characterizing a user in the app
public class User {

    //attributes of a user
    private String mFullname;
    private String mPassword;
    private String mEmail;
    private String mProfilePic;

    private UUID mID;
    public static boolean firstTime = true;

    //constructor
    public User(){
        mID = UUID.randomUUID();
        mProfilePic = "none";

    }

    /* Getters and Setters*/

    public User(UUID id){
        mID = id;
    }

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String mFullname) {
        this.mFullname = mFullname;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public void setProfilePic(String mProfilePic) {
        this.mProfilePic = mProfilePic;
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID mID) {
        this.mID = mID;
    }

    //method to fix pictures to be displayed in the app
    public static Bitmap getScaledBitmap(String path, int width, int height) {

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

}


