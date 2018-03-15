package apps.gligerglg.geotone;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gayan Lakshitha on 3/6/2018.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.CardHolder> {

    private List<Place> placeList;
    private RecyclerView recyclerView;
    public class CardHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView img_vibrate,img_sound,img_airplane;
        ImageButton btn_more;
        TextView title;

        CardHolder(View itemView)
        {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_place);
            title = itemView.findViewById(R.id.txt_cv_place_title);
            img_vibrate = itemView.findViewById(R.id.img_cv_place_vibrate);
            img_sound = itemView.findViewById(R.id.img_cv_place_sound);
            img_airplane = itemView.findViewById(R.id.img_cv_place_airplane);
            btn_more = itemView.findViewById(R.id.btn_cv_place_more);
        }
    }

    public PlaceAdapter(List<Place> placeList, RecyclerView recyclerView) {
        this.placeList = placeList;
        this.recyclerView = recyclerView;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_place,parent,false);
        CardHolder ch = new CardHolder(v);
        return  ch;
    }

    @Override
    public void onBindViewHolder(final PlaceAdapter.CardHolder holder, final int position) {
        holder.title.setText(placeList.get(position).getTitle());

        if(placeList.get(position).isAirplaneModeOn()==1)
            holder.img_airplane.setImageResource(R.drawable.ic_airplane_on);
        else
            holder.img_airplane.setImageResource(R.drawable.ic_airplane_off);

        if(placeList.get(position).isSoundCutOn()==1)
            holder.img_sound.setImageResource(R.drawable.ic_sound_on);
        else
            holder.img_sound.setImageResource(R.drawable.ic_sound_off);

        if(placeList.get(position).isVibrateOn()==1)
            holder.img_vibrate.setImageResource(R.drawable.ic_vibration_on);
        else
            holder.img_vibrate.setImageResource(R.drawable.ic_vibration_off);

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(holder.btn_more,position,placeList.get(position));
            }
        });

    }

    private void showPopUpMenu(View view, int position, Place place)
    {
        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.cv_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MeniItemClickListener(place,view.getContext(),this,recyclerView,position,placeList));
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
