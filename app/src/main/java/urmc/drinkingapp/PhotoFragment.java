package urmc.drinkingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import urmc.drinkingapp.R;
import urmc.drinkingapp.model.PhotoCollection;

/**
 * PhotoFragment hosting the RecyclerView with all the pictures
 */
public class PhotoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PhotoCollection mPhotoCollection;
    private List<String> mFilesList;


    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_photo, container, false);

        //new instance of a photo collection
        mPhotoCollection = new PhotoCollection(mFilesList);

        //wires up the recycler view and sets the grid layout
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view_photos);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setHasFixedSize(true);


        //sets the adapter to the collection
        mRecyclerView.setAdapter(new PhotoAdapter(mPhotoCollection.getCollection()));

        return v;
    }

    //updates the UI setting a new adapter
    public void UpdateUI(){
        mRecyclerView.setAdapter(new PhotoAdapter(mPhotoCollection.getCollection()));
    }

    //adds a path to the collection
    public void addPathToList(String path){
        mPhotoCollection.addPath(path);
    }

    //converts a List of files into a List of Strings
    public void createFileList(List<File> theList){
        mFilesList = new ArrayList<>();
        int size = theList.size();
        String aPath = "";
        for(int i = 0; i<size;i++){
            aPath = theList.get(i).getPath();
            mFilesList.add(aPath);
        }
    }

}
