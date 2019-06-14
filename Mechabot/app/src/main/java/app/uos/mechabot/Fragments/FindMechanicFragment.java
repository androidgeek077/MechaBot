package app.uos.mechabot.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.uos.mechabot.Models.MechanicModel;
import app.uos.mechabot.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindMechanicFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    LatLng current;
    String[] polylines;
    private static final String DirectionUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    String apiKey = "AIzaSyBe6jPPwp3T121djiVmLpW0FhodNdpSYfQ";

    private ArrayList<String> mAdmins = new ArrayList<>();
    private ArrayList<String> mAdminsIds = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    private Double StdLatitude, StdLonitude;
    ArrayList mLocationList, mLongList;

    DatabaseReference StudentRef;
    ArrayList mLocationArr;
    Double latDouble, langDouble;
    FirebaseAuth mAuth;
    DatabaseReference MechanicLocation;
    MapView mMapView;
    private GoogleMap googleMap;
    private LatLng mylocation;

    public FindMechanicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MechanicLocation = FirebaseDatabase.getInstance().getReference("Mechanic");
//        getMechanicLocation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_find_mechanic, container, false);
//        Double strLat = getArguments().getDouble("stdlang");
//        Toast.makeText(getContext(), ""+strLat, Toast.LENGTH_SHORT).show();
//        String strLang = getArguments().getString("stdlang");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

//                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                    @Override
//                    public void onMarkerDragStart(Marker marker) {
//                        Toast.makeText(getContext(), "Draged", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onMarkerDrag(Marker marker) {
//
//                    }
//
//                    @Override
//                    public void onMarkerDragEnd(Marker marker) {
//
//                    }
//                });
                // For dro
                // pping a marker at a point on the Map
                LatLng Sargodha = new LatLng(32.0740, 72.6861);
                LatLng kachar_Pur = new LatLng(32.092743, 72.733996);
                LatLng ChakNo52 = new LatLng(32.147929, 72.694703);
                LatLng GPO = new LatLng(32.073278, 72.668703);

                googleMap.addMarker(new MarkerOptions().position(Sargodha).title("Home").snippet("Sargodha"));
                googleMap.addMarker(new MarkerOptions().position(kachar_Pur).title("Nazir AutoShop").snippet("0345-8757847"));
                googleMap.addMarker(new MarkerOptions().position(ChakNo52).title("Bismillah Workshop").snippet("0300-3453463"));
                googleMap.addMarker(new MarkerOptions().position(GPO).title("Sargodha Motors").snippet("0483217654"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(Sargodha).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        googleMap.clear();
                        marker.getTitle();
                        LatLng selectedMarkerLoc = marker.getPosition();
                        googleMap.addMarker(new MarkerOptions().position(selectedMarkerLoc).title(marker.getTitle()).snippet(marker.getSnippet()));

                        distance(current.latitude, current.longitude, selectedMarkerLoc.latitude, selectedMarkerLoc.longitude);

                        String StrUrl = DirectionUrl + current.latitude + "," + current.longitude + "&destination=" + selectedMarkerLoc.latitude + "," + selectedMarkerLoc.longitude + "&key=" + apiKey;
                        StringRequest request = new StringRequest(0, StrUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                        location.getLatitude(), location.getLongitude()
//                        Toast.makeText(MapsActivity.this, response, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray getData = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                                    int count = getData.length();
                                    polylines = new String[count];

                                    for (int i = 0; i < count; i++) {
                                        try {
                                            polylines[i] = getPath(getData.getJSONObject(i));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                int polylinesCount = polylines.length;
                                for (int i = 0; i < polylinesCount; i++) {
                                    PolylineOptions options = new PolylineOptions();
                                    options.color(Color.BLUE);
                                    options.width(10);
                                    options.addAll(PolyUtil.decode(polylines[i]));
                                    googleMap.addPolyline(options);
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            // response
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);

                        return true;
                    }
                });
            }
        });

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        current = new LatLng(location.getLatitude(), location.getLongitude());
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                    .zoom(12)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                });


        return rootView;
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
    public void onLocationChanged(Location location) {
        googleMap.addMarker(new MarkerOptions().position(mylocation).title("03004626618"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(StdLatitude, StdLonitude), 15.0f));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    private ArrayList<String> getMechanicLocation() {
        mLocationArr = new ArrayList<>();
        MechanicLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latDouble = (Double) dataSnapshot.child("mLatitude").getValue();
                langDouble = (Double) dataSnapshot.child("mLongitude").getValue();
                mylocation = new LatLng(latDouble, langDouble);
                String str = Double.toString(latDouble);
                String str1 = Double.toString(langDouble);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), str1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mLocationArr;
    }

    private void SendSmsToAll() {
        mAdmins = new ArrayList<>();
        MechanicLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> adminsList = dataSnapshot.getChildren();
                for (DataSnapshot admins : adminsList) {
                    mAdminsIds.add(admins.getKey());
                    MechanicModel model = admins.getValue(MechanicModel.class);
                    mAdmins.add(model.getPhone());

                    AddMarkerToMap(Double.parseDouble(model.getLatStr()), Double.parseDouble(model.getLongStr()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddMarkerToMap(Double Lat, Double Lng) {
        mylocation = new LatLng(Lat, Lng);
        googleMap.addMarker(new MarkerOptions().position(mylocation).title("Marker in Sydney"));

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Toast.makeText(getActivity(), "" + dist, Toast.LENGTH_SHORT).show();
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public String getPath(JSONObject googlePathJson) {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
