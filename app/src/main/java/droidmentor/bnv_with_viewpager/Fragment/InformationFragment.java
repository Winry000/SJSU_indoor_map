package droidmentor.bnv_with_viewpager.Fragment;

/**
 * Created by winryxie on 4/8/17.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;


import droidmentor.bnv_with_viewpager.R;


public class InformationFragment extends Fragment implements OnMapReadyCallback {


//    public InformationFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_information, container, false);
//    }


    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_information, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }







        // Perform any camera updates here
        return v;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    public void setUpMap() {
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();

        LatLng centerLocation = new LatLng(37.337066925986306, -121.88179314136505);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.eng_buliding_f2);
        groundOverlayOptions.image(bitmapDescriptor);
        groundOverlayOptions.position(centerLocation, 140f, 140f);
        groundOverlayOptions.bearing(-31);

        groundOverlayOptions.image(bitmapDescriptor);
        groundOverlayOptions.transparency(0.2f);
        googleMap.addGroundOverlay(groundOverlayOptions);

        // latitude and longitude
        double latitude = 37.33642715101153;
        double longitude = -121.8819272518158;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("San Jose State University");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        // adding marker
        LatLng sjsu = new LatLng(37.33642715101153, -121.8819272518158);
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sjsu).zoom(18).build();
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sjsu));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));



//        LatLng room278 = new LatLng(37.33675983467852, -121.88195943832397);
//        googleMap.addMarker(new MarkerOptions().position(room278).title("Room 278"));

//        try{
//            StringBuilder buf=new StringBuilder();
//            InputStream json = getAssets().open("location.json");
//            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
//            String str;
//
//            while ((str=in.readLine()) != null) {
//                buf.append(str);
//            }
//
//            in.close();
//        } catch(Exception e){
//
//        }



        try {
            // Load data
            String jsonString = loadJsonFromAsset("location.json", getActivity());
            JSONObject json = new JSONObject(jsonString);
            JSONArray locations = json.getJSONArray("locations");


            // Get Recipe objects from data
            for(int i = 0; i < locations.length(); i++){

                String lan = locations.getJSONObject(i).getString("latitude");
                latitude = Double.parseDouble(lan);
                String lon = locations.getJSONObject(i).getString("longitude");
                longitude = Double.parseDouble(lon);
                String information = locations.getJSONObject(i).getString("information");

                LatLng roomPosition = new LatLng(latitude, longitude);
                String snip = locations.getJSONObject(i).getString("snippet");
                googleMap.addMarker(new MarkerOptions().position(roomPosition).title(information).snippet(snip));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

