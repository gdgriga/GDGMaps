package lv.gdgriga.gdgmaps.photo_view;

import com.google.android.gms.maps.*;

import lv.gdgriga.gdgmaps.Location;

public class PhotoViewMapFragment extends MapFragment {
    private GoogleMap map;

    private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings().setMapToolbarEnabled(false);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }
}
