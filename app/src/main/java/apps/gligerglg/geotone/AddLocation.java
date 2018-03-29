package apps.gligerglg.geotone;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import plugins.gligerglg.locusservice.LocusService;

public class AddLocation extends AppCompatActivity {

    private FloatingActionButton btn_myPosition, btn_setLocation;
    private ConstraintLayout layout;
    private LocusService locusService;
    private MapView mapView;
    private Boolean isLocationOK = false;
    private Location myPosition;
    private Bundle bundle;
    private int tabIndex=0;
    private InterstitialAd mInterstitialAd;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,"pk.eyJ1IjoiZ2xpZ2VyIiwiYSI6ImNqZWJzN2Z6bzBkYTQyeG1rNW9rMjAwZncifQ.69m8fnyfGB4N1VtvmzpnOA");
        setContentView(R.layout.activity_add_location);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-2629585729370619~7922714837");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2629585729370619/8484207213");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        //Initialize
        Init();

        btn_setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocationOK) {
                    if(tabIndex==0)
                        intent = new Intent(AddLocation.this,AddNewPlace.class);
                    else
                        intent = new Intent(AddLocation.this,AddNewActivity.class);

                    intent.putExtra("latitude",myPosition.getLatitude());
                    intent.putExtra("longitude",myPosition.getLongitude());

                    if (mInterstitialAd.isLoaded())
                        mInterstitialAd.show();
                    else
                        startActivity(intent);
                }
                else
                    setMessage("You Need to Set a Location First!");
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        mapboxMap.clear();
                        mapboxMap.addMarker(new MarkerOptions().position(point));
                        myPosition = new Location("myPosition");
                        myPosition.setLatitude(point.getLatitude());
                        myPosition.setLongitude(point.getLongitude());
                        isLocationOK = true;
                    }
                });
            }
        });

        btn_myPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locusService.isNetProviderEnabled())
                {
                    final Location myLocation = locusService.getGPSLocation();
                    if(myLocation!=null)
                    {
                        myPosition = new Location(myLocation);
                        isLocationOK = true;

                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(MapboxMap mapboxMap) {
                                mapboxMap.clear();
                                LatLng latLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                                mapboxMap.addMarker(new MarkerOptions().position(latLng));

                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(latLng)) // Sets the new camera position
                                        .zoom(15) // Sets the zoom
                                        .build();
                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),4000);
                            }
                        });
                    }
                    else
                        setMessage("GPS will take some time to calculate your position. Try again!");
                }
                else
                    locusService.openSettingsWindow("GPS Service is currently disabled.\nEasyLec needs to enabled GPS connection" +
                            "\nDo you want to switch on GPS?");
            }
        });


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                startActivity(intent);
            }
        });

    }

    private void Init() {
        btn_myPosition = findViewById(R.id.btn_myPosition);
        btn_setLocation = findViewById(R.id.btn_setLocation);
        layout = findViewById(R.id.layout_add_location);
        mapView = findViewById(R.id.mapView);
        locusService = new LocusService(this);
        intent = null;
        bundle = getIntent().getExtras();
        tabIndex = bundle.getInt("service");

    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
