package lv.gdgriga.gdgmaps.tag;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lv.gdgriga.gdgmaps.PhotoLoader;
import lv.gdgriga.gdgmaps.R;

public class TagActivity extends Activity {
    private TagMapFragment mapFragment;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_activity);
        createMapFragment();
        setupButton();
    }

    private void createMapFragment() {
        mapFragment = new TagMapFragment();
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, mapFragment)
                            .commit();
    }

    private void setupButton() {
        okButton = (Button) findViewById(R.id.bottom_button);
        okButton.setText(R.string.TAP_TO_ADD);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storePhoto();
                finish();
            }
        });
        okButton.setEnabled(false);
    }

    private void storePhoto() {
        new StoreTagTask().execute(mapFragment.getPhoto());
    }

    @Override
    protected void onStart() {
        super.onStart();
        tagPhoto();
    }

    private void tagPhoto() {
        String extra = getString(R.string.photoPath);
        String path = getIntent().getStringExtra(extra);
        mapFragment.tagPhoto(PhotoLoader.fromFile(path));
    }

    void enableButton() {
        okButton.setText(R.string.STORE_TAG);
        okButton.setEnabled(true);
    }
}
