package apps.gligerglg.geotone;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


/**
 * Created by Gayan Lakshitha on 3/15/2018.
 */

public class Intro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
        setSlideOverAnimation();

        addSlide(AppIntroFragment.newInstance("GeoTone",
                "Geo-based Sound profiler & Task reminder",
                R.drawable.intro1,
                Color.parseColor("#480a6d")));

        addSlide(AppIntroFragment.newInstance("Custom Sound Profiling",
                "Customize your sound profile for each place.\nIt doesn't matter where you are, GeoTone will take care of your device sound according to your location.",
                R.drawable.intro2,
                Color.parseColor("#ff7f00")));

        addSlide(AppIntroFragment.newInstance("Task Reminder",
                "Specific task? in specific location? in specific date and time? \nGive me details.\nI'll remind the task when you reach to the location in initiated date and time.",
                R.drawable.intro3,
                Color.parseColor("#107c0f")));

        addSlide(AppIntroFragment.newInstance("Just one thing!",
                "Please make sure to enable your GPS connection for real-time updates!",
                R.drawable.intro4,
                Color.parseColor("#8b0101")));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(Intro.this,MainMenu.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(Intro.this,MainMenu.class));
        finish();
    }
}
