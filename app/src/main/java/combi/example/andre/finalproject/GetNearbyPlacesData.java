package combi.example.andre.finalproject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... params) {
        try{
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            //Log.d("getmap", String.valueOf(mMap.isMyLocationEnabled()));
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
        }
        catch (Exception e){
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result){
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList, mMap);
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList, GoogleMap mMap){
        try{
            for(int i = 0; i < nearbyPlacesList.size(); i++){
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latlng = new LatLng(lat, lng);
                Log.d("test", String.valueOf(latlng)+placeName+vicinity);

                if(mMap!=null){
                    mMap.addMarker(markerOptions.position(latlng).title(placeName + " : " + vicinity));
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}
