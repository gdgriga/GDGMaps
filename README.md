GDG Maps
=========
## Introduction
Welcome to yet another GDG Riga event. Today we're going to develop an Android app that will allow the user to view his photos on the map and add location tags to them. The end result is going to look something like this:
![device-2016-01-20-225127](https://cloud.githubusercontent.com/assets/5235166/12517797/618d8514-c13d-11e5-8362-c311d31462da.png)

## Prerequisites
* Java 7 JDK
* An Android device (v17 or higher, optional)
* Android Studio 2.0 (or higher) with SDK v22 (+ appcompat for the version of your device) and Google Play Services installed.

## Setting Up the Android Studio
* Download [Android Studio IDE 2.0 Preview](http://tools.android.com/download/studio/canary/latest)
* Follow the [instructions](http://developer.android.com/sdk/installing/index.html?pkg=studio) to get started
* Install the Android SDK through the Android Studio's (AS) wizard
* Install the Android 5.1.1 (API v22) SDK platform
* Import the project into the AS
* Get ready to code

## Setting Up the Emulator (Optional)
* Skip this step if you're going to develop the application using an Android device
* Open the Android Virtual Devices (AVD) Manager
* Download the API v22 (Lollipop) system image (with Google APIs) for your platform
* [Download](https://drive.google.com/open?id=0BwU6hY4pw6KyNFFDM3UyUWE1UDg) the SD card image containing the test photo set
* Click on **Show Advanced Settings** and under **Memory and Storage** tab specify the path to the unzipped SD card image (external file)

## Sanity Check
First things first, let's launch the app. A blank screen should appear.

## Configuring the Dependencies
Now let's add the necessary dependencies and configure the project.
Start by adding a compile-time dependency on Google Play Services inside *app/build.gradle*
```groovy
compile 'com.google.android.gms:play-services:7.3.0'
```
And adding the following entry to the manifest as a child of ``<application>`` node
```xml
<meta-data
    android:name="com.google.android.gms.version"
    android:value="@integer/google_play_services_version"/>
```

## Adding the Key
Now, let's add the API key to the resources file (app/src/main/res/values/strings.xml). Either generate the key on your own or get [this](https://docs.google.com/document/d/1UzgXOyp_qwItbj-qq2moL75kTlg7Q0gx2iU0bC9Ld48/view) one. Add the key as a string resource with name *googleApiKey*. Then, add it to the manifest
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="@string/googleApiKey"/>
```
We're done with the configuration. Let's launch the app and observe the void once more (this is the last time, I promise).

## Plumbing
Now it's time to get dirty with code. Let's start by setting up the supporting infrastructure. Since we will be working with the geographical data, we need some representation of it. Google API provides the class [*LatLng*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/LatLng) to represent the geographical location in terms of point's coordinates (latitude and longitude). We'll be using this class throughout our application.

The first thing we'll do is open the TODO tab in Android Studio and go through each item in the list.

Let's start with *lv.gdgriga.gdgmaps.Photo*. As you can see it has two fields: a file name and a thumbnail. Another useful field to add would be the location where the photo was taken. Let's also add a method to check whether photo has location and call it *hasLocation*. This is how the class will look like after these additions:
```java
public class Photo {
    public static final Photo EMPTY = new Photo();
    public String fileName;
    public Bitmap thumbnail;
    public LatLng location;

    public boolean hasLocation() {
        return location != null;
    }
}
```
Next, let's move over to *lv.gdgriga.gdgmaps.PhotoLoader*. It has two methods we need to modify (the ones with TODOs in them). The modified methods should look something like the ones below:
```java
private static Photo fromExifInterface(ExifInterface exif) {
    Photo photo = new Photo();
    photo.location = getLocationFrom(exif);
    if (exif.hasThumbnail()) {
        photo.thumbnail = getThumbnailFrom(exif);
    }
    return photo;
}

private static LatLng getLocationFrom(ExifInterface exif) {
    float[] latLong = new float[2];
    if (!exif.getLatLong(latLong)) {
        return null;
    }
    logTags(exif);
    return new LatLng(latLong[0], latLong[1]);
}
```
Next stop is *lv.gdgriga.gdgmaps.photo_view.PhotosWithLocation*. You should know the drill by now. Modify the code as indicated by the TODO. The result should resemble the following:
```java
if (photo != Photo.EMPTY && photo.hasLocation()) {
    photos.add(photo);
}
```
The last stop is *lv.gdgriga.gdgmaps.tag.StoreTagTask*. Get the latitude and the longitude from the Photo object:
```java
private void storeLocationTag(Photo photo) throws IOException {
    ExifInterface exif = new ExifInterface(photo.fileName);
    double latitude = photo.location.latitude;
    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(Coordinate.fromDecimalDegrees(latitude)));
    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude < 0 ? "S" : "N");
    double longitude = photo.location.longitude;
    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(Coordinate.fromDecimalDegrees(longitude)));
    ...
 }
```
Run the app and check that you have no compilation errors. You have arrived at your destination!

## Building the Photo View
Now that we're done warming up, let's start actually building our app. The app is going to have two views. One for photo display and the other for tagging. We'll start with the display one.

Start by opening **PhotoViewActivity** and overriding the [*onCreate*](http://developer.android.com/reference/android/app/Activity.html#onCreate%28android.os.Bundle%29) method of the [**Activity**](http://developer.android.com/reference/android/app/Activity.html) class. Inside it, we're going to call the [*onCreate*](http://developer.android.com/reference/android/app/Activity.html#onCreate%28android.os.Bundle%29) from the parent, [*set the content view*](http://developer.android.com/reference/android/app/Activity.html#setContentView%28int%29) to *R.layout.photo_view_activity* and attach the PhotoViewMapFragment.

Before we do that, let's move over to PhotoViewMapFragment and make it extend [**MapFragment**](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment), like this:
```java
public class PhotoViewMapFragment extends MapFragment {
}
```
Now, go back to PhotoViewActivity and attach the fragment using the [Fragment Manager](http://developer.android.com/reference/android/app/FragmentManager.html). To do that, get the Fragment Manager for the view by calling [*getFragmentManager*](http://developer.android.com/reference/android/app/Activity.html#getFragmentManager%28%29), begin a transaction by calling [*beginTransaction*](http://developer.android.com/reference/android/app/FragmentManager.html#beginTransaction%28%29), [*add*](http://developer.android.com/reference/android/app/FragmentTransaction.html#add%28int,%20android.app.Fragment%29) a new instance of the **PhotoViewMapFragment** using the id *R.id.map_container* and [*commit*](http://developer.android.com/reference/android/app/FragmentTransaction.html#commit%28%29) the transaction. When done, here's how PhotoViewActivity will look like:
```java
public class PhotoViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_activity);
        attachMapFragment();
    }

    private void attachMapFragment() {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, new PhotoViewMapFragment())
                            .commit();
    }
}
```
Run the app and you should see the map (finally!)

![device-2016-01-22-233239](https://cloud.githubusercontent.com/assets/5235166/12523593/7b581068-c160-11e5-9bf0-9162ee956cde.png)

As you can see, adding a map is very easy. The basic map has pretty much all the functionality you would expect from a map. But let's change things a bit and configure the map the way we want to see it.

### Getting an Instance of GoogleMap
We are going to need an instance of [**GoogleMap**
](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap), so let's get it. Inside **PhotoViewMapFragment**, start by overriding the [*onStart*](http://developer.android.com/reference/android/app/Activity.html#onStart%28%29) method. Inside the method, be sure to call [*onStart*](http://developer.android.com/reference/android/app/Activity.html#onStart%28%29) in parent and [*getMapAsync*](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment.html#getMapAsync%28com.google.android.gms.maps.OnMapReadyCallback%29) which will set up the map for the fragment. You need to pass an instance of [**OnMapReadyCallback**](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback) to [*getMapAsync*](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment.html#getMapAsync%28com.google.android.gms.maps.OnMapReadyCallback%29). When the map is ready, it'll be passed to the [*onMapReady*](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback.html#onMapReady%28com.google.android.gms.maps.GoogleMap%29) method of the callback. Inside [*onMapReady*](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback.html#onMapReady%28com.google.android.gms.maps.GoogleMap%29), the first thing we'll do is save the map in the field of **PhotoViewMapFragment**. Run the app and make sure it's still working. We haven't changed anything yet, so the map should look the same. This is how **PhotoViewMapFragment** could look like:
```java
public class PhotoViewMapFragment extends MapFragment {
    private GoogleMap map;

