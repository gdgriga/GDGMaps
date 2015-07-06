package lv.gdgriga.gdgmaps.tag;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import lv.gdgriga.gdgmaps.Location;
import lv.gdgriga.gdgmaps.Photo;

public class TagMapFragment extends MapFragment {
    GoogleMap map;
    Photo photo;

    OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setOnMapClickListener(onMapClick);
            focusOnRiga();
        }

        private void focusOnRiga() {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
        }
    };

    OnMarkerDragListener onMarkerDragListener = new OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            Log.d("OnMarkerDragListener", marker.getPosition()
                                                .toString());
            photo.setLocation(marker.getPosition());
        }
    };

    OnMapClickListener onMapClick = new OnMapClickListener() {
        @Override
        public void onMapClick(LatLng clickLatLng) {
            photo.setLocation(clickLatLng);
            drawPhoto();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    void tagPhoto(Photo photo) {
        this.photo = photo;
    }

    void drawPhoto() {
        addPhotoMarker();
    }

    void addPhotoMarker() {
        LatLng photoLocation = photo.getLocation();
        Marker photoMarker = map.addMarker(new MarkerOptions().position(photoLocation)
                                                              .draggable(true));
        if (photo.getThumbnail() != null) {
            photoMarker.setIcon(BitmapDescriptorFactory.fromBitmap(photo.getThumbnail()));
        } else {
            photoMarker.setTitle("No Thumbnail");
            photoMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        map.setOnMarkerDragListener(onMarkerDragListener);
        map.moveCamera(CameraUpdateFactory.newLatLng(photoLocation));
    }

    public Photo getPhoto() {
        return photo;
    }
}
