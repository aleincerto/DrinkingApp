# DrinkingApp
README
Botcus App

First of all, if you have any questions when going over this app and how things work please contact me and I would be happy to explain anything to you. My email is aless.incerto@gmail.com. Additionally, you're going to need access to the Firebase database so I need to authorize you. 

The Botcus App uses 4 external libraries that you should review in case you encounter something that doesn't look like the Android Standard.

	For Circle ImageViews: https://github.com/hdodenhof/CircleImageView
	For Fancy Buttons: https://github.com/medyo/Fancybuttons
	For a cool slider: https://github.com/MAXDeliveryNG/slideview
	For Graphing tool: http://www.android-graphview.org/

Furthermore, when developing this app you should be able to connect to use version control with gitHub. The link for the app is: https://github.com/aleincerto/DrinkingApp

The app utilizes an online database thanks to the Firebase framework. Please refer to its documentation online for more detail explanation about the framework: https://firebase.google.com/docs/android/setup?hl=en-419

I will provide some examples but I won't be able to cover all details.

How the database is structured:
	The Firebase database is structured using a JSON tree and this is the structure that I created:
	App 
		--> users (each user is identified by its authentication key)
				 --> UID
						--> User object with all attributes +
							buddy (UID of a buddy) +
							lat +
							lon +
							friends
									--> UID of friend & boolean value

The firebase documentation provides good instructions on how to use all the framework features but I will provide some examples specific to this app:

Initialize reference to the database:
```
	 // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    // [START initialize_database_ref]
    mDatabase = FirebaseDatabase.getInstance().getReference()
    // [END initialize_database_ref]
```


Read from the database:
```
// [START single_value_read]
        final String userId = getUid(); //get UID from current user
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        mUser = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (mUser == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //do something with the user stored at mUser
                        }

                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
```


Write to the database: you can modify specific attributes or substitute the whole User object for a new one. I recommend changing specific attributes because the attributes in the database not included in the object (like the friends branch) will get deleted if you include a brand new object.
```
	//Substituting whole user object
	mDatabase.child("users").child(userId).setValue(mUser);

	//Writing specific value
	mDatabase.child("users").child(userId).child("fullname").setValue(mUser.getFullname());
```	

The Firebase database also has implementations to read several values and display them in a Recycler View. If you want to do this please refer to the MyFriendsTabFragment or FriendsFragment where you can see some examples

Storage:

The Firebase framework also provides functionality to have online storage. The profile pictures of the users are being stored in there. Please refer to the Firebase website for detailed documentation, I will be providing some examples.

Initialize reference to the storage:
```
	private StorageReference mStorageRef;
	private StorageReference mUserStorageRef; 
	//I'm using a separate instance for the reference to the current user but it doesn't have to be two separate instances

	mStorageRef = FirebaseStorage.getInstance().getReference();
	mUserStorageRef = mStorageRef.child(getUid());
```

Read from the storage: Refer to loadPic() method in OnlineEditProfile as an example
```
    private void loadPic(){
        Glide.with(getActivity() /* context */)
                .using(new FirebaseImageLoader())
                .load(mUserStorageRef)
                .into(mProfilePicImageView);
    }
```
Write to the storage - upload new picture: Refer to uploadPic() method in OnlineEditProfile as an example

```
    private void uploadPic(){
        try {
            InputStream stream = new FileInputStream(new File(mPath)); //mPath represents the path to the image
            UploadTask uploadTask = mUserStorageRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
        catch (Exception e){
            Log.d("ONLINEEDIT","File not found");
        }
    }
```

The Firebase Framework also provides authentication functionality that is being utilized. Only the email and password option was implemented so in the future the social media sign in should be added, Firebase also has that capacity. For an explanation about authentication please refer to the firebase documentation as it is very straight forward and there is nothing special that was added when implemented in the app.

The external libraries being utilized also provide documentation, so if you have doubts, especially with the graph library please refer to the links provided at the beginning. The rest of the App should have standard Android Implementations but if you have questions please contact me or read the comments I wrote on the code. 

Finally, I'm going to provide some suggestions regarding the things that need to be improved and implemented

	Permissions: The app requests permissions at different points for different features, these should not be too annoying to the user, confirm that this is the case. Moreover, after a permission is granted the app should go and implement whatever feature it was asking for permission. However, for some reason this first reaction after permission has been granted is not as good as when the user accesses the feature already with permission. Therefore, whenever you're testing something with permission try to do it multiple times as the first time the permission is being asked the functionality won't work as well. You can see this happening with Maps and tracking and with Drunk Texting graph. Clearly, this is something that needs to be improved.

	Settings: The user should be able to configure the app to his own taste, especially with the permissions aspect. If the user doesn't grant permission to something the app should work fine anyway. This whole settings area needs to be implemented.

	Tonight's Settings: The user has the capacity to preset certain information to be used in drunk mode. Maybe this should be available from the main screen and not from drunk mode. Additionally, a more comprehensive configuration can be provided. If the user has a buddy set then maybe some preset options about the buddy should be displayed.

	Friend Requests: Currently there is no approval or disapproval of friends, one important thing to implement is that a user should not be able to just add a friend, this need to be a two side process. The user needs to send a friend request and this needs to be approved (like Facebook). This implies that the app should send notifications. Firebase has the feature of instant messaging, maybe this can be used to send notifications like friend requests. The same thing applies to the buddy system, there needs to be approval.

	Authentication: Facebook, Twitter and Google Plus authentication should be implemented. Firebase has this feature. Currently, the authentication is done through email and password only.

	Search Results: When searching for a user the spelling has to match, even the first letter needs to be CAPS in order to get a match. This needs to be improved. Additionally, when there are no results some message should be displayed to the user. Check the no result dialog that was implemented by not included in the current version.

	Buddy System: A better way to add buddies and display your buddies should be implemented. Additionally, the feature is currently a little bit buggy so more extensive testing and improvement should be done. Potentially the buddy system can be extended and allow more than one buddy at a given time.

	Drunk Texting behavior: Currently the app analyzes the drunk texting behavior by looking at the SMSs that have been sent by the user. Maybe the user could have the option to have his drunk tweeting behavior analyzed.

	Graphs: In the current version the app will try to plot the analysis from all text messages that have ever been sent by the user. This should be limited to a shorter period of time. IMPORTANT: when testing this feature make sure you have some drunk text messages because otherwise there will not be any interesting information being displayed in the graph. A text message that will be marked as drunk is "drunk drunk drink" so try that, or use the code I commented out in the readTexts() function in the MainActivity for more testing.

	Transitions: The movement throughout the app should be reviewed to try to give to the user the best transitions from one activity to the other. Especially when going to drunk mode maybe the user should not be able to go back unless it slides drunk mode off. Furthermore, when the user is in tonight's settings if the user clicks back he should be taken still to drunk mode and not the main screen.

	Landscape Mode: The app was developed entirely to work in Portrait Mode. Landscape mode should be considered, either to block the possibility of turning the device horizontally or implement the UI in landscape.

	Save Instance State: Only a few OnSaveInstanceState were implemented. Before released into production the app should have a more comprehensive implementation of this.dfd

	Testing: Finally, more throughout testing should be made, especially with real devices because this app has only being tested using emulators

	Firebase: Before going into production please review the Firebase limits before starting to pay. This framework is free until a certain point and if the app gets popular and it is expected to cross this threshold then some money needs to be put into this framework. 



