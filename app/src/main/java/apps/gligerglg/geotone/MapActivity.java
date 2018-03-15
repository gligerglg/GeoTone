package apps.gligerglg.geotone;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private Bundle bundle;
    private int tabIndex,id;
    private DBPlaceHelper dbPlaceHelper;
    private DBTaskHelper dbTaskHelper;
    private List<Place> placeList;
    private List<Task> taskList;
    private List<Locations> locationsList;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,"pk.eyJ1IjoiZ2xpZ2VyIiwiYSI6ImNqZWJzN2Z6bzBkYTQyeG1rNW9rMjAwZncifQ.69m8fnyfGB4N1VtvmzpnOA");
        setContentView(R.layout.activity_map);

        Init();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapboxMap.clear();
                if(locationsList.size()==0)
                    setMessage("Currently you have nothing to show!");
                else {
                    for (Locations location : locationsList)
                        mapboxMap.addMarker(new MarkerOptions().position(location.getPosition()).title(location.getTitle()));
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(locationsList.get(0).getPosition())) // Sets the new camera position
                            .zoom(12) // Sets the zoom
                            .build();

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000);

                }
            }
        });
    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void Init()
    {
        mapView = findViewById(R.id.full_mapView);
        bundle = getIntent().getExtras();
        tabIndex = bundle.getInt("service");
        id = bundle.getInt("id",-1);
        layout = findViewById(R.id.full_map_layout);
        locationsList = new ArrayList<>();
        placeList = new ArrayList<>();
        taskList = new ArrayList<>();

            if (tabIndex == 0) {
                dbPlaceHelper = new DBPlaceHelper(this);

                if(id==-1)
                    placeList = dbPlaceHelper.getAllPlaces();
                else
                    placeList.add(dbPlaceHelper.getPlace(id));

                for (Place place : placeList)
                    locationsList.add(new Locations(place.getTitle(), new LatLng(place.getLatitude(), place.getLongitude())));
            } else {
                dbTaskHelper = new DBTaskHelper(this);

                if(id==-1)
                    taskList = dbTaskHelper.getAllTasks();
                else
                    taskList.add(dbTaskHelper.getTask(id));

                for (Task task : taskList)
                    locationsList.add(new Locations(task.getTitle(), new LatLng(task.getLatitude(), task.getLongitude())));
            }
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

class Locations{
    private String title;
    private LatLng position;

    public Locations() {
    }

    public Locations(String title, LatLng position) {
        this.title = title;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}