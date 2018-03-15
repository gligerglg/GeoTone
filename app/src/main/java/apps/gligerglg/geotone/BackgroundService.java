package apps.gligerglg.geotone;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import plugins.gligerglg.locusservice.LocusService;


/**
 * Created by Gayan Lakshitha on 3/11/2018.
 */

public class BackgroundService extends Service {

    private ArrayList<Place> placeList;
    private ArrayList<Task> taskList;
    private ArrayList<LocationObject> locationObjects;
    private LocusService locusService;
    private LocationObject nearestObject;
    private Location nearest_Location;
    private float Ori_distance = 0;
    private boolean isPlaceNotified = false, isTaskNotified = false;
    private AudioManager audioManager;
    private int musicVol=0, alarmVol=0, notiVol = 0;
    private boolean isVibrateOn = false, isSoundCutOn = false, isAirplaneOn = false;
    private DBTaskHelper dbTaskHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        placeList = intent.getParcelableArrayListExtra("placeList");
        taskList = intent.getParcelableArrayListExtra("taskList");
        locationObjects = convertToLocationObjects(placeList,taskList);
        locusService = new LocusService(getApplicationContext(),false);
        locusService.startRealtimeGPSListening(10000);
        nearest_Location = new Location("nearest_Location");
        dbTaskHelper = new DBTaskHelper(getApplicationContext());

        locusService.setRealTimeLocationListener(new LocusService.RealtimeListenerService() {
            @Override
            public void OnRealLocationChanged(Location location) {

                if(location!=null) {
                    //Get the nearest location
                    nearestObject = getNearestLocation(location, locationObjects);

                    if (nearestObject != null) {
                        //Check nearest location distance and set output respond
                        if (nearestObject.isAPlace()) {
                            nearest_Location.setLatitude(nearestObject.getPlace().getLatitude());
                            nearest_Location.setLongitude(nearestObject.getPlace().getLongitude());
                            Ori_distance = location.distanceTo(nearest_Location);

                            if(nearestObject.getPlace().isAirplaneModeOn()==1) isAirplaneOn=true; else isAirplaneOn=false;
                            if(nearestObject.getPlace().isSoundCutOn()==1) isSoundCutOn=true; else isSoundCutOn=false;
                            if(nearestObject.getPlace().isVibrateOn()==1) isVibrateOn=true; else isVibrateOn=false;

                            if (Ori_distance < nearestObject.getPlace().getRadius()) {
                                //Do entering operations
                                if (!isPlaceNotified) {
                                    setPlaceService(isVibrateOn,isSoundCutOn,isAirplaneOn);
                                    setNotification("EasyLec","You are in " +
                                            nearestObject.getPlace().getTitle() + "\nConfiguration successfully applied!",100);
                                    isPlaceNotified = true;
                                }

                            }
                            else {
                                //Do leaving operation
                                if(isPlaceNotified)
                                {
                                    NotificationManagerCompat.from(getApplicationContext()).cancel(100);
                                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,notiVol,0);
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,alarmVol,0);
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,musicVol,0);
                                    isPlaceNotified = false;
                                }
                            }
                        } else if (nearestObject.isATask()) {
                            nearest_Location.setLatitude(nearestObject.getTask().getLatitude());
                            nearest_Location.setLongitude(nearestObject.getTask().getLongitude());
                            Ori_distance = location.distanceTo(nearest_Location);

                            if (Ori_distance < nearestObject.getTask().getRadius()) {
                                //Do entering operations
                                if (!isTaskNotified) {
                                    checkAndExecuteTask(nearestObject.getTask(),nearestObject);
                                }
                            }
                            else {
                                //Do leaving operation
                                if(isTaskNotified)
                                {
                                    isTaskNotified = false;
                                }
                            }
                        }
                    }
                    else {
                        locusService.stopRealTimeGPSListening();
                        SharedPreferences sharedPref= getSharedPreferences("easylec_service_status", 0);
                        SharedPreferences.Editor editor= sharedPref.edit();
                        editor.putBoolean("status",false);
                        stopSelf();
                    }
                }
            }
        });

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locusService.stopRealTimeGPSListening();
    }

    private ArrayList<LocationObject> convertToLocationObjects(ArrayList<Place> placeList, ArrayList<Task> taskList)
    {
        ArrayList<LocationObject> locationObjects = new ArrayList<>();

        for(Place place : placeList)
            locationObjects.add(new LocationObject(place));

        for(Task task : taskList)
            locationObjects.add(new LocationObject(task));

        return  locationObjects;
    }

    private LocationObject getNearestLocation(Location myLocation, ArrayList<LocationObject> locationObjects)
    {
        LocationObject minObject = null;
        if(!locationObjects.isEmpty())
        {
            float distance = 0, minDistance = 0;
            minObject = locationObjects.get(0);

            //Initialize Minimum Distance Location
            Location minLoc = new Location("minLoc");
            if(locationObjects.get(0).isAPlace()) {
                minLoc.setLatitude(locationObjects.get(0).getPlace().getLatitude());
                minLoc.setLatitude(locationObjects.get(0).getPlace().getLongitude());
            }
            else if(locationObjects.get(0).isATask()){
                minLoc.setLatitude(locationObjects.get(0).getTask().getLatitude());
                minLoc.setLatitude(locationObjects.get(0).getTask().getLongitude());
            }

            minDistance = myLocation.distanceTo(minLoc);

            //Find Minimum distance location
            for(LocationObject object : locationObjects)
            {
                if(object.isAPlace()) {
                    minLoc.setLatitude(object.getPlace().getLatitude());
                    minLoc.setLatitude(object.getPlace().getLongitude());
                }
                else if(object.isATask()){
                    minLoc.setLatitude(object.getTask().getLatitude());
                    minLoc.setLatitude(object.getTask().getLongitude());
                }

                distance = myLocation.distanceTo(minLoc);

                if(distance<minDistance)
                    minObject = object;
            }
        }

        return minObject;
    }

    private void checkAndExecuteTask(Task task, LocationObject object)
    {
        Calendar taskcalendar = Calendar.getInstance();
        Calendar nowcalender = Calendar.getInstance();

        String DateTimeStr = task.getDate() + " " + task.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
        try {
            Date taskDateTime = dateFormat.parse(DateTimeStr);
            taskcalendar.setTime(taskDateTime);
            if(nowcalender.after(taskcalendar))
            {
                setNotification("GeoTone : " + task.getTitle(),task.getDescription(),200);
                dbTaskHelper.deleteTask(task);
                locationObjects.remove(object);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setNotification(String title, String message,  int id)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(BackgroundService.this,MainMenu.class),0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"ID");
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo(message)
                .addAction(new NotificationCompat.Action(R.drawable.ic_home_black_24dp,"GeoTone",pendingIntent))
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat.from(this).notify(id,notification);
    }

    private void setPlaceService(boolean isVibrateOn, boolean isSoundCutOn, boolean isAirplaneOn)
    {
        musicVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        alarmVol = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        notiVol = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

        if(isAirplaneOn) {
            //Airplane On changed to Music
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        }

        if(isSoundCutOn) {
            //Music and Alearm Cut On
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
        }

        if(isVibrateOn) {
            //Vibrate and Notification sound off
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }

    }
}