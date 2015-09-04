package lv.gdgriga.gdgmaps.photo_view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lv.gdgriga.gdgmaps.R;

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
        tagButton.setText(R.string.tag_photo);
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
}
