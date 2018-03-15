package apps.gligerglg.geotone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.PopupMenu;

import java.util.List;


/**
 * Created by Gayan Lakshitha on 3/7/2018.
 */

public class MeniItemClickListener implements PopupMenu.OnMenuItemClickListener{
    private int position;
    private Place place;
    private Task task;
    private Context context;
    private DBPlaceHelper dbPlace;
    private DBTaskHelper dbTaskHelper;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<Place> placeList;
    private List<Task> taskList;

    public MeniItemClickListener(Place place, Context context, RecyclerView.Adapter adapter, RecyclerView recyclerView, int position,List<Place> places) {
        this.place = place;
        this.context = context;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.position = position;
        this.placeList = places;
        dbPlace = new DBPlaceHelper(context);
    }

    public MeniItemClickListener(Task task, Context context, RecyclerView.Adapter adapter, RecyclerView recyclerView, int position,List<Task> tasks) {
        this.task = task;
        this.context = context;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.position = position;
        this.taskList = tasks;
        dbTaskHelper = new DBTaskHelper(context);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(place!=null)
        {
            switch (menuItem.getItemId())
            {
                case R.id.mnu_location:
                    Intent intent_loc = new Intent(context,MapActivity.class);
                    intent_loc.putExtra("id",place.getId());
                    intent_loc.putExtra("service",0);
                    context.startActivity(intent_loc);
                    return true;

                case R.id.mnu_update:
                    Intent intent_update = new Intent(context,AddNewPlace.class);
                    intent_update.putExtra("id",place.getId());
                    intent_update.putExtra("title",place.getTitle());
                    intent_update.putExtra("radius",place.getRadius());
                    intent_update.putExtra("latitude",place.getLatitude());
                    intent_update.putExtra("longitude",place.getLongitude());
                    intent_update.putExtra("airplane",place.isAirplaneModeOn());
                    intent_update.putExtra("sound",place.isSoundCutOn());
                    intent_update.putExtra("vibrate",place.isVibrateOn());
                    intent_update.putExtra("update",true);
                    context.startActivity(intent_update);
                    return true;

                case R.id.mnu_delete:
                    showNotificationOnDeletePlace();
                    return true;
            }
        }

        if(task!=null)
        {
            switch (menuItem.getItemId())
            {
                case R.id.mnu_location:
                    Intent intent_loc = new Intent(context,MapActivity.class);
                    intent_loc.putExtra("id",task.getId());
                    intent_loc.putExtra("service",1);
                    context.startActivity(intent_loc);
                    return true;

                case R.id.mnu_update:
                    Intent intent_update = new Intent(context,AddNewActivity.class);
                    intent_update.putExtra("id",task.getId());
                    intent_update.putExtra("title",task.getTitle());
                    intent_update.putExtra("description",task.getDescription());
                    intent_update.putExtra("radius",task.getRadius());
                    intent_update.putExtra("latitude",task.getLatitude());
                    intent_update.putExtra("longitude",task.getLongitude());
                    intent_update.putExtra("date",task.getDate());
                    intent_update.putExtra("time",task.getTime());
                    intent_update.putExtra("update",true);
                    context.startActivity(intent_update);
                    return true;

                case R.id.mnu_delete:
                    showNotificationOnDeleteTask();
                    return true;
            }
        }
        return false;
    }

    private void showNotificationOnDeletePlace()
    {
        AlertDialog.Builder aleart = new AlertDialog.Builder(context);
        aleart.setTitle("Warning");
        aleart.setMessage("Do you want to Delete this Place?");

        aleart.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbPlace.deletePlace(place);
                placeList.remove(position);
                recyclerView.removeViewAt(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        aleart.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        aleart.create().show();
    }

    private void showNotificationOnDeleteTask()
    {
        AlertDialog.Builder aleart = new AlertDialog.Builder(context);
        aleart.setTitle("Warning");
        aleart.setMessage("Do you want to Delete this Task?");

        aleart.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbTaskHelper.deleteTask(task);
                taskList.remove(position);
                recyclerView.removeViewAt(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        aleart.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        aleart.create().show();
    }
}
