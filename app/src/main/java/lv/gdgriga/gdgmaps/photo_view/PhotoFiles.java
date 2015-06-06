package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PhotoFiles {
    static final String CAMERA_IMAGE_BUCKET_ID = bucketId(externalPhotoStorage());
    static final String DEFAULT_ORDERING = null;

    static String externalPhotoStorage() {
        return Environment.getExternalStorageDirectory()
                          .toString() + "/DCIM/Camera";
    }

    static String bucketId(String path) {
        return String.valueOf(path.toLowerCase()
                                  .hashCode());
    }

    public static List<String> list(Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        Cursor cursor = context.getContentResolver()
                               .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, DEFAULT_ORDERING);
        try {
            return photoList(cursor);
        } finally {
            cursor.close();
        }
    }

    static List<String> photoList(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return Collections.emptyList();
        }
        List<String> photos = new ArrayList<>(cursor.getCount());
        int column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        do {
            String photo = cursor.getString(column);
            photos.add(photo);
        } while (cursor.moveToNext());
        return photos;
    }
}
