package apps.gligerglg.geotone;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

import plugins.gligerglg.locusservice.LocusService;

public class MainMenu extends AppCompatActivity implements TabLayout.OnTabSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    private int tabIndex=0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private PagerAdapter pagerAdapter;
    private ArrayList<Place> placeList;
    private ArrayList<Task> taskList;
    private DBPlaceHelper dbPlaceHelper;
    private DBTaskHelper dbTaskHelper;
    private LocusService locusService;

    private boolean isServiceStarted = false;

    int[] colorArray = new int[]{R.color.color_floating_place,R.color.color_floating_task};
    int[] iconArray = new int[]{R.drawable.ic_add_location,R.drawable.ic_task_add};

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        setupDrawer();
        navigationView.setNavigationItemSelectedListener(this);

        //Initialize Components
        Init();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setAnimationButton(position);
                tabIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,AddLocation.class);
                if(tabIndex==0)
                    intent.putExtra("service",0);
                else
                    intent.putExtra("service",1);
                startActivity(intent);
            }
        });

    }

    private void Init() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        floatingActionButton = findViewById(R.id.btn_floating);

        tabLayout.addTab(tabLayout.newTab().setText("Places"));
        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        dbPlaceHelper = new DBPlaceHelper(this);
        dbTaskHelper = new DBTaskHelper(this);
        placeList = new ArrayList<>();
        taskList = new ArrayList<>();
        locusService = new LocusService(getApplicationContext(),false);
    }

    private void setupDrawer()
    {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setAnimationButton(tab.getPosition());
        tabIndex = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @TargetApi(21)
    protected void setAnimationButton(final int position)
    {
        floatingActionButton.clearAnimation();

        ScaleAnimation shrink = new ScaleAnimation(1f,0.2f,1f,0.2f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionButton.setBackgroundTintList(getResources().getColorStateList(colorArray[position]));
                floatingActionButton.setImageDrawable(getResources().getDrawable(iconArray[position],null));

                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);
                expand.setInterpolator(new AccelerateInterpolator());
                floatingActionButton.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        floatingActionButton.startAnimation(shrink);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_item_power:
                    if(isServiceStarted = statusChange())
                    {
                        stopService(new Intent(MainMenu.this,BackgroundService.class));
                        NotificationManagerCompat.from(getApplicationContext()).cancel(100);
                        NotificationManagerCompat.from(getApplicationContext()).cancel(200);
                        setMessage("Service has Stopped!");
                    }
                    else {
                        if(locusService.isGPSProviderEnabled())
                        {
                            placeList.clear();
                            taskList.clear();
                            placeList = (ArrayList<Place>) dbPlaceHelper.getAllPlaces();
                            taskList = (ArrayList<Task>) dbTaskHelper.getAllTasks();

                            if(placeList.isEmpty() && taskList.isEmpty())
                                setMessage("Insert a Place or a Task before Start the Service");
                            else {
                                Intent intent_service = new Intent(MainMenu.this, BackgroundService.class);
                                intent_service.putParcelableArrayListExtra("placeList", placeList);
                                intent_service.putParcelableArrayListExtra("taskList", taskList);
                                startService(intent_service);
                                setMessage("Service has started!");
                            }
                        }
                        else
                            setMessage("GeoTone needs GPS location data. \nPlease Turn On GPS");
                    }

                return true;

            case R.id.nav_item_viewPlaces:
                Intent intent_places = new Intent(MainMenu.this, MapActivity.class);
                intent_places.putExtra("service",0);
                startActivity(intent_places);
                return true;

            case R.id.nav_item_viewTasks:
                Intent intent_tasks = new Intent(MainMenu.this, MapActivity.class);
                intent_tasks.putExtra("service",1);
                startActivity(intent_tasks);
                return true;

            case R.id.nav_item_about:
                startActivity(new Intent(MainMenu.this,About.class));
                return true;

            case R.id.nav_item_contact:
                startActivity(new Intent(MainMenu.this,Contact.class));
                return true;
        }

        return false;
    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(drawerLayout,message,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


    private boolean statusChange()
    {
        SharedPreferences sharedPref= getSharedPreferences("easylec_service_status", 0);
        SharedPreferences.Editor editor= sharedPref.edit();
        boolean isStarted = sharedPref.getBoolean("status", false);

        if(isStarted)
            editor.putBoolean("status",false);
        else
            editor.putBoolean("status",true);

        editor.commit();
        return isStarted;
    }

    @Override
    protected void onResume() {
        super.onResume();
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
    }
}
