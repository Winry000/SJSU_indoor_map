package droidmentor.bnv_with_viewpager.Fragment;

/**
 * Created by winryxie on 4/8/17.
 */

/**
 * Created by winryxie on 4/8/17.
 */


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashSet;

import droidmentor.bnv_with_viewpager.R;


public class NavigationFragment extends Fragment implements OnMapReadyCallback {


//    public NavigationFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_navigation, container, false);
//    }


    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_navigation, container,
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
//        MarkerOptions marker = new MarkerOptions().position(
//                new LatLng(latitude, longitude)).title("San Jose State University");
//
//        // Changing marker icon
//        marker.icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//
//        // adding marker
        LatLng sjsu = new LatLng(37.33642715101153, -121.8819272518158);
//        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sjsu).zoom(18).build();
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sjsu));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        LatLng nw = new LatLng(37.33720127803866, -121.88256025314331);
        LatLng ne = new LatLng(37.337638986828125, -121.88161142170429);
//        googleMap.addMarker(new MarkerOptions().position(nw).title("nw"));
//        googleMap.addMarker(new MarkerOptions().position(ne).title("ne"));
        LatLng sw = new LatLng(37.33649646023034, -121.8820395693183);
        LatLng se = new LatLng(37.336918445363196, -121.88109710812569);
