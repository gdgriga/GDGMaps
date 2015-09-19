package lv.gdgriga.gdgmaps.tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import lv.gdgriga.gdgmaps.Photo;
import lv.gdgriga.gdgmaps.PhotoLoader;
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

    @Override
    protected void onStart() {
        super.onStart();
        setPhotoToTag();
    }

    private void setPhotoToTag() {
        Photo photoToTag = PhotoLoader.fromFile(photoPath());
        mapFragment.setPhotoToTag(photoToTag);
    }

    private String photoPath() {
        Intent toTagPhoto = getIntent();
        String extraName = getString(R.string.photoPath);
        return toTagPhoto.getStringExtra(extraName);
    }
}
