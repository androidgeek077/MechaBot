package app.uos.mechabot.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.uos.mechabot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindMechanicFragment extends Fragment {


    public FindMechanicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_mechanic, container, false);
    }

}
