package apps.gligerglg.geotone;

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

public class Places_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DBPlaceHelper dbPlace;
    private List<Place> placeList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_places_,container,false);
       dbPlace = new DBPlaceHelper(this.getContext());
       placeList = new ArrayList<>();
       placeList = dbPlace.getAllPlaces();
       recyclerView = view.findViewById(R.id.recycler_place);
       PlaceAdapter adapter = new PlaceAdapter(placeList,recyclerView);
       linearLayoutManager = new LinearLayoutManager(this.getActivity());
       recyclerView.setLayoutManager(linearLayoutManager);
       recyclerView.setAdapter(adapter);
       return view;
    }
}
