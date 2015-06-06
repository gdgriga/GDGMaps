package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lv.gdgriga.gdgmaps.Photo;
import lv.gdgriga.gdgmaps.PhotoLoader;

class PhotosWithLocation {
    public static List<Photo> list(Context context) {
        List<String> files = PhotoFiles.list(context);
        List<Photo> photos = new ArrayList<>(files.size());
        for (String file : files) {
            Photo photo = PhotoLoader.fromFile(file);
            if (photo != Photo.EMPTY && photo.getLocation() != null) {
                photos.add(photo);
            }
        }
        return photos;
    }
}
