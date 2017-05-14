package urmc.drinkingapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Alessandro on 2/25/17.
 */

/**
 * Model to represent a User with all its attributes
 * Contains getters and setters for attributes
 */

//Object characterizing a user in the app
public class User {

    //attributes of a user
    private String mFullname;
    private String mPassword;
    private String mEmail;
    private String mProfilePic = "none";
    public Double Lat;
    public Double Lon;

    private UUID mID;
    public static boolean firstTime = true;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    //not currently being used
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", mEmail);
        result.put("fullname", mFullname);
        result.put("profilePic", mProfilePic);
        result.put("Lat", Lat);
        result.put("Lon", Lon);

        return result;
    }

    /*
    //constructor
    public User(){
        mID = UUID.randomUUID();
        mProfilePic = "none";

    }*/

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


}


