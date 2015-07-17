package lv.gdgriga.gdgmaps.photo_view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lv.gdgriga.gdgmaps.R;
import lv.gdgriga.gdgmaps.tag.TagActivity;

public class PhotoViewActivity extends Activity {
    public static final int PICK_PHOTO_CODE = 0xBadFace;

    private final View.OnClickListener onTagButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/jpg");
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_activity);
        createMapFragment();
        setupButton();
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.bottom_button);
        button.setText(R.string.tag_photo);
        button.setOnClickListener(onTagButtonClicked);
    }

    private void createMapFragment() {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, new PhotoViewMapFragment())
                            .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent pickPhotoIntent) {
        super.onActivityResult(requestCode, resultCode, pickPhotoIntent);
        if (requestCode != PICK_PHOTO_CODE) return;
        if (resultCode != RESULT_OK) return;
        startTagActivity(pickPhotoIntent.getData());
    }

    private void startTagActivity(Uri photoUri) {
        Intent tagIntent = new Intent(this, TagActivity.class);
        String photoPath = PathFromUriResolver.fromContext(getApplicationContext())
                                              .resolve(photoUri);
        tagIntent.putExtra(getString(R.string.photoPath), photoPath);
        startActivity(tagIntent);
    }
}
