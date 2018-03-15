package apps.gligerglg.geotone;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gayan Lakshitha on 3/4/2018.
 */
public class Place implements Parcelable {
    private int id;
    private String title;
    private int radius;
    private double latitude;
    private double longitude;
    private int isVibrateOn;
    private int isSoundCutOn;
    private int isAirplaneModeOn;

    public Place() {
    }

    public Place(int id, String title, int radius, double latitude, double longitude, int isVibrateOn, int isSoundCutOn, int isAirplaneModeOn) {
        this.id = id;
        this.title = title;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isVibrateOn = isVibrateOn;
        this.isSoundCutOn = isSoundCutOn;
        this.isAirplaneModeOn = isAirplaneModeOn;
    }

    public Place(String title, int radius, double latitude, double longitude,int isVibrateOn, int isSoundCutOn, int isAirplaneModeOn) {
        this.title = title;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isVibrateOn = isVibrateOn;
        this.isSoundCutOn = isSoundCutOn;
        this.isAirplaneModeOn = isAirplaneModeOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int isVibrateOn() {
        return isVibrateOn;
    }

    public void setVibrateOn(int vibrateOn) {
        isVibrateOn = vibrateOn;
    }

    public int isSoundCutOn() {
        return isSoundCutOn;
    }

    public void setSoundCutOn(int soundCutOn) {
        isSoundCutOn = soundCutOn;
    }

    public int isAirplaneModeOn() {
        return isAirplaneModeOn;
    }

    public void setAirplaneModeOn(int airplaneModeOn) {
        isAirplaneModeOn = airplaneModeOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.radius);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.isVibrateOn);
        dest.writeInt(this.isSoundCutOn);
        dest.writeInt(this.isAirplaneModeOn);
    }

    protected Place(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.radius = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.isVibrateOn = in.readInt();
        this.isSoundCutOn = in.readInt();
        this.isAirplaneModeOn = in.readInt();
    }

    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
