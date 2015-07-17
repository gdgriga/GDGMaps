package lv.gdgriga.gdgmaps.photo_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.*;

import lv.gdgriga.gdgmaps.Location;
import lv.gdgriga.gdgmaps.Photo;

public class PhotoViewMapFragment extends MapFragment {
    private GoogleMap map;

    private final OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            UiSettings uiSettings = map.getUiSettings();
            uiSettings.setMapToolbarEnabled(false);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location.RIGA, Location.DEFAULT_ZOOM));
            drawPhotos();
            map.setOnMarkerClickListener(onMarkerCLick);
        }
    };

    private final OnMarkerClickListener onMarkerCLick = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            showPhotoInGallery(marker.getSnippet());
            return true;
        }

        private void showPhotoInGallery(String photoFilePath) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + photoFilePath), "image/*");
            startActivity(intent);
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
        BitmapDescriptor icon = thumbnail != null ? BitmapDescriptorFactory.fromBitmap(thumbnail) : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
        return new MarkerOptions().position(photo.location)
                                  .icon(icon)
                                  .snippet(photo.fileName);
    }
}
