package com.echopen.asso.echopen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.echopen.asso.echopen.model.Seance;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeancesFragment extends Fragment {

    public final static String SEANCE_KEY = "Seance";
    Seance currentSeance;

    public SeancesFragment() {
        // Required empty public constructor
    }

    public static SeancesFragment newInstance(Seance seance){
        SeancesFragment sf = new SeancesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SEANCE_KEY,seance);

        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.seances, container, false);

        TextView nameView = (TextView) v.findViewById(R.id.name_D);
        TextView descView = (TextView) v.findViewById(R.id.desc_D);

        if (getArguments()!= null){
            Bundle bundle = getArguments();
            currentSeance = bundle.getParcelable(SEANCE_KEY);
        }

        if (currentSeance != null) {
            nameView.setText(currentSeance.getName());
            descView.setText(currentSeance.getDescription());
        }

        return v;
    }
}
