package com.echopen.asso.echopen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArchivesFragment extends Fragment {
    public static ArchivesFragment newInstance() {
        ArchivesFragment fragment = new ArchivesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archives, container, false);

        imagesHandler imagesHandler = new imagesHandler(getActivity().getFilesDir());

        ListView list = (ListView) view.findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                imagesHandler.listFiles());
        list.setAdapter(arrayAdapter);
        return view;
    }
}