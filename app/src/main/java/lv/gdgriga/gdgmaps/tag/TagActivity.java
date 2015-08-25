package lv.gdgriga.gdgmaps.tag;

import android.app.Activity;
import android.os.Bundle;

import lv.gdgriga.gdgmaps.R;

public class TagActivity extends Activity {
    private TagMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_activity);
        mapFragment = new TagMapFragment();
        attachFragment(mapFragment);
    }

    private void attachFragment(TagMapFragment tagMapFragment) {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, tagMapFragment)
                            .commit();
    }
}
