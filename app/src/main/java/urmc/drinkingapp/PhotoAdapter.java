package urmc.drinkingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Alessandro on 3/26/17.
 */
//Adapter to be used to display a RecyclerView with all the pictures
public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder>{


    private List<String> mAdapterImageList;

    //constructor gets a list and sets it to the list in the adapter
    public PhotoAdapter(List<String> images){mAdapterImageList = images;}

    //Creates a new ViewHolder
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.photo_view_holder, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        return holder;
    }

    //Binds a new ViewHolder to a specific image using the method in the viewHolder
    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bindPicture(mAdapterImageList.get(position));
    }

    //gets the number of items in the list
    @Override
    public int getItemCount() {
        return mAdapterImageList.size();
    }

}
