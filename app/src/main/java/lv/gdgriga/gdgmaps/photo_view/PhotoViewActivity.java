package lv.gdgriga.gdgmaps.photo_view;

import android.app.Activity;
import android.os.Bundle;

import lv.gdgriga.gdgmaps.R;

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
