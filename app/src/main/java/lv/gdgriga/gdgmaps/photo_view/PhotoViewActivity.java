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
    private static final int PICK_PHOTO_CODE = 0xACE_F070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view_activity);
        attachMapFragment();
        setUpButton();
    }

    private void attachMapFragment() {
        getFragmentManager().beginTransaction()
                            .add(R.id.map_container, new PhotoViewMapFragment())
                            .commit();
    }

    private void setUpButton() {
        Button tagButton = (Button) findViewById(R.id.bottom_button);
        tagButton.setText(R.string.tagPhoto);
        tagButton.setOnClickListener(onTagButtonClick());
    }

    private View.OnClickListener onTagButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPickPhoto = new Intent(Intent.ACTION_PICK);
                toPickPhoto.setType("image/jpg");
                startActivityForResult(toPickPhoto, PICK_PHOTO_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode != PICK_PHOTO_CODE) return;
        if (resultCode != RESULT_OK) return;
        Uri photoUri = intent.getData();
        String photoPath = resolveToPath(photoUri);
        startTagActivity(photoPath);
    }

    private String resolveToPath(Uri photoUri) {
        return PathFromUriResolver.fromContext(getApplicationContext())
                                  .resolve(photoUri);
    }

    private void startTagActivity(String photoPath) {
        Intent tagActivity = new Intent(this, TagActivity.class);
        String photoPathExtraName = getString(R.string.photoPath);
        tagActivity.putExtra(photoPathExtraName, photoPath);
        startActivity(tagActivity);
    }
}
