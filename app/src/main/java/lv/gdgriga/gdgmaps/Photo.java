package lv.gdgriga.gdgmaps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Photo {
    public static final Photo EMPTY = new Photo();

    public String fileName;
    public LatLng location;
    public Bitmap thumbnail;
}
