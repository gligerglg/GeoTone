package apps.gligerglg.geotone;


import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AddNewPlace extends AppCompatActivity {

    private Bundle intent_info;
    private TextView txt_myLat, txt_myLon, txt_radius, txt_title;
    private ToggleButton btn_sound, btn_vibrate, btn_airplane;
    private SeekBar seekBar;
    private AppCompatButton btn_setPlace;
    private DBPlaceHelper dbPlace;
    private ConstraintLayout layout;
    private boolean isComplete = false,isUpdate;

    private int id;
    private String title="";
    private int radius;
    private Double myLatitude,myLongitude;
    private int isAirplaneOn,isVibrateOn,isSoundCutOn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Initialize Components
        Init();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txt_radius.setText("" + seekBar.getProgress() + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_setPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isComplete)
                {
                    startActivity(new Intent(AddNewPlace.this,MainMenu.class));
                }
                else
                {
                    if(TextUtils.isEmpty(txt_title.getText().toString()))
                        txt_title.setError("Place title cannot be empty!");
                    else
                    {
                        if(btn_airplane.isChecked())isAirplaneOn=1;else isAirplaneOn=0;
                        if(btn_sound.isChecked())isSoundCutOn=1;else isSoundCutOn=0;
                        if(btn_vibrate.isChecked())isVibrateOn=1;else isVibrateOn=0;

                        Place place = new Place(txt_title.getText().toString(),
                                seekBar.getProgress(),
                                myLatitude,myLongitude,
                                isVibrateOn,
                                isSoundCutOn,
                                isAirplaneOn);

                        if(isUpdate)
                        {
                            place.setId(id);
                            dbPlace.updatePlace(place);
                            setMessage("Your Place Updated Successfully!");
                        }
                        else {
                            dbPlace.addPlace(place);
                            setMessage("New Place Added Successfully!");
                        }

                        isComplete = true;
                        btn_setPlace.setText("Goto Main Menu");

                    }
                }
            }
        });

    }


    private void Init()
    {
        intent_info = getIntent().getExtras();

        id = intent_info.getInt("id");
        title = intent_info.getString("title","");
        myLatitude = intent_info.getDouble("latitude");
        myLongitude = intent_info.getDouble("longitude");
        radius = intent_info.getInt("radius",200);
        isAirplaneOn = intent_info.getInt("airplane",0);
        isVibrateOn = intent_info.getInt("vibrate",0);
        isSoundCutOn = intent_info.getInt("sound",0);
        isUpdate = intent_info.getBoolean("update",false);


        txt_myLat = findViewById(R.id.txt_place_latitude);
        txt_myLon = findViewById(R.id.txt_place_longitude);
        txt_radius = findViewById(R.id.txt_place_radius);
        txt_title = findViewById(R.id.txt_place_title);

        btn_airplane = findViewById(R.id.btn_airplane);
        btn_sound = findViewById(R.id.btn_sound);
        btn_vibrate = findViewById(R.id.btn_vibrate);
        btn_setPlace = findViewById(R.id.btn_setPlace);

        dbPlace = new DBPlaceHelper(this);
        layout = findViewById(R.id.layout_add_place);

        seekBar = findViewById(R.id.seek_radius);
        txt_title.setText(title);
        txt_myLat.setText("Latitude\t\t\t" + myLatitude);
        txt_myLon.setText("Longitude\t\t" + myLongitude);
        txt_radius.setText(""+radius + "m");
        seekBar.setProgress(radius);

        if(isSoundCutOn==1)
            btn_sound.setChecked(true);
        else
            btn_sound.setChecked(false);

        if(isVibrateOn==1)
            btn_vibrate.setChecked(true);
        else
            btn_vibrate.setChecked(false);

        if(isAirplaneOn==1)
            btn_airplane.setChecked(true);
        else
            btn_airplane.setChecked(false);

        if(isUpdate)
            btn_setPlace.setText("Update Place");
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
