package lv.gdgriga.gdgmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

import static android.media.ExifInterface.*;

public class PhotoLoader {
    public static Photo fromFile(String fileName) {
        try {
            return fromFileName(fileName);
        } catch (IOException e) {
            Log.e("Photos", e.getMessage());
            return Photo.EMPTY;
        }
    }

    private static Photo fromFileName(String fileName) throws IOException {
        Photo photo = fromExifInterface(new ExifInterface(fileName));
        photo.fileName = fileName;
        return photo;
    }

    private static Photo fromExifInterface(ExifInterface exif) {
        // TODO: get location
        Void location = getLocationFrom(exif);
        Photo photo = new Photo();
        // TODO: store location inside photo
        if (exif.hasThumbnail()) {
            photo.thumbnail = getThumbnailFrom(exif);
        }
        return photo;
    }

    private static Bitmap getThumbnailFrom(ExifInterface exif) {
        byte[] thumbnail = exif.getThumbnail();
        return BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
    }

    private static Void getLocationFrom(ExifInterface exif) {
        float[] latLong = new float[2];
        if (!exif.getLatLong(latLong)) {
            return null;
        }
        logTags(exif);
        // TODO: return a LatLng
        return null;
    }

    private static void logTags(ExifInterface exif) {
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_LATITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_LATITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_LONGITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_LONGITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_ALTITUDE)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_ALTITUDE_REF)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_DATESTAMP)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_PROCESSING_METHOD)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_GPS_TIMESTAMP)));
        Log.d("PhotoLoader", String.valueOf(exif.getAttribute(TAG_ORIENTATION)));
    }
}
