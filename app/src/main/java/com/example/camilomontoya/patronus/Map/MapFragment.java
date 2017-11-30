package com.example.camilomontoya.patronus.Map;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.camilomontoya.patronus.Friends.FriendItem;
import com.example.camilomontoya.patronus.Friends.FriendsAdapter;
import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, android.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private static final String TAG = "MapFragment";
    private static final int PERMISSION_RESULT = 101;
    private static final long MIN_DISTANCE = 10;
    private static final long MIN_TIME = 30000;
    private static final LatLngBounds LATLNG_BOUNDS = new LatLngBounds(new LatLng(3.28, -76.65), new LatLng(3.55, -76.45));
    private static final int[] COLORS = new int[]{R.color.colorPrimaryPatrnous, R.color.colorAlternativeRoute, R.color.colorAlternativeRoute2};

    private GoogleMap map;
    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationManager locationManager;
    private Location myLocation;
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private boolean gpsOn, networkOn;
    private boolean firstTime;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Place destiny;
    private List<Polyline> polylines;
    private long lastElection;

    private AutoCompleteTextView search;
    private Button goBtn;
    private ImageView locationBtn;
    private RelativeLayout fondoGoBtn;
    private ArrayList<User> filterItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#1C2335"));

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        search = (AutoCompleteTextView) view.findViewById(R.id.search_place);
        goBtn = (Button) view.findViewById(R.id.btn_go_map);
        locationBtn = (ImageView) view.findViewById(R.id.mylocation_btn);
        fondoGoBtn = (RelativeLayout) view.findViewById(R.id.relative_btn_go_map);

        polylines = new ArrayList<>();

        search.setTypeface(Typo.getInstance().getContent());
        search.setTextSize(14f);
        goBtn.setTypeface(Typo.getInstance().getTitle());

        if (!firstTime) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(getContext())
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(getActivity(), (GoogleApiClient.OnConnectionFailedListener) this)
                    .build();
        }

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient,
                LATLNG_BOUNDS, null);

        search.setOnItemClickListener(mAutocompleteClickListener);

        search.setAdapter(mPlaceAutocompleteAdapter);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //Searching method
                    geoLocate();
                }

                return false;
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.getText().toString().length() > 0) {
                    geoLocate();
                }
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                cleanRoute();
                map.clear();
                search.setText("");
                hideKeyboard();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!getActivity().isFinishing()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (search.getText().toString().length() > 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fondoGoBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPatrnous));
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fondoGoBtn.setBackgroundColor(Color.parseColor("#bbbbbb"));
                            }
                        });
                    }
                }
            }
        }).start();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsOn && !networkOn) {
            Log.d(TAG, "onCreateView: Not connected");
            showAlertGPS();
        } else {
            Log.d(TAG, "onCreateView: Connected");
        }

        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        //updateUI(location);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboard();

        mapView = (MapView) getActivity().findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_night));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Position is done");
                    Location currentStart = task.getResult();
                    myLocation = currentStart;
                    LatLng startingPos = new LatLng(currentStart.getLatitude(), currentStart.getLongitude());
                    if (!firstTime) {
                        firstTime = true;
                        moveCamera(startingPos, 15f);
                    } else {
                        animateCamera(startingPos, 15f);
                    }
                } else {
                    Log.d(TAG, "onComplete: Position is undone :C");
                }
            }
        });
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Position is done");
                    Location currentStart = task.getResult();
                    myLocation = currentStart;
                } else {
                    Log.d(TAG, "onComplete: Position is undone :C");
                }
            }
        });
    }

    private void animateCamera(LatLng latlng, float zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void moveCamera(LatLng latlng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void animateCameraRoute(LatLng latlng, float zoom, String title, long election) {
        MarkerOptions options = new MarkerOptions().position(latlng)
                .title(title);
        map.addMarker(options);
        getRouteToMarker(options, election);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: Geolocating");

        String searchDir = search.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchDir, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15f);
            MarkerOptions options = new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude()))
                    .title(address.getAddressLine(0));
            map.addMarker(options);
            animateCamera(new LatLng(options.getPosition().latitude,options.getPosition().longitude),15f);
        }
    }

    private void showFriendsDialog(final LatLng latlng, final float zoom, final String title) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_users_route, null);

        final RecyclerView recyclerView = mView.findViewById(R.id.friends_to_go);
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<FriendChooseItem> allFriends = new ArrayList<>();

        if (CurrentUser.getRef().getFriends().size() > 0) {
            for (User item : CurrentUser.getRef().getFriends()) {
                allFriends.add(new FriendChooseItem(item.getProfilePic(), item.getName(), item.getEmail(), "", false));
            }
        }

        adapter = new FriendsChooseAdapter(allFriends);

        recyclerView.setAdapter(adapter);


        TextView start = mView.findViewById(R.id.start_trip);
        TextView cancel = mView.findViewById(R.id.cancel_trip);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems = new ArrayList<User>();
                for (int i = 0; i < allFriends.size(); i++) {
                    if (allFriends.get(i).isChoosed()) {
                        filterItems.add(CurrentUser.getRef().getFriends().get(i));
                    }
                }

                long distanceAverage = 0;

                for (User userChoosed : filterItems) {
                    distanceAverage += userChoosed.getDistance();
                }

                distanceAverage = distanceAverage / filterItems.size();

                animateCameraRoute(latlng, zoom, title, distanceAverage);

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getLocation();
                cleanRoute();
                map.clear();
                search.setText("");
                hideKeyboard();
            }
        });


    }

    private void showAlertGPS() {

    }

    private void hideKeyboard() {
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getView().getRootView().getWindowToken(), 0);
    }

    /*
                         ------------------  GOOGLE PLACES API -------------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            pendingResult.setResultCallback(mUpdateCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdateCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: The place wasn't found");
                places.release();
                return;
            } else {
                final Place place = places.get(0);
                Log.d(TAG, "onResult: Place found: " + place.getAttributions());

                destiny = place;
                showFriendsDialog(destiny.getLatLng(),15f,destiny.getName().toString());
                places.release();
            }
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*
                    ------------------------ GOOGLE DIRECTIONS API ---------------------
     */

    private void getRouteToMarker(MarkerOptions option, long election) {
        getMyLocation();
        lastElection = election;
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
                        new LatLng(option.getPosition().latitude, option.getPosition().longitude))
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        //Start the route
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;
            PolylineOptions polyOptions = new PolylineOptions();

            if(route.size()==2) {
                if(lastElection<=2) {
                    Log.d(TAG, "onRoutingSuccess: Ruta de 2 opciones");
                    polyOptions.color(getResources().getColor(COLORS[0]));
                    polyOptions.width(4);
                    polyOptions.addAll(route.get(0).getPoints());
                    Polyline polyline = map.addPolyline(polyOptions);
                    polylines.add(polyline);
                } else {
                    polyOptions.color(getResources().getColor(COLORS[2]));
                    polyOptions.width(4);
                    polyOptions.addAll(route.get(1).getPoints());
                    Polyline polyline = map.addPolyline(polyOptions);
                    polylines.add(polyline);
                }
            } else if(route.size()==3){
                Log.d(TAG, "onRoutingSuccess: Ruta de 3 opciones");
                if(lastElection<2) {
                    polyOptions.color(getResources().getColor(COLORS[0]));
                    polyOptions.width(4);
                    polyOptions.addAll(route.get(0).getPoints());
                    Polyline polyline = map.addPolyline(polyOptions);
                    polylines.add(polyline);
                } else if(lastElection>=2 && lastElection <4){
                    polyOptions.color(getResources().getColor(COLORS[1]));
                    polyOptions.width(4);
                    polyOptions.addAll(route.get(1).getPoints());
                    Polyline polyline = map.addPolyline(polyOptions);
                    polylines.add(polyline);
                } else {
                    polyOptions.color(getResources().getColor(COLORS[2]));
                    polyOptions.width(4);
                    polyOptions.addAll(route.get(2).getPoints());
                    Polyline polyline = map.addPolyline(polyOptions);
                    polylines.add(polyline);
                }
            } else {
                Log.d(TAG, "onRoutingSuccess: Ruta de una sola opcion o de mas opciones");
                polyOptions.color(getResources().getColor(COLORS[0]));
                polyOptions.width(4);
                polyOptions.addAll(route.get(0).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylines.add(polyline);
            }

            Toast.makeText(getContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void cleanRoute() {
        for (Polyline poly : polylines) {
            poly.remove();
        }

        polylines.clear();
    }
}
