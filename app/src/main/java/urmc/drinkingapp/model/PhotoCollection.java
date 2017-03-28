package urmc.drinkingapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro on 3/26/17.
 */

//class to store a collection of paths to the pictures stored in external storage
public class PhotoCollection {

    private List<String> mImageList; //list of paths
    private int size;

    //constructor that generates an empty list - used for testing before the app was able to get all the files from external storage
    public PhotoCollection(){
        mImageList = new ArrayList<>();
        size = 0;
    }

    //constructor currently in use that takes a list as an argument and assigns it to the list in the model
    public PhotoCollection(List<String> myList){
        mImageList = new ArrayList<>();
        mImageList = myList;
    }

    //adds a string (path) to the existing List
    public void addPath(String path){
        mImageList.add(path);
        size++;
    }

    //gets a specific path
    public String getPath(int position){
        return mImageList.get(position);
    }

    //returns the full list of paths
    public List<String> getCollection(){
        return mImageList;
    }
}
