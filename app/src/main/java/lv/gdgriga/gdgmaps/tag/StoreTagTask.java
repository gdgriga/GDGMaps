package lv.gdgriga.gdgmaps.tag;

import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

import lv.gdgriga.gdgmaps.Photo;

class StoreTagTask extends AsyncTask<Photo, Void, Void> {
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy:MM:dd");

    @Override
    protected Void doInBackground(Photo... params) {
        try {
            Photo photo = params[0];
            storeLocationTag(photo);
        } catch (IOException e) {
            Log.e("StoreTagTask", e.toString());
        }
        return null;
    }

    private void storeLocationTag(Photo photo) throws IOException {
        ExifInterface exif = new ExifInterface(photo.fileName);
        double latitude = photo.location.latitude;
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(Coordinate.fromDecimalDegrees(latitude)));
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude < 0 ? "S" : "N");
        double longitude = photo.location.longitude;
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(Coordinate.fromDecimalDegrees(longitude)));
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude < 0 ? "W" : "E");
        exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "0/1");
        exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, "0");
        exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, formatter.print(LocalDate.now()));
        exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, String.valueOf(LocalTime.now()));
        exif.saveAttributes();
    }
}
