package lv.gdgriga.gdgmaps.tag;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
            drawPhoto();
        }
    };

    GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener() {


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

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    void tagPhoto(Photo photo) {
        this.photo = photo;
    }

    void drawPhoto() {
        setPhotoLocationIfAbsent();
        addPhotoMarker();
    }

    void setPhotoLocationIfAbsent() {
        if (photo.getLocation() == null) {
            photo.setLocation(Location.RIGA);
        }
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(photoLocation, 13));
    }

    public Photo getPhoto() {
        return photo;
    }
}
