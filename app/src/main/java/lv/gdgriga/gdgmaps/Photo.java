package lv.gdgriga.gdgmaps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Photo {
    public static final Photo EMPTY = new Photo();
    public String fileName;
    public Bitmap thumbnail;
    public LatLng location;

    public boolean hasLocation() {
        return location != null;
    }
}
