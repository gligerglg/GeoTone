package apps.gligerglg.geotone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


public class Task_Fragment extends Fragment {

    private static final String TABLE_NAME = "easylec_task";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "desc";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String RADIUS = "radius";
    private static final String DATE = "date";
    private static final String TIME = "time";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DBTaskHelper dbTask;
    private List<Task> taskList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_task_,container,false);
        dbTask = new DBTaskHelper(this.getContext());
        taskList = new ArrayList<>();
        try {
            taskList = dbTask.getAllTasks();
        }catch (Exception ex){
            SQLiteDatabase db = getActivity().openOrCreateDatabase("easylecV2",Context.MODE_PRIVATE,null);
            String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " VARCHAR(25), " +
                    DESCRIPTION + " VARCHAR(75), " +
                    LAT + " DOUBLE, " +
                    LON + " DOUBLE, " +
                    RADIUS + " INT, " +
                    DATE + " TEXT, " +
                    TIME + " TEXT)";
            db.execSQL(sql);
            System.out.println("Database Created!");
        }
        recyclerView = view.findViewById(R.id.recycler_task);
        TaskAdapter adapter = new TaskAdapter(taskList,recyclerView);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
