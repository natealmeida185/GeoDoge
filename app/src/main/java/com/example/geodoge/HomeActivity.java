/**
 * Name: Nathan Almeida,
 * Date Created: 05/05/2021,
 * Purpose: Main Page, User can view Google Maps, view Current Coordinates, view selected Park Coordinates, and Park Images).
 *  Allows the user to compare their coordinates in relation to their selected Park or Destination
 *  (Once coordinates match, the user is rewarded with a TOAST).
 *  Referenced Implementations:
 *  Google Maps- https://developers.google.com/maps/documentation/android-sdk/map
 *  GPS Locator App- Class Exercise
 */

package com.example.geodoge;

import androidx.annotation.RequiresApi;import androidx.appcompat.app.AppCompatActivity;import android.annotation.TargetApi;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.SharedPreferences;import android.content.pm.PackageManager;import android.os.Build;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.ImageView;import android.widget.Spinner;import android.widget.TextView;import com.google.android.gms.maps.CameraUpdateFactory;import com.google.android.gms.maps.GoogleMap;import com.google.android.gms.maps.OnMapReadyCallback;import com.google.android.gms.maps.SupportMapFragment;import com.google.android.gms.maps.model.LatLng;import com.google.android.gms.maps.model.MarkerOptions;import java.text.DecimalFormat;import java.util.ArrayList;import static android.Manifest.permission.ACCESS_COARSE_LOCATION;import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Declare android device permissions
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    //Used to see if the device's location is being tracked
    LocationTrack locationTrack;

    //Declare Google Marker and Spinner Position
    MarkerOptions markerOptions = new MarkerOptions();

    //Declare the devices Longitude and Latitude, Audio Playing Value
    double myLongitude;
    double myLatitude;

    //Declare UI Button, Image, and Google Map Tool
    Button btnLocation;
    ImageView locationImage;
    GoogleMap gMap;

    //Declare list of Park Coordinates
    ArrayList<LatLng>arrayList = new ArrayList<>();
    LatLng myCoords = new LatLng(myLatitude, myLongitude);
    LatLng Nelson = new LatLng(41.9667, -70.6715);
    LatLng Ellisville = new LatLng(41.8453, -70.5413);
    LatLng Morton = new LatLng(41.9427, -70.6805);
    LatLng Pilgrim = new LatLng(41.9589, -70.6622);
    LatLng Brewster = new LatLng(41.9556, -70.6623);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Add location permission from device
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        //Set icon in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Hello " + SignUpActivity.user);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Add Parks to List Created
        arrayList.add(Nelson);
        arrayList.add(Ellisville);
        arrayList.add(Morton);
        arrayList.add(Pilgrim);
        arrayList.add(Brewster);

        //Declare UI Elements and Audio
        TextView txtCoord = (TextView) findViewById(R.id.txtCoord);
        TextView selectedCoord = (TextView) findViewById(R.id.selectedCoord);
        DecimalFormat formater = new DecimalFormat("#.####");
        locationImage = findViewById(R.id.locationImage);
        btnLocation = findViewById(R.id.btnLocation);
        final Spinner group = (Spinner) findViewById(R.id.locations);

        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        locationTrack = new LocationTrack(HomeActivity.this);
        //Check if location can be found
        if (locationTrack.canGetLocation()) {
            myLongitude = locationTrack.getLongitude();
            myLatitude = locationTrack.getLatitude();
            txtCoord.setText("Your Coordinates: " +  formater.format(myLatitude) + "° N" + ", " + formater.format(myLongitude) + "° W");
        } else {
            locationTrack.showSettingsAlert();
            txtCoord.setText("Your Coordinates: ");
        }

        //Set listener for Display Selected Location Button
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int locationIndex = group.getSelectedItemPosition();

                //Check to see which Park or Spinner Index is being selected (1 - 5) ===> (0 - 4), then set Coordinate Text, Park Image, and Google Maps Marker
                switch(locationIndex) {
                    case 0:
                        displayNelson(selectedCoord, markerOptions, formater);
                        break;
                    case 1:
                        displayEllis(selectedCoord, markerOptions, formater);
                        break;
                    case 2:
                        displayMorton(selectedCoord, markerOptions, formater);
                        break;
                    case 3:
                        displayPilgrim(selectedCoord, markerOptions, formater);
                        break;
                    case 4:
                        displayBrewster(selectedCoord, markerOptions, formater);
                        break;
                    default:
                        displayMyCoords(txtCoord, markerOptions, formater, myLatitude, myLongitude);
                        break;
                }
            }
        });

        //Set listener for the user's personal location or device's location, then display the Coordinates
        Button setCoord = (Button) findViewById(R.id.setCoord);
        setCoord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                locationTrack = new LocationTrack(HomeActivity.this);

                if (locationTrack.canGetLocation()) {
                    myLongitude = locationTrack.getLongitude();
                    myLatitude = locationTrack.getLatitude();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // write all the data entered by the user in SharedPreference and apply
                    myEdit.putString("latitude", String.valueOf(myLatitude));
                    myEdit.putString("longitude", String.valueOf(myLongitude));
                    myEdit.apply();

                    displayMyCoords(txtCoord, markerOptions, formater, myLatitude, myLongitude);

                } else {
                    locationTrack.showSettingsAlert();
                }
            }
        });
    }

    //Add/Ask any other necessary permissions
    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    //If permitted, make sure user has the correct Android Version
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    //Boolean method to check if Build Version is greater than Lolipop
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    //Show Dialog Box with an Ok and Cancel option
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    protected void onResume(TextView txtCoord, DecimalFormat formater) {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        myLongitude = locationTrack.getLongitude();
        myLatitude = locationTrack.getLatitude();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("latitude", String.valueOf(myLatitude));
        myEdit.putString("longitude", String.valueOf(myLongitude));

        // Setting the fetched data
        // in the EditTexts
        txtCoord.setText("Your Coordinates: " +  formater.format(myLatitude) + "° N" + ", " + formater.format(myLongitude) + "° W");
    }

    //Stop tracking location
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    //Once Google Maps is loaded, display a set Marker Position
    @Override
    public void onMapReady(GoogleMap googleMap) {
        DecimalFormat formater = new DecimalFormat("#.####");
        gMap = googleMap;
        LatLng userCoords = new LatLng(myLatitude, myLongitude);
        gMap.addMarker(new MarkerOptions()
                .position(userCoords)
                .title("Your Coordinates: " +  formater.format(myLatitude) + "° N" + ", " + formater.format(myLongitude) + "° W"));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCoords, 15));
    }

    //Spinner Switch Statement: Display Nelson Memorial Park on Google Maps, as well as its selected coordinates, and a park image
    public void displayNelson(TextView selectedCoord, MarkerOptions markerOptions, DecimalFormat formater) {
        selectedCoord.setText("Selected Park: 41.9667° N, -70.6715° W");
        locationImage.setImageResource(R.drawable.nelson);
        markerOptions
                .position(arrayList.get(0))
                .title("Nelson Memorial Park:\n" + formater.format(arrayList.get(0).latitude) + "° N" + ", " + formater.format(arrayList.get(0).longitude) + "° W");
        //Zoom the Marker
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(0), 15));
        //Add Marker On Map
        gMap.addMarker(markerOptions);
    }

    //Spinner Switch Statement: Display Ellisville Harbor State Park on Google Maps, as well as its selected coordinates, and a park image
    public void displayEllis(TextView selectedCoord, MarkerOptions markerOptions, DecimalFormat formater) {
        selectedCoord.setText("Selected Park: 41.8453° N, -70.5413° W");
        locationImage.setImageResource(R.drawable.ellisville);
        markerOptions
                .position(arrayList.get(1))
                .title("Ellisville Harbor State Park:\n" + formater.format(arrayList.get(1).latitude) + "° N" + ", " + formater.format(arrayList.get(1).longitude) + "° W");
        //Zoom the Marker
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(1), 15));
        //Add Marker On Map
        gMap.addMarker(markerOptions);
    }

    //Spinner Switch Statement: Display Morton Park on Google Maps, as well as its selected coordinates, and a park image
    public void displayMorton(TextView selectedCoord, MarkerOptions markerOptions, DecimalFormat formater) {
        selectedCoord.setText("Selected Park: 41.9427° N, -70.6805° W");
        locationImage.setImageResource(R.drawable.morton);
        markerOptions
                .position(arrayList.get(2))
                .title("Morton Park:\n" + formater.format(arrayList.get(2).latitude) + "° N" + ", " + formater.format(arrayList.get(2).longitude) + "° W");
        //Zoom the Marker
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(2), 15));
        //Add Marker On Map
        gMap.addMarker(markerOptions);
    }

    //Spinner Switch Statement: Display Pilgrim Memorial State Park on Google Maps, as well as its selected coordinates, and a park image
    public void displayPilgrim(TextView selectedCoord, MarkerOptions markerOptions, DecimalFormat formater) {
        selectedCoord.setText("Selected Park: 41.9589° N, -70.6622° W");
        locationImage.setImageResource(R.drawable.pilgrim);
        markerOptions
                .position(arrayList.get(3))
                .title("Pilgrim Memorial State Park:\n" + formater.format(arrayList.get(3).latitude) + "° N" + ", " + formater.format(arrayList.get(3).longitude) + "° W");
        //Zoom the Marker
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(3), 15));
        //Add Marker On Map
        gMap.addMarker(markerOptions);
    }

    //Spinner Switch Statement: Display Brewster Gardens on Google Maps, as well as its selected coordinates, and a park image
    public void displayBrewster(TextView selectedCoord, MarkerOptions markerOptions, DecimalFormat formater) {
        selectedCoord.setText("Selected Park: 41.9556° N, -70.6623° W");
        locationImage.setImageResource(R.drawable.brewster);
        markerOptions
                .position(arrayList.get(4))
                .title("Brewster Gardens:\n" + formater.format(arrayList.get(4).latitude) + "° N" + ", " + formater.format(arrayList.get(4).longitude) + "° W");
        //Zoom the Marker
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(4), 15));
        //Add Marker On Map
        gMap.addMarker(markerOptions);
    }

    //Spinner Switch Statement: Display the user on Google Maps, as well as their selected coordinates, and GeoDoge's Icon image
    public void displayMyCoords(TextView txtCoord, MarkerOptions markerOptions, DecimalFormat formater, double myLatitude, double myLongitude) {
        txtCoord.setText("Your Coordinates: " +  formater.format(myLatitude) + "° N" + ", " + formater.format(myLongitude) + "° W");
        LatLng userCoords = new LatLng(myLatitude, myLongitude);
        gMap.addMarker(new MarkerOptions()
                .position(userCoords)
                .title("Your Coordinates: " +  formater.format(myLatitude) + "° N" + ", " + formater.format(myLongitude) + "° W"));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCoords, 15));
    }
}