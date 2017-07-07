package com.echopen.asso.echopen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echopen.asso.echopen.model.Seance;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeanceListFragment extends Fragment implements SeanceListFragmentAdapter.SeanceListItemClicked{
    private ArrayList<Seance> mSeanceList = new ArrayList<>();
    private ViewGroup mFragmentContainer;

    public SeanceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mSeanceList.size() == 0 ){
            mSeanceList.add(new Seance("Coeur 04/07/2017","description"));
            mSeanceList.add(new Seance("Coeur 05/07/2017","description"));
            mSeanceList.add(new Seance("Coeur 06/07/2017","description"));
        }else{
            mSeanceList.add(new Seance("Coeur 07/07/2017","description"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentContainer = container;

        View view = inflater.inflate(R.layout.seance_list, container, false);
        RecyclerView List = (RecyclerView) view.findViewById(R.id.list_seance);

        // use a linear layout manager
        LinearLayoutManager LLManager = new LinearLayoutManager(getActivity());
        List.setLayoutManager(LLManager);

        SeanceListFragmentAdapter ListFragment = new SeanceListFragmentAdapter(mSeanceList, this);
        List.setAdapter(ListFragment);
        if (view.findViewById(R.id.seances)!= null){
            view.findViewById(R.id.seances).setOnClickListener((MainActivity) getActivity());
        }
        if (view.findViewById(R.id.profil)!= null){
            view.findViewById(R.id.profil).setOnClickListener((MainActivity) getActivity());
        }
        if (view.findViewById(R.id.faq)!= null){
            view.findViewById(R.id.faq).setOnClickListener((MainActivity) getActivity());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSeanceClicked(int position) {
        SeancesFragment sf = SeancesFragment.newInstance(mSeanceList.get(position));

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, sf).addToBackStack(null).commit();
    }

}