package lv.gdgriga.gdgmaps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Photo {
    public static final Photo EMPTY = new Photo();

    String fileName;
    LatLng location;
    Bitmap thumbnail;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
