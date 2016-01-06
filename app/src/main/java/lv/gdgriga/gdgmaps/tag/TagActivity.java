package lv.gdgriga.gdgmaps.tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lv.gdgriga.gdgmaps.Photo;
import lv.gdgriga.gdgmaps.PhotoLoader;
import lv.gdgriga.gdgmaps.R;

public class TagActivity extends Activity {
    private TagMapFragment mapFragment;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_activity);
        mapFragment = new TagMapFragment();
        attachFragment(mapFragment);
        setupButton();
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

    private void setupButton() {
        button = (Button) findViewById(R.id.bottom_button);
        button.setText(R.string.tapOnMap);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StoreTagTask().execute(mapFragment.getPhoto());
                finish();
            }
        });
    }

    void enableStoreTagButton() {
        button.setText(R.string.storeTag);
        button.setEnabled(true);
    }
}