//        googleMap.addMarker(new MarkerOptions().position(sw).title("sw"));
//        googleMap.addMarker(new MarkerOptions().position(se).title("se"));

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }

    private double slatitude;
    private double slongitude;

    private double elatitude;
    private double elongitude;

    private String startPosition;
    private String endPosition;

    public void startSet(View view) {
        googleMap.clear();
        setUpMap();
//        endSet(view);
        EditText location_tf = (EditText)getView().findViewById(R.id.addressStartText);
        String startRoom = location_tf.getText().toString();
//        List<Address> addressList = null;
        if (startRoom != null || startRoom.equals("")) {

            try {
                String jsonString = loadJsonFromAsset("location.json", getActivity());
                JSONObject json = new JSONObject(jsonString);
                JSONArray locations = json.getJSONArray("locations");
                Boolean flag = false;

                for(int i = 0; i < locations.length(); i++){

                    startPosition = locations.getJSONObject(i).getString("information");

                    if (startRoom.equals(startPosition)) {
                        flag = true;
                        String lan = locations.getJSONObject(i).getString("latitude");
                        slatitude = Double.parseDouble(lan);
                        String lon = locations.getJSONObject(i).getString("longitude");
                        slongitude = Double.parseDouble(lon);
                        LatLng roomPosition = new LatLng(slatitude, slongitude);
                        googleMap.addMarker(new MarkerOptions().position(roomPosition).title(startPosition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(roomPosition).build();
                        googleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        break;
                    }
                }
                if (flag == false) {
                    Toast.makeText(getActivity(), "No Such Room Number",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            Geocoder geocoder = new Geocoder(getActivity());
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(latLng).title("Room 278"));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }

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

    public void endSet(View view) {
        googleMap.clear();
        setUpMap();
        startSet(view);
        EditText location_tf = (EditText)getView().findViewById(R.id.addressEndText);
        String startRoom = location_tf.getText().toString();
//        List<Address> addressList = null;
        if (startRoom != null || startRoom.equals("")) {
            try {
                String jsonString = loadJsonFromAsset("location.json", getActivity());
                JSONObject json = new JSONObject(jsonString);
                JSONArray locations = json.getJSONArray("locations");
                Boolean flag = false;
                for (int i = 0; i < locations.length(); i++) {

                    endPosition = locations.getJSONObject(i).getString("information");

                    if (startRoom.equals(endPosition)) {
                        flag = true;
                        String lan = locations.getJSONObject(i).getString("latitude");
                        elatitude = Double.parseDouble(lan);
                        String lon = locations.getJSONObject(i).getString("longitude");
                        elongitude = Double.parseDouble(lon);
                        LatLng roomPosition = new LatLng(elatitude, elongitude);
                        googleMap.addMarker(new MarkerOptions().position(roomPosition).title(endPosition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(roomPosition).build();
                        googleMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                        break;
                    }

                }
                if (flag == false) {
                    Toast.makeText(getActivity(), "No Such Room Number",
                            Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onSearch(View view) {

        LatLng nw = new LatLng(37.337220, -121.882427);
        LatLng ne = new LatLng(37.33760326647438, -121.88164427876472);
        LatLng sw = new LatLng(37.33649646023034, -121.8820395693183);
        LatLng se = new LatLng(37.33694750173803, -121.88107162714005);

        String west[] = new String[]{"Room 278","Room 276","Room 272","Room 268","Room 256A","Room 256"};
        HashSet<String> westSet = new HashSet();
        for (int i = 0; i < west.length; i++) {
            westSet.add(west[i]);
        }

        String north[] = new String[]{"Room 249","Room 247","Room 245","Room 244","Room 242","Room 238",
                "Room 239","Room 237","Room 236","Room 232"};
        HashSet<String> northSet = new HashSet();
        for (int i = 0; i < north.length; i++) {
            northSet.add(north[i]);
        }

        String south[] = new String[]{"Room 284","Room 286","Room 288","Room 290","Room 292",
                "Room 287","Room 289","Room 291","Room 295"};
        HashSet<String> southSet = new HashSet();
        for (int i = 0; i < south.length; i++) {
            southSet.add(south[i]);
        }

        String east[] = new String[]{"Room 217","Room 215","Room 213","Room 211","Room 209","Room 207","Room 206","Room 203","Room 201"};
        HashSet<String> eastSet = new HashSet();
        for (int i = 0; i < east.length; i++) {
            eastSet.add(east[i]);
        }

        if (westSet.contains(startPosition) && westSet.contains(endPosition) ||
                northSet.contains(startPosition) && northSet.contains(endPosition) ||
                southSet.contains(startPosition) && southSet.contains(endPosition) ||
                eastSet.contains(startPosition) && eastSet.contains(endPosition)) {

            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(slatitude, slongitude), new LatLng(elatitude,elongitude))
                    .width(5)
                    .color(Color.RED));

        }

        if (westSet.contains(startPosition)) {
            if (northSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), nw)
                        .add(nw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (southSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), sw)
                        .add(sw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (eastSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), nw)
                        .add(nw, ne)
                        .add(ne, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
        }


        if (northSet.contains(startPosition)) {
            if (eastSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), ne)
                        .add(ne, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (westSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), nw)
                        .add(nw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (southSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), ne)
                        .add(ne, se)
                        .add(se, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
        }

        if (eastSet.contains(startPosition)) {
            if (northSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), ne)
                        .add(ne, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (southSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), se)
                        .add(se, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (westSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), se)
                        .add(se, sw)
                        .add(sw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
        }

        if (southSet.contains(startPosition)) {
            if (westSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), sw)
                        .add(sw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (eastSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), se)
                        .add(se, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
            if (northSet.contains(endPosition)) {
                googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(slatitude, slongitude), sw)
                        .add(sw, nw)
                        .add(nw, new LatLng(elatitude,elongitude))
                        .width(5)
                        .color(Color.RED));
            }
        }




//        EditText location_tf = (EditText)getView().findViewById(R.id.addressStartText);
//        String location = location_tf.getText().toString();
//
//        List<Address> addressList = null;
//
//        if (location != null || location.equals("")) {
//            Geocoder geocoder = new Geocoder(getActivity());
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(latLng).title("Room 278"));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        }
    }

//    private EditText mLocationEditText;
//
//    private void onSearch() {
//        String location = mLocationEditText.getText().toString();
//        List<Address> addressList = null;
//        if (location != null || !location.equals("")) {
//            Geocoder geocoder = new Geocoder(getActivity());
//            try {
//                addressList = geocoder.getFromLocationName(location, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address address = addressList.get(0);
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//            mLocationEditText.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//            mLocationEditText.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        }
//    }


}