package apps.gligerglg.geotone;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Contact extends AppCompatActivity {

    private ImageButton btn_facebook, btn_github, btn_linkedin, btn_email;
    private ConstraintLayout layout;
    private CardView btn_myWeb;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-2629585729370619~7922714837");
        adView = findViewById(R.id.adView_Contact);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btn_email = findViewById(R.id.btn_gmail);
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_github = findViewById(R.id.btn_github);
        btn_linkedin = findViewById(R.id.btn_linkedIn);
        btn_myWeb = findViewById(R.id.btn_myweb);
        layout = findViewById(R.id.layout_contact);

        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/gliger-glg/"));
                startActivity(viewIntent);
            }
        });

        btn_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/gligerglg"));
                startActivity(viewIntent);
            }
        });

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.facebook.com/gligerglg"));
                startActivity(viewIntent);
            }
        });

        btn_myWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://gligerglg.github.io/"));
                startActivity(viewIntent);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "gliger.glg@gmail.com");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send a mail to Gayan"));
                }
                catch (Exception ex){
                    setMessage("There is no Email client installed.");
                }
            }
        });
    }

    private void setMessage(String message)
    {
        Snackbar snackbar = Snackbar.make(layout,message,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
