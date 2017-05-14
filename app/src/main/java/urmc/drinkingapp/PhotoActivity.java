package urmc.drinkingapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import mehdi.sakout.fancybuttons.FancyButton;
import urmc.drinkingapp.R;

/**
 * Activity hosting the photo fragment and a button to start the camera and take a picture
 */
public class PhotoActivity extends AppCompatActivity {

    private FancyButton mPhotoButton;
    private File mPhotoFile;
    int MY_CAMERA_REQUEST_CODE = 9;

    private PhotoFragment mPhotoFragment; //instance of the fragment containing the recyclerView for the photos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);



        mPhotoFragment = new PhotoFragment(); //initializes the photofragment

        //gets all the files from external storage as an array, coverts the array into a list and then calls a method in the fragment that convers the List of files
        //into a list of strings with the paths and also passes this information to the fragment.
        File picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] allPhotoFiles = picturesDirectory.listFiles();
        List<File> myFilesList = Arrays.asList(allPhotoFiles);
        mPhotoFragment.createFileList(myFilesList);

        //puts the existing fragment on screen using the fragment manager
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frame_layout_photo_grid, mPhotoFragment)
                .commit();

        //onClickListener in the takePhoto button that calls the method to take a photo
        mPhotoButton = (FancyButton) findViewById(R.id.take_picture_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(PhotoActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    takePicture(v);
                } else {
                    // Show rationale and request permission.
                    ActivityCompat.requestPermissions(PhotoActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.CAMERA &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    //method to take a photo launches the camera using an implicit intent and also configures the output to external storage
    public void takePicture(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String filename = "IMG_"+ UUID.randomUUID().toString()+".jpg";
        File picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mPhotoFile = new File(picturesDirectory, filename);

        Uri photoUri = Uri.fromFile(mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, 0);
    }

    //method to take a photo launches the camera using an implicit intent and also configures the output to external storage
    public void takePicture(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String filename = "IMG_"+ UUID.randomUUID().toString()+".jpg";
        File picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mPhotoFile = new File(picturesDirectory, filename);

        Uri photoUri = Uri.fromFile(mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, 0);
    }

    //adds the path of the recently taken picture to the List of paths in the fragment once the activity returns and updates the UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        mPhotoFragment.addPathToList(mPhotoFile.getPath());
        mPhotoFragment.UpdateUI();
    }
}
