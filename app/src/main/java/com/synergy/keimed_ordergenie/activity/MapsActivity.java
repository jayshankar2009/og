package com.synergy.keimed_ordergenie.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.synergy.keimed_ordergenie.R;
import com.synergy.keimed_ordergenie.model.m_callplans;
import com.synergy.keimed_ordergenie.utils.GeoFencing;
import com.synergy.keimed_ordergenie.utils.LocationAddress;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<Status> {

    GoogleMap mGoogleMap;
    TextView markerRating;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    List<m_callplans> posts;
    Polyline line;
    LatLng latLng;
    int i,sequencednum;
    String adress,number_marker,printsequence,prtsquenced;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        GsonBuilder builder = new GsonBuilder();

        Gson mGson = builder.create();

//        createStoreMarker();
        try {
            posts = new ArrayList<m_callplans>();
            String array_s = getIntent().getStringExtra("response");

            Log.d("comeRespString11",array_s);

            posts = new LinkedList<m_callplans>(Arrays.asList(mGson.fromJson(array_s, m_callplans[].class)));

            Log.d("comeRespString12",array_s);
        } catch (Exception e) {
            e.toString();
        }

        //   getSupportActionBar().setTitle("Map Location Activity");

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapsActivity.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.directionn);
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mGoogleMap.clear();
        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(icon);
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //optionally, stop location updates if only current location is needed
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


