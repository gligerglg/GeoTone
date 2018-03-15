package apps.gligerglg.geotone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private boolean isFirsttime = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sharedPref= getSharedPreferences("easylec_service_status", 0);
        final SharedPreferences.Editor editor= sharedPref.edit();
        isFirsttime = sharedPref.getBoolean("intro", true);

        Thread timerThread = new Thread()
        {
            public  void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent;
                    if(isFirsttime) {
                        intent = new Intent(SplashScreen.this, Intro.class);
                        editor.putBoolean("intro",false);
                        editor.commit();
                    }
                    else
                        intent = new Intent(SplashScreen.this,MainMenu.class);
                    startActivity(intent);
                }
            }
        };

        timerThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
