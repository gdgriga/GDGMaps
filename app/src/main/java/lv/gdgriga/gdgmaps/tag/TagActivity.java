package lv.gdgriga.gdgmaps.tag;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lv.gdgriga.gdgmaps.PhotoLoader;
import lv.gdgriga.gdgmaps.R;

public class TagActivity extends Activity {
    TagMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_activity);
        createMapFragment();
        setupButton();
    }

    void createMapFragment() {
        mapFragment = new TagMapFragment();
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, mapFragment)
                            .commit();
    }

    void setupButton() {
        Button button = (Button) findViewById(R.id.bottom_button);
        button.setText(R.string.OK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userConfirmedToStorePhoto()) {
                    storePhoto();
                }
                finish();
            }
        });
    }

    boolean userConfirmedToStorePhoto() {
        // show dialog
        return true;
    }

    void storePhoto() {
        new StoreTagTask().execute(mapFragment.getPhoto());
    }

    @Override
    protected void onStart() {
        super.onStart();
        tagPhoto();
    }

    void tagPhoto() {
        String extra = getString(R.string.photoPath);
        String path = getIntent().getStringExtra(extra);
        mapFragment.tagPhoto(PhotoLoader.fromFile(path));
    }
}
