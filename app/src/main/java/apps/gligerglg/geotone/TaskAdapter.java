package apps.gligerglg.geotone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/7/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.CardHolder> {

    private List<Task> taskList;
    private RecyclerView recyclerView;

    public TaskAdapter(List<Task> taskList, RecyclerView recyclerView) {
        this.taskList = taskList;
        this.recyclerView = recyclerView;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_task,parent,false);
        TaskAdapter.CardHolder ch = new TaskAdapter.CardHolder(v);
        return  ch;
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        holder.title.setText(taskList.get(position).getTitle());
        holder.txt_time.setText("Time :\t" + taskList.get(position).getTime());
        holder.txt_date.setText("Date :\t" + taskList.get(position).getDate());
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(holder.btn_more,taskList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void showPopUpMenu(View view, Task task, int position)
    {
        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.cv_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MeniItemClickListener(task,view.getContext(),this,recyclerView,position,taskList));
        popupMenu.show();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageButton btn_more;
        TextView title,txt_date,txt_time;
        public CardHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_task);
            title = itemView.findViewById(R.id.txt_cv_task_title);
            txt_date = itemView.findViewById(R.id.txt_cv_task_date);
            txt_time = itemView.findViewById(R.id.txt_cv_task_time);
            btn_more = itemView.findViewById(R.id.btn_cv_task_more);
        }
    }
}