    OnMapReadyCallback onMapReady =  new OnMapReadyCallback(){
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }
}
```

### Setting the Default Location
As you have noticed, the map is focused somewhere on the Equator, which probably is a nice place to be, but let's set the default location for the map to somewhere more familiar.

To change the location, we're going to use the [**CameraUpdateFactory**](https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdateFactory). The factory will build the desired [**CameraUpdate**](https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdate) object which, in turn, we'll pass to the [*moveCamera*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#moveCamera%28com.google.android.gms.maps.CameraUpdate%29) method of the [**GoogleMap**](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap) object. To move the camera to the desired location, which will be Riga, Latvia, we'll use the [*newLatLngZoom*](https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdateFactory.html#newLatLngZoom%28com.google.android.gms.maps.model.LatLng,%20float%29) method of the [**CameraUpdateFactory**](https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdateFactory). Before we do that, let's add a constant field which we'll name **RIGA** to **lv.gdgriga.gdgmaps.Location**. Like this:
```java
public class Location {
    public static final float DEFAULT_ZOOM = 13;
    public static final LatLng RIGA = new LatLng(56.948889, 24.106389);
}
```
To build the [**CameraUpdate**](https://developers.google.com/android/reference/com/google/android/gms/maps/CameraUpdate), we'll pass the coordinates for Riga and the default zoom to *newLatLngZoom*. When we're done, *onMapReady* will look like this:
```java
private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
    }
};
```
Run the app, now the map should focus on Riga.
![device-2016-01-23-002237](https://cloud.githubusercontent.com/assets/5235166/12524685/73361d06-c167-11e5-9c58-236840d3dc9b.png)

### Displaying the Photos
It's time to display the photos! To do that, we'll add a *drawPhotos* method which we'll call from inside *onMapReady* of the **OnMapReadyCallback**.

The first thing we'll do inside *drawPhotos* is clear the map by using the [*clear*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#clear%28%29) method of the map object. Then we'll iterate over the list of photos in the gallery that have location tags embedded in them. Don't worry, the list was already prepared for you and all you need to get it is to call **PhotosWithLocation**.*list()* passing in an instance of [**Context**](http://developer.android.com/reference/android/content/Context.html).

To get the context from the **PhotoViewMapFragment**, you first need to get the activity to which the fragment is attached to,  then get the application context from it. You can do it by calling [*getActivity*](http://developer.android.com/reference/android/app/Fragment.html#getActivity%28%29) and [*getApplicationContext*](http://developer.android.com/reference/android/content/ContextWrapper.html#getApplicationContext%28%29) respectively.

To add a photo to the map we are going to need to make a marker of it first. To do that, we'll convert the photo's thumbnail into a [**BitmapDescriptor**](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptor) by calling [**BitmapDescriptorFactory**.*fromBitmap*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html#fromBitmap%28android.graphics.Bitmap%29) and add it as an icon to the marker.

To add a marker to the map, we first need to create an instance of [**MarkerOptions**](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions) builder class. The marker attributes are set through that instance which, in turn, is attached to the map. The marker attribute that we're going to set are position, icon, anchor and snippet.

The position is set through [*position*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions.html#position%28com.google.android.gms.maps.model.LatLng%29) method of **MarkerOptions**. Get the position from the photo's *location* field.

To set the icon, use the [*icon*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions.html#icon%28com.google.android.gms.maps.model.BitmapDescriptor%29) method. Pass in the **BitmapDescriptor** instance we created from the thumbnail.

By default, the anchor of a bitmap is located in the middle of it's left side, which is kind of weird. Let's fix it by setting the anchor to be dead center by calling [*anchor*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions#anchor%28float,%20float%29) with both of the parameters equal to *0.5* (remember that counting starts from the top left corner).

We'll also store the file name of the photo in the snippet field of the options by using the [*snippet*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions.html#snippet%28java.lang.String%29) method (which is a hack and you probably shouldn't do it in a production-scale app, but it's fine for our workshop, the snippet won't be displayed if the title is not set). We are going to need the stored file name later. Get the file name from *fileName* field of the Photo object.

To add the newly-populated **MarkerOptions** to the map, call the [addMarker](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#addMarker%28com.google.android.gms.maps.model.MarkerOptions%29) on the map. Do it inside the photo-iterating loop. Here's the whole **PhotoViewMapFragment** after the changes:
```java
public class PhotoViewMapFragment extends MapFragment {
    private GoogleMap map;

