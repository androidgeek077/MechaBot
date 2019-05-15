package app.uos.mechabot.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.uos.mechabot.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {



    private Double StdLatitude, StdLonitude;
    ArrayList mLocationList, mLongList;

    DatabaseReference StudentRef;
    ArrayList mLocationArr;
    Double latDouble, langDouble;
    FirebaseAuth mAuth;
    DatabaseReference AddLocation;
    MapView mMapView;
    private GoogleMap googleMap;
    private LatLng mylocation;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AddLocation= FirebaseDatabase.getInstance().getReference("Locations");
        getMechanicLocation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_home, container, false);
//        Double strLat = getArguments().getDouble("stdlang");
//        Toast.makeText(getContext(), ""+strLat, Toast.LENGTH_SHORT).show();
//        String strLang = getArguments().getString("stdlang");


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

                // For dropping a marker at a point on the Map
                LatLng Sargodha = new LatLng(32.0740, 72.6861);

                googleMap.addMarker(new MarkerOptions().position(Sargodha).title("Home").snippet("Sargodha"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(Sargodha).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
        googleMap.addMarker(new MarkerOptions().position(mylocation).title("Marker in Sydney"));
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


    private ArrayList<Double> getStudentLocation() {
        mLocationList = new ArrayList<>();
        mLongList = new ArrayList<>();
        StudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), ""+dataSnapshot, Toast.LENGTH_SHORT).show();
                StdLatitude = dataSnapshot.child("studentlat").getValue(Double.class);
//                Toast.makeText(getContext(), ""+StdLatitude, Toast.LENGTH_SHORT).show();
                StdLonitude = dataSnapshot.child("studentlong").getValue(Double.class);
//                Toast.makeText(getContext(), ""+StdLonitude, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mLocationList;
    }

    private ArrayList<String> getMechanicLocation(){
        mLocationArr=new ArrayList<>();
        AddLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latDouble = (Double) dataSnapshot.child("mLatitude").getValue();
                langDouble = (Double) dataSnapshot.child("mLongitude").getValue();
                mylocation=new LatLng(latDouble, langDouble);
                String str=Double.toString(latDouble);
                String str1=Double.toString(langDouble);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), str1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mLocationArr;
    }


}
