package lv.gdgriga.gdgmaps.tag;

import com.google.android.gms.maps.*;

import lv.gdgriga.gdgmaps.Location;
import lv.gdgriga.gdgmaps.Photo;

public class TagMapFragment extends MapFragment {
    private GoogleMap map;
    private Photo photo;

    OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            focusOnRiga();
        }

        private void focusOnRiga() {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    public void setPhotoToTag(Photo photo) {
        this.photo = photo;
    }
}