    private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
            drawPhotos();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    private void drawPhotos() {
        map.clear();
        for (Photo photo : PhotosWithLocation.list(context())) {
            map.addMarker(toMarker(photo));
        }
    }

    private Context context() {
        return getActivity().getApplicationContext();
    }

    private MarkerOptions toMarker(Photo photo) {
        BitmapDescriptor icon = photo.thumbnail != null ?
                BitmapDescriptorFactory.fromBitmap(photo.thumbnail) :
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        return new MarkerOptions().position(photo.location)
                                  .icon(icon)
                                  .anchor(0.5f, 0.5f)
                                  .snippet(photo.fileName);
    }
}
```
As you've noticed, we've added a fallback for the marker icon if a photo has no thumbnail. Check [**BitmapDescriptor**](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptor) out for more options.

Run the app, it should display your photos. If not, that probably means that none of your photos have Geo tags in them. We're going to fix this by implementing our own photo-tagging solution. Read on!

![device-2016-01-23-005253](https://cloud.githubusercontent.com/assets/5235166/12525335/2b572e9e-c16c-11e5-9da6-790c1ce45a83.png)

## Building the Tagging View
We need some way to tag or photos so that we can display them on our map. Let's create a view to do just that! We'll start by modifying **TagMapFragment** so that it extends [MapFragment](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment):
```java
public class TagMapFragment extends MapFragment {
}
```
Then we'll modify **TagActivity** to override the [onCreate](http://developer.android.com/reference/android/app/Activity.html#onCreate%28android.os.Bundle%29) method of the [Activity](http://developer.android.com/reference/android/app/Activity.html) class. Just like the last time, we'll call the parent's onCreate. We'll also set the content view to *R.layout.tag_actiivty* and attach an instance of the **TagMapFragment** to the view, but this time we'll also store it in a field:
```java
public class TagActivity extends Activity {
    TagMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_activity);
        mapFragment = new TagMapFragment();
        attachFragment(mapFragment);
    }

    private void attachFragment(TagMapFragment tagMapFragment) {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, tagMapFragment)
                            .commit();
    }
}
```

## Building the Tagging Fragment
In **TagMapFragment**, we'll override [onStart](developer.android.com/reference/android/app/Fragment.html#onStart()) inside which we'll call the parent's on start and [getMapAsync](https://developers.google.com/android/reference/com/google/android/gms/maps/MapFragment.html#getMapAsync%28com.google.android.gms.maps.OnMapReadyCallback%29). Unsurprisingly, to *getMapAsync*, we'll pass an instance of [OnMapReadyCallback](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback). Inside the callback's *onMapReady* method, we'll store the map that is passed in as a field of **tagMapFragment**. Then, just as in the **PhotoViewMapFragment**, we'll move the camera to Riga.
```java
public class TagMapFragment extends MapFragment {
    private GoogleMap map;

    OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            focusOnRiga();
        }

        private void focusOnRiga() {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }
}
```

## Picking a Photo
We need some kind of mechanism to initiate tagging, so let's setup a button to do that. The button will be located in the
**PhotoViewActivity**. The button is already present in the layout, so let's set it up. To get the button from layout, call [findViewById](http://developer.android.com/reference/android/app/http://developer.android.com/reference/android/view/View.OnClickListener.htmlActivity.html#findViewById%28int%29) passing in the id of the button which is *R.id.bottom_button* and cast the result to [Button](http://developer.android.com/reference/android/widget/Button.html). Then, set button text by calling [setText](http://developer.android.com/reference/android/widget/TextView.html#setText%28int%29) specifying the resource id *R.string.tag_photo*. Finally, set the listener for the click event. To do that, call [setOnClickListener](http://developer.android.com/reference/android/view/View.html#setOnClickListener%28android.view.View.OnClickListener%29) and pass in an instance of [View.OnClickListener](http://developer.android.com/reference/android/view/View.OnClickListener.html). Inside the listener's *onClick* method, we'll build an intent to pick a photo for tagging. To find out more about intent's, please refer to the **Useful Links** section. To build an intent, create an instance of [Intent](http://developer.android.com/reference/android/content/Intent.html) specifying the [ACTION_PICK](http://developer.android.com/reference/android/content/Intent.html#ACTION_PICK) action. Then, set the intent type to "image/jpg" by using the [setType](http://developer.android.com/reference/android/content/Intent.html#setType%28java.lang.String%29) method. To fire the intent, pass it to [startActivityForResult](http://developer.android.com/reference/android/app/Activity.html#startActivityForResult%28android.content.Intent,%20int%29). The method requires the second parameter - requestCode which is an int. This could be any integer greater than zero. It will be passed in with the intent result when the intent completes so that we can be sure that we are reacting to the expected intent. This is **PhotoViewActivity** after the modifications:
```java
public class PhotoViewActivity extends Activity {
    private static final int PICK_PHOTO_CODE = 0xACE_F070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_activity);
        attachMapFragment();
        setUpButton();
    }

    private void attachMapFragment() {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, new PhotoViewMapFragment())
                            .commit();
    }

    private void setUpButton() {
        Button tagButton = (Button) findViewById(R.id.bottom_button);
        tagButton.setText(R.string.tag_photo);
        tagButton.setOnClickListener(onTagButtonClick());
    }

    private View.OnClickListener onTagButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPickPhoto = new Intent(Intent.ACTION_PICK);
                toPickPhoto.setType("image/jpg");
                startActivityForResult(toPickPhoto, PICK_PHOTO_CODE);
            }
        };
    }
}
```

## Processing the Pick Result
We've setup the button in PhotoViewActivity to create an intent to pick a photo from the image gallery. When the user will select the photo he wants to tag, the uri of the photo file will be returned as the result. But to get the result, we'll need to listen for it. To do that, we'll override the Activity class's method [*onActivityResult*](http://developer.android.com/reference/android/app/Activity.html#onActivityResult%28int,%20int,%20android.content.Intent%29). The first thing to do inside the method is, as with any other overridden **Activity** method, is to call the method on *super*. [*onActivityResult*](http://developer.android.com/reference/android/app/Activity.html#onActivityResult%28int,%20int,%20android.content.Intent%29) will be invoked by the system when the photo is picked. The system will three arguments in. The first is the request code. We should check whether the request code that we get corresponds to the request code that we started the intent with. If it doesn't, we'll return immediately since there's not much sense in reacting to intents we don't expect. The second argument is the result code. We'll need to check whether it is [**RESULT_OK**](http://developer.android.com/reference/android/app/Activity.html#RESULT_OK). If it isn't, we'll also terminate the execution, since other codes can only mean that something unexpected happened during the intent's execution or it was simply canceled by the user. In either case, we won't get the data that we are waiting for so there's no sense in continuing. When both the request code and the result code are **PICK_PHOTO_CODE** and [**RESULT_OK**](http://developer.android.com/reference/android/app/Activity.html#RESULT_OK) respectively, we'll proceed with the third argument - the intent which yield the uri to the photo file. To get the uri we'll call [*getData*](http://developer.android.com/reference/android/content/Intent.html#getData%28%29) on the intent.
OK, we've got our uri, now what? Well, the things aren't as straightforward as you would think because the uri that we got isn't the path to the file on disk but an internal identifier which we'll have to resolve first. Sounds scary, but don't worry, all the hard work was already done for you. All you need to get the path from the uri is to create an instance of **PathFromUriResolver** by calling fromContext on the class passing in the current context (which, if you remember, you can get by calling [*getApplicationContext*](http://developer.android.com/reference/android/content/ContextWrapper.html#getApplicationContext%28%29)) and calling *resolve* on the instance passing in the uri. It will return the path to the file that we can work with.
We've got the photo the user wants to tag, so let's start taggin'! The thing we want to do here is to open our TagActivity that will handle the tagging process. To open an activity from another activity, we need to create an [Intent](http://developer.android.com/reference/android/content/Intent.html). This time around, we won't use the constructor that accepts the action string, but the one that accepts a [**Context**](http://developer.android.com/reference/android/content/Context.html) and a class of the activity we want to open. Remember that we want to open the **TagActivity**. **Activity** extends [**Context**](http://developer.android.com/reference/android/content/Context.html), so you can just pass *this* as the first argument. To pass the path of the photo to **TagActivity**, we'll put it inside the intent as an extra. *Extra* is what Android architects called the payload that could be added to an intent. To add extra to the intent, call [*putExtra*](http://developer.android.com/reference/android/content/Intent.html#putExtra%28java.lang.String,%20java.lang.CharSequence%29) on it. Extra should have a name, you can use the string contained in the resource *R.string.photoPath*. To get a string from a string resource, use [*getString*](http://developer.android.com/reference/android/content/Context.html#getString%28int%29). Finally, to start the **TagActivity**, call [*startActivity*](http://developer.android.com/reference/android/app/Activity.html#startActivity%28android.content.Intent%29) with the intent we've just created. Now, when user picks a photo from the gallery, **TagActivity** will launch. Here's the bit that sums it all up:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode != PICK_PHOTO_CODE) return;
    if (resultCode != RESULT_OK) return;
    Uri photoUri = intent.getData();
    String photoPath = resolveToPath(photoUri);
    startTagActivity(photoPath);
}

private String resolveToPath(Uri photoUri) {
    return PathFromUriResolver.fromContext(getApplicationContext())
                              .resolve(photoUri);
}

private void startTagActivity(String photoPath) {
    Intent tagActivity = new Intent(this, TagActivity.class);
    String photoPathExtraName = getString(R.string.photoPath);
    tagActivity.putExtra(photoPathExtraName, photoPath);
    startActivity(tagActivity);
}
```

