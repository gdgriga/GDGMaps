package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import lv.gdgriga.gdgmaps.Location;
import lv.gdgriga.gdgmaps.Photo;

public class PhotoViewMapFragment extends MapFragment {
    private GoogleMap map;

    private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.getUiSettings()
               .setMapToolbarEnabled(false);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
            drawPhotos();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getMapAsync(onMapReady);
    }

    private void drawPhotos() {
        map.clear();
        for (Photo photo : PhotosWithLocation.list(context())) {
            map.addMarker(markerFrom(photo));
        }
    }

    private Context context() {
        return getActivity().getApplicationContext();
    }

    private MarkerOptions markerFrom(Photo photo) {
        Bitmap thumbnail = photo.thumbnail;
        BitmapDescriptor icon = thumbnail != null ? BitmapDescriptorFactory.fromBitmap(thumbnail) : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        return new MarkerOptions().position(photo.location)
                                  .icon(icon)
                                  .snippet(photo.fileName);
    }
}
