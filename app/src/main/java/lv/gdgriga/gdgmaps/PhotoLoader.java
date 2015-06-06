package lv.gdgriga.gdgmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class PhotoLoader {
    public static Photo fromFile(String fileName) {
        try {
            return fromFileName(fileName);
        } catch (IOException e) {
            Log.e("Photos", e.getMessage());
            return Photo.EMPTY;
        }
    }

    static Photo fromFileName(String fileName) throws IOException {
        Photo photo = fromExifInterface(new ExifInterface(fileName));
        photo.setFileName(fileName);
        return photo;
    }

    static Photo fromExifInterface(ExifInterface exif) {
        LatLng location = getLocationFrom(exif);
        Photo photo = new Photo();
        photo.setLocation(location);
        if (exif.hasThumbnail()) {
            photo.setThumbnail(getThumbnailFrom(exif));
        }
        return photo;
    }

    static Bitmap getThumbnailFrom(ExifInterface exif) {
        byte[] thumbnail = exif.getThumbnail();
        return BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
    }

    static LatLng getLocationFrom(ExifInterface exif) {
        float[] latLong = new float[2];
        if (!exif.getLatLong(latLong)) {
            return null;
        }
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(ExifInterface.TAG_ORIENTATION)));
        return new LatLng(latLong[0], latLong[1]);
    }
}
