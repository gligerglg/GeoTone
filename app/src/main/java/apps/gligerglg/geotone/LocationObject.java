package apps.gligerglg.geotone;

/**
 * Created by Gayan Lakshitha on 3/11/2018.
 */

public class LocationObject {
    private Place place;
    private Task task;

    public LocationObject() {
    }

    public LocationObject(Place place) {
        this.place = place;
    }

    public LocationObject(Task task) {
        this.task = task;
    }

    public boolean isAPlace()
    {
        if(place!=null)
            return true;
        else
            return false;
    }

    public boolean isATask()
    {
        if(task!=null)
            return true;
        else
            return false;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
