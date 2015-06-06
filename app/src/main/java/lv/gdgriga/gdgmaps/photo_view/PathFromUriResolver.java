package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

class PathFromUriResolver {
    static final String[] projection = {MediaStore.Images.Media.DATA};
    static final String selection = null;
    static final String[] selectionArgs = null;
    static final String sortOrder = null;

    final Context context;

    public PathFromUriResolver(Context context) {
        this.context = context;
    }

    public static PathFromUriResolver fromContext(Context context) {
        return new PathFromUriResolver(context);
    }

    public String resolve(Uri uri) {
        Cursor cursor = context.getContentResolver()
                               .query(uri, projection, selection, selectionArgs, sortOrder);
        cursor.moveToFirst();
        try {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        } finally {
            cursor.close();
        }
    }
}
