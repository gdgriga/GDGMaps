package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lv.gdgriga.gdgmaps.Photo;

public class PhotoViewMapFragment extends MapFragment {
    GoogleMap map;

    OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            UiSettings uiSettings = map.getUiSettings();
            uiSettings.setMapToolbarEnabled(false);
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(locationListener);
            drawPhotos();
        }
    };

    GoogleMap.OnMyLocationChangeListener locationListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            map.setOnMyLocationChangeListener(null);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    void drawPhotos() {
        map.clear();
        for (Photo photo : PhotosWithLocation.list(context())) {
            Bitmap thumbnail = photo.getThumbnail();
            BitmapDescriptor icon = thumbnail != null ? BitmapDescriptorFactory.fromBitmap(thumbnail) : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
            MarkerOptions marker = new MarkerOptions().position(photo.getLocation())
                                                      .icon(icon);
            map.addMarker(marker);
        }
    }

    Context context() {
        return getActivity().getApplicationContext();
    }
}
