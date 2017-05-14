package urmc.drinkingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Alessandro on 3/26/17.
 */

/**
 * ViewHolder to be used by the RecyclerView displaying all the images
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private ImageView mPhotoIV;
    private String mPath;
    private final Context context;

    //key to pass data to another activity
    public static final String KEY_PATH = "PATH";

    public PhotoViewHolder(View view){
        super(view);
        context = view.getContext();
        //wires the widget
        mPhotoIV = (ImageView)view.findViewById(R.id.image_view_view_holder);
        //onClickListener that starts the PhotoEnlargeActivity to display the pressed picture full screen
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity c = (AppCompatActivity)v.getContext();
                Intent intent = new Intent();
                intent.putExtra(KEY_PATH, mPath); //passes the path to this activity
                c.setResult(Activity.RESULT_OK, intent);
                c.finish();
            }
        });
    }

    //binds a picture to the view holder after scaling the picture
    public void bindPicture(String path){
        mPath = path;
        Bitmap photo = getScaledBitmap(path,100,100);
        mPhotoIV.setImageBitmap(photo);
        mPhotoIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //method to scale the pictures
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
