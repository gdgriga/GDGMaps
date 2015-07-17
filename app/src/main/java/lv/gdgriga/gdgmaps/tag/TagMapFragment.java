package lv.gdgriga.gdgmaps.tag;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;

import lv.gdgriga.gdgmaps.Location;
import lv.gdgriga.gdgmaps.Photo;

public class TagMapFragment extends MapFragment {
    private GoogleMap map;
    private Photo photo;

    private final OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setOnMapClickListener(onMapClick);
            focusOnRiga();
            drawPhotoIfItHasLocation();
        }

        private void focusOnRiga() {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
        }

        private void drawPhotoIfItHasLocation() {
            if (photo.location != null) {
                drawPhoto();
            }
        }
    };

    private final OnMarkerDragListener onMarkerDragListener = new OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {

        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            photo.location = marker.getPosition();
        }
    };

    private final OnMapClickListener onMapClick = new OnMapClickListener() {
        @Override
        public void onMapClick(LatLng clickLatLng) {
            photo.location = clickLatLng;
            drawPhoto();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    private void drawPhoto() {
        addPhotoMarker();
        ((TagActivity) getActivity()).enableButton();
    }

    private void addPhotoMarker() {
        LatLng photoLocation = photo.location;
        Marker photoMarker = map.addMarker(new MarkerOptions().position(photoLocation)
                                                              .draggable(true));
        if (photo.thumbnail != null) {
            photoMarker.setIcon(BitmapDescriptorFactory.fromBitmap(photo.thumbnail));
        } else {
            photoMarker.setTitle("No Thumbnail");
            photoMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        map.setOnMarkerDragListener(onMarkerDragListener);
        map.moveCamera(CameraUpdateFactory.newLatLng(photoLocation));
    }

    Photo getPhoto() {
        return photo;
    }

    void tagPhoto(Photo photo) {
        this.photo = photo;
    }
}