//        for(int i = 0;i<posts.size();i++)
//        {
//            if (posts.get(i).getSequence() != null)
//            {
//
//                prtsquenced = String.valueOf(posts.get(i).getSequence());
//                Log.d("ordergeniesequeced11", String.valueOf(posts.get(i).getSequence()));
//                Log.d("ordergeniesequeced12", prtsquenced);
//              markerRating.setText(prtsquenced);
//            }
//        }


        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getLongitude() == null)

            {
                posts.remove(i);
            }
        }

        try {
            add_markers(posts);
        } catch (Exception e) {

        }
        if (mGoogleMap != null) {
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(final Marker marker) {

                    //EventInfo eventInfo = posts.get(marker.getId());)
                    // sequencemap_textview

                    View convertView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.map_marker_info_view, null, false);

                    //  TextView sequencemap_textview = (TextView) convertView.findViewById(R.id.sequencemap_textview);
                    TextView client_name = (TextView) convertView.findViewById(R.id.client_name);

                    TextView client_address = (TextView) convertView.findViewById(R.id.client_address);
                    LinearLayout call_plan_start = (LinearLayout) convertView.findViewById(R.id.call_plan_start);




                       /*  for (i = 0; i < posts.size(); i++) {
                        if (posts.get(i).getSequence()!= null) {
                            sequencemap_textview.setText(String.valueOf(posts.get(i).getSequence()));
                            Log.d("SnumberTest", String.valueOf(posts.get(i).getSequence()));

                        }
                    }*/


                    if (marker.getId().equals("m0")) {
                        call_plan_start.setVisibility(View.GONE);


                    } else {
                        call_plan_start.setVisibility(View.VISIBLE);
                    }
                    String adress = LocationAddress.getAddressFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, MapsActivity.this);
                    client_address.setText(adress);


              /*      for (i = 0; i < posts.size(); i++) {
                        if (posts.get(i).getClient_LegalName().equals(marker.getTitle())) {
                            client_address.setText(posts.get(i).getClientLocation());
                        }
                    }*/


                    client_name.setText(marker.getTitle());


                    return convertView;
                }
            });
        }

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (i = 0; i < posts.size(); i++) {


                    if (marker.getId().equals("m0")) {
                        return;
                    }


                    if (posts.get(i).getClient_LegalName().equals(marker.getTitle())) {
                        JSONObject j_obj = new JSONObject();
                        JSONArray j_array = new JSONArray();
                        try {
                            j_obj.put("StockistCallPlanID", posts.get(i).getStockistCallPlanID());
                            j_obj.put("Client_LegalName", posts.get(i).getClient_LegalName());
                            j_obj.put("Latitude", posts.get(i).getLatitude());
                            j_obj.put("Longitude", posts.get(i).getLongitude());
                            j_obj.put("ClientLocation", posts.get(i).getClientLocation());
                            j_array.put(j_obj);

                            Intent iint = new Intent(MapsActivity.this, CallPlanDetails.class);
                            iint.putExtra("client_name", posts.get(i).getClient_LegalName());
                            iint.putExtra("client_id", posts.get(i).getStockistCallPlanID().toString());
                            iint.putExtra("response", j_array.toString());
                            startActivity(iint);

                        } catch (Exception e) {

                        }
                    }
                }
            }


        });
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void add_markers(final List<m_callplans> posts_s) {


        for (int i = 0; i < posts_s.size(); i++) {

            // Creating a marker

            MarkerOptions markerOptions = new MarkerOptions();


            //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

           /* BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.directionn);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Location Testing");
            markerOptions.icon(icon);
             markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/




            // Getting latitude of the place
            double latt = posts_s.get(i).getLatitude();

            // Getting longitude of the place
            double lngg = posts_s.get(i).getLongitude();

            // Getting name
            String name = posts_s.get(i).getClient_LegalName();

            number_marker = posts_s.get(i).getSequence().toString();
            //  sequencednum = posts_s.get(i).getSequence();

            LatLng latLng_s = new LatLng(latt, lngg);

            if (i == 0) {

                makeURL(latLng.latitude, latLng.longitude, latt, lngg, Color.GREEN);
                // drawPath(latLng,latt, lngg,Color.GREEN);
            } else {
                //  drawPath(new LatLng(posts_s.get(i-1).getLatitude(), posts_s.get(i-1).getLongitude()),latt, lngg,Color.GREEN);
                makeURL(posts_s.get(i - 1).getLatitude(), posts_s.get(i - 1).getLongitude(), latt, lngg, Color.RED);
                create_geo_fence_requests(posts_s.get(i).getLatitude(), posts_s.get(i).getLongitude(), mGoogleApiClient);
            }

            // Setting the position for the marker
            markerOptions.position(latLng_s);

            // Setting the title for the marker
            markerOptions.title(name);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createStoreMarker(number_marker)));

            // Placing a marker on the touched position
            mGoogleMap.addMarker(markerOptions);

            // Locate the first location
           /* if (i == 0)
                myMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));*/
        }
    }

    public Bitmap createStoreMarker(String number_marker) {
        //for (int i = 0; i < posts_s.size(); i++) {
            //  posts = new ArrayList<m_callplans>();
             adress = number_marker;
            View markerLayout = getLayoutInflater().inflate(R.layout.store_marker_layout, null);

            ImageView markerImage = (ImageView) markerLayout.findViewById(R.id.marker_image);
            markerRating = (TextView) markerLayout.findViewById(R.id.marker_text);
            markerImage.setImageResource(R.drawable.placeholder);

            markerRating.setText(adress);

        //  markerRating.setText(String.valueOf(posts.get(0).getSequence()));
        //  markerRating.setText(GlobalApp.getInstance().getLoginUserInfo().getStoreName());

//        for(int i = 0; i< this.posts.size(); i++)
//        {
//            if (this.posts.get(i).getSequence() != null){
//                prtsquenced = String.valueOf(this.posts.get(i).getSequence());
//                // Log.d("ordergeniesequeced13", String.valueOf(posts.get(i).getSequence()));
//                Log.d("OGsequeced14", prtsquenced);
//                markerRating.setText(prtsquenced);
//            }
//        }

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);

            return bitmap;

    }


    public void drawPath(LatLng latLngs, Double latit, Double Longt, int color) {

        try {
            // Tranform the string into a json object

            PolylineOptions options = new PolylineOptions();

            options.color(Color.RED);
            options.width(5);
            options.visible(true);
            options.geodesic(true);
            options.clickable(true);


            options.add(latLngs,
                    new LatLng(latit, Longt));


            mGoogleMap.addPolyline(options);




            /*line = mGoogleMap.addPolyline(new PolylineOptions()
                        .add(latLng,
                                new LatLng(latit, Longt))
                        .width(10).color(color).geodesic(true));*/


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void makeURL(double sourcelat, double sourcelog, double destlat, double destlog, int color) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyCUgf4bXNvmUhi2MJ59hE4Wh29sda_n1D8");


        JSONParser jParser = new JSONParser();

        new connectAsyncTask(urlString.toString(), color).execute();

        // String json = jParser.getJSONFromUrl(urlString.toString());
        // drawPath(json);
    }


  /*  public void getJSONFromUrl(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq_state = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                drawPath(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                return headers;
            }
        }

                ;

        queue.add(jsonObjReq_state);

    }*/


    public class JSONParser {

        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {
        }

        public String getJSONFromUrl(String url) {

            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;

        }
    }


    public void drawPath(final String s_json, int color) {

        try {
            final JSONObject json = new JSONObject(s_json);

            //Tranform the string into a json object
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);


            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(color)//Google maps blue color
                    .geodesic(true)
            );


           /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
        } catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        int Colors;

        connectAsyncTask(String urlPass, int color) {
            url = urlPass;
            Colors = color;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result, Colors);
            }
        }
    }

    @Override
    public void onResult(@NonNull Status status) {

        if (status.isSuccess()) {

        } else {
        }
    }

    void create_geo_fence_requests(double latitude, double longitude, GoogleApiClient googleApiClient) {
        GeoFencing o_GeoFencing = new GeoFencing();
        o_GeoFencing.createGeofences(latitude, longitude, this, googleApiClient);


    }
}