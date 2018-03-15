package apps.gligerglg.geotone;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class AddNewActivity extends AppCompatActivity implements TimePickerFragment.TimeDialogListener,DatePickerFragment.DateDialogListener {

    private Bundle intent_info;
    private TextInputEditText txt_title, txt_description;
    private SeekBar seekBar;
    private TextView txt_radius, txt_Date, txt_Time;
    private ImageButton btn_pickDate, btn_pickTime;
    private AppCompatButton btn_setTask;
    private String timeObj;
    private String dateObj;
    private boolean isTimeSet = false, isDateSet = false, isComplete = false, isUpdate;
    private DBTaskHelper dbTask;
    private ConstraintLayout layout;
    private Double myLatitude,myLongitude;

    private int id, radius;
    private String title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

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

        btn_pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment time = new TimePickerFragment();
                time.show(getFragmentManager(),"timePicker");
            }
        });

        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment date = new DatePickerFragment();
                date.show(getFragmentManager(),"datePicker");
            }
        });

        btn_setTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txt_title.getText().toString()))
                    txt_title.setError("Title cannot be Empty!");
                if(TextUtils.isEmpty(txt_description.getText().toString()))
                    txt_description.setError("Description cannot be empty!");
                if(!TextUtils.isEmpty(txt_title.getText().toString()) && !TextUtils.isEmpty(txt_description.getText().toString()))
                {
                    if(isComplete)
                    {
                        startActivity(new Intent(AddNewActivity.this,MainMenu.class));
                    }
                    else
                    {
                        if(!isDateSet && !isTimeSet)
                            setMessage("You need to set up Date and Time!");
                        else {
                            if (!isDateSet)
                                setMessage("Please set a Date!");
                            if (!isTimeSet)
                                setMessage("Please set a Time");
                            if (isTimeSet && isDateSet) {
                                Task task = new Task(txt_title.getText().toString(),
                                        txt_description.getText().toString(),
                                        seekBar.getProgress(), dateObj, timeObj,myLatitude,myLongitude);

                                if(isUpdate)
                                {
                                    task.setId(id);
                                    dbTask.updateTask(task);
                                    setMessage("Your Task Updated Successfully!");
                                }
                                else
                                {
                                    dbTask.addTask(task);
                                    setMessage("New Task Added Successfully!");
                                }

                                isComplete = true;
                                btn_setTask.setText("Goto Main Menu");

                            }
                        }
                    }
                }
            }
        });
    }

    private void Init()
    {
        intent_info = getIntent().getExtras();

        isUpdate = intent_info.getBoolean("update",false);
        id = intent_info.getInt("id");
        title = intent_info.getString("title","");
        description = intent_info.getString("description","");
        myLatitude = intent_info.getDouble("latitude");
        myLongitude = intent_info.getDouble("longitude");
        radius = intent_info.getInt("radius",200);
        dateObj = intent_info.getString("date","");
        timeObj = intent_info.getString("time","");

        txt_title = findViewById(R.id.txt_task_title);
        txt_description = findViewById(R.id.txt_task_description);
        seekBar = findViewById(R.id.seek_radius_task);
        txt_radius = findViewById(R.id.txt_task_radius);
        txt_Date = findViewById(R.id.txt_task_date);
        txt_Time = findViewById(R.id.txt_task_time);
        btn_pickDate = findViewById(R.id.btn_datePick);
        btn_pickTime = findViewById(R.id.btn_timePick);
        btn_setTask = findViewById(R.id.btn_setTask);

        dbTask = new DBTaskHelper(this);
        layout = findViewById(R.id.layout_add_task);

        txt_title.setText(title);
        txt_description.setText(description);
        seekBar.setProgress(radius);
        txt_radius.setText(radius + "m");

        if(TextUtils.isEmpty(dateObj))
            btn_pickDate.setImageResource(R.drawable.ic_date_off);
        else {
            btn_pickDate.setImageResource(R.drawable.ic_date_on);
            isDateSet = true;
        }

        if(TextUtils.isEmpty(timeObj))
            btn_pickTime.setImageResource(R.drawable.ic_time_off);
        else {
            btn_pickTime.setImageResource(R.drawable.ic_time_on);
            isTimeSet = true;
        }

        if(isUpdate)
            btn_setTask.setText("Update Task");
    }

    @Override
    public void onFinishDialog(String time) {
        timeObj = time;
        if(!TextUtils.isEmpty(timeObj))
        {
            txt_Time.setText(timeObj);
            btn_pickTime.setImageResource(R.drawable.ic_time_on);
            isTimeSet = true;
        }

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

    @Override
    public void onDateChange(String date) {
        dateObj = date;
        if(!TextUtils.isEmpty(dateObj))
        {
            txt_Date.setText(dateObj);
            btn_pickDate.setImageResource(R.drawable.ic_date_on);

            isDateSet = true;
        }
    }

}
