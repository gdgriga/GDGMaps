package lv.gdgriga.gdgmaps.tag;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import lv.gdgriga.gdgmaps.Photo;

public class TagMapFragment extends MapFragment {
    GoogleMap map;
    Photo photo;

    OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(onLocationChange);
        }
    };

    GoogleMap.OnMyLocationChangeListener onLocationChange = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (photo == null) {
                return;
            }
            map.setOnMyLocationChangeListener(null);
            if (photo.getLocation() == null) {
                photo.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            }
            addPhotoMarker();
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

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    void tagPhoto(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }
}