## Getting the Picked Photo
We've started the Tag Activity, but how do we get the path to the file that contains the photo to tag? To do that, we'll override [Activity](http://developer.android.com/reference/android/app/Activity.html)'s <*onStart*>. Inside it, we'll call the *super*'s onStart. Now, we can call [*getIntent*](http://developer.android.com/reference/android/app/Activity.html#getIntent%28%29) to get the intent with which the activity was started. Once we have our intent, we can get the extra we've stored in it by calling [*getStringExtra*](http://developer.android.com/reference/android/content/Intent.html#getStringExtra%28java.lang.String%29) passing in the name of the extra we want to retrieve. Remember, that we stored the path of the photo file under the name contained in *R.string.photoPath* (call [*getString*]([*getString*](http://developer.android.com/reference/android/content/Context.html#getString%28int%29)) to get the name). The path is ours! Let's load the photo and store it inside **TagMapFragment** (you need to create a setter for that). The further processing will be done inside **TagMapFragment**.
The code for **TagActivity** is below:
```java
@Override
protected void onStart() {
    super.onStart();
    setPhotoToTag();
}

private void setPhotoToTag() {
    Photo photoToTag = PhotoLoader.fromFile(photoPath());
    mapFragment.setPhotoToTag(photoToTag);
}

private String photoPath() {
    Intent toTagPhoto = getIntent();
    String extraName = getString(R.string.photoPath);
    return toTagPhoto.getStringExtra(extraName);
}
```

And here's **TagMapFragment**:
```java
public class TagMapFragment extends MapFragment {
    ...

    private Photo photo;

    ...

    public void setPhotoToTag(Photo photo) {
        this.photo = photo;
    }
}

```

## Drawing the Picked Photo
The photo is ready for tagging! Let's start by modifying [*onMapReady*](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback.html#onMapReady%28com.google.android.gms.maps.GoogleMap%29) method of the [**OnMapReadyCallback's**](https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback) instance inside **TagMapFragment**. The first thing we'll do is check whether the photo has location, if it does, we're going to add it as a marker to the fragment's map. Create the marker using [MarkerOptions](https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions), same as before. You can get the position for the marker from the photo's location field. To create the icon, use [**BitmapDescriptorFactory**](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html). If the photo has a thumbnail, load it using [*fromBitmap*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html#fromBitmap%28android.graphics.Bitmap%29). If it doesn't, use [*defaultMarker*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html#defaultMarker%28float%29) instead, select the color you prefer from one of the [**BitmapDescriptorFactory's**](https://developers.google.com/android/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html) constants. Finally, we'll focus on the photo by using the same mechanism as we used to focus on Riga (it probably makes sense to extract the logic into a separate method). Now we can pick a photo from the gallery. Here's **TagMapFragment** after the modifications:
```java
public class TagMapFragment extends MapFragment {
    private GoogleMap map;
    private Photo photo;

    private final OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            if (photo.hasLocation()) {
                addPhotoMarker(photo);
                focusOnLocation(photo.location);
            } else {
                focusOnLocation(Location.RIGA);
            }
        }

        private void focusOnLocation(LatLng location) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Location.DEFAULT_ZOOM));
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    public void setPhotoToTag(Photo photo) {
        this.photo = photo;
    }

    private void addPhotoMarker(Photo photo) {
        map.addMarker(markerFrom(photo));
    }

    private MarkerOptions markerFrom(Photo photo) {
        return new MarkerOptions().position(photo.location)
                                  .icon(iconFrom(photo.thumbnail))
                                  .draggable(true);
    }

    private BitmapDescriptor iconFrom(Bitmap thumbnail) {
        if (thumbnail != null) {
            return BitmapDescriptorFactory.fromBitmap(thumbnail);
        }
        return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
    }
}
```

## Adding the Picked Photo to the Map
Next thing to do is to add the picked photo to the map. We're going to add the photo when the map is clicked (tapped). To do it, we'll set a click event listener ( [**GoogleMap.OnMapClickListener**](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMapClickListener)) on the map by calling [*setOnMapCLickListener*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#setOnMapClickListener%28com.google.android.gms.maps.GoogleMap.OnMapClickListener%29) and override the listener's [*onMapClick*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMapClickListener.html#onMapClick%28com.google.android.gms.maps.model.LatLng%29) method. Inside the method we'll set the location of the photo to the just clicked one and redraw the photo. The redrawing process includes cleaning the map from all the existing markers by calling [*clear*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#clear%28%29), adding the photo with the new location and focusing on it. We had bit of this logic spread around inside onMapReady callback, so let's extract it into a separate method and move into the activity. The **TagMapFragment** should look something like this:
```java
public class TagMapFragment extends MapFragment {
    private GoogleMap map;
    private Photo photo;

    private final OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setOnMapClickListener(onMapClick);
            if (photo.hasLocation()) {
                drawPhoto();
            } else {
                focusOnLocation(Location.RIGA);
            }
        }
    };

    private final OnMapClickListener onMapClick = new OnMapClickListener() {
        @Override
        public void onMapClick(LatLng clickedLocation) {
            photo.location = clickedLocation;
            drawPhoto();
        }
    };

    ...(methods ommited)...

    private void drawPhoto() {
        map.clear();
        addPhotoMarker(photo);
        focusOnLocation(photo.location);
    }

    private void focusOnLocation(LatLng location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Location.DEFAULT_ZOOM));
    }
}
```

## Providing a Hint
Right now it's not very clear for the user what he needs to do after the photo is picked, so let's add a hint. We have the button in the bottom of the screen, so let's put the hint on it. We'll setup the button in the **TagActivity**. Let's set the text of the button to *R.string.tapOnMap* by calling [*setText*](http://developer.android.com/reference/android/widget/TextView.html#setText%28java.lang.CharSequence%29). Let's also disable the button by calling [*setEnabled*](http://developer.android.com/reference/android/widget/TextView.html#setEnabled%28boolean%29) until the user taps on the map. Here's the related code:
```java
public class TagActivity extends Activity {
    ...
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        setupButton();
    }

    ...(methods ommited)...

    private void setupButton() {
        button = (Button) findViewById(R.id.bottom_button);
        button.setText(R.string.tapOnMap);
        button.setEnabled(false);
    }
}
```

## Tagging
Now it's possible to add the photo to the map and set it's location tag. The only thing that's left is to store the tag on disk. We'll setup the view's button to do it by adding a click listener. Let's call [*setOnClickListener*](http://developer.android.com/reference/android/view/View.html#setOnClickListener%28android.view.View.OnClickListener%29) and pass it an instance of [View.OnClickListener](http://developer.android.com/reference/android/view/View.OnClickListener.html). Inside the listener's [*onCLick*](http://developer.android.com/reference/android/view/View.OnClickListener.html#onClick%28android.view.View%29) method, we'll store the tag and close the view to go back to the initial view with all the available photos displayed. To store the tag, create an instance of **lv.gdgriga.gdgmaps.tag.StoreTagTask** and call it's execute method by passing in the photo from the *mapFragment* (get the photo by adding a getter method to **TagMapFragment**). It's as easy as that. Now, to close the view, call [*finish*](http://developer.android.com/reference/android/app/Activity.html#finish%28%29). All we have on the view for user interaction is one button, which is disabled right now. After the photo is picked, it needs to be enabled. It's text needs to be changed as well. Let's implement a method to do it inside the **TagActivity**. The most appropriate way to call the method would be at the bottom of the *drawPhoto* inside **TagMapFragment**. To call the method, get the activity by calling *getActivity* and cast the result to **TagActivity**. The code is below:
```java
public class TagActivity extends Activity {
    ...
    private void setupButton() {
        ...
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StoreTagTask().execute(mapFragment.getPhoto());
                finish();
            }
        });
    }

    void enableStoreTagButton() {
        button.setText(R.string.storeTag);
        button.setEnabled(true);
    }
}

public class TagMapFragment extends MapFragment {
    ...
    private void drawPhoto() {
        ...
        ((TagActivity) getActivity()).enableStoreTagButton();
    }

    Photo getPhoto() {
        return photo;
    }
}
```

## Changing Photo's Location Upon Dragging
The tagging process is complete! The one last touch is to change the photo's location when it's dragged. That will help the user to specify the location more precisely. As you have noticed, the photo is already draggable. The only thing left to do is to store the location inside the Photo object when the dragging stops. To do it, we'll implement the [**GoogleMap.OnMarkerDragListener**](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMarkerDragListener) interface. Inside it's [*onDragEnd*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMarkerDragListener.html#onMarkerDragEnd%28com.google.android.gms.maps.model.Marker%29) method, we'll take the position of the marker by calling [*getPosition*](https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker.html#getPosition%28%29) and set the photo's *location* field to this value. Finally, register the drag listener with the *map* by calling [*setOnMarkerDragListener*](https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.html#setOnMarkerDragListener%28com.google.android.gms.maps.GoogleMap.OnMarkerDragListener%29) after the photo is added to the map (inside *addPhotoMarker* method). Congratulations, you are done! Here's the code:
```java
public class TagMapFragment extends MapFragment {
    ...

    private final GoogleMap.OnMarkerDragListener onMarkerDrag = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            photo.location = marker.getPosition();
        }
    };

    ...(methods ommited)...

    private void addPhotoMarker(Photo photo) {
        ...
        map.setOnMarkerDragListener(onMarkerDrag);
    }
}
```

## Useful Links
* [Android API](http://developer.android.com/reference/packages.html)
* [Google Maps API for Android](https://developers.google.com/android/reference/com/google/android/gms/maps/package-summary)
* [Google APIs for Android](https://developers.google.com/android/reference/packages)
* [Intents and Intent Filters](http://developer.android.com/guide/components/intents-filters.html)
