package com.example.root.gln;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 16/2/17.
 */

public class GLNFragment extends Fragment {
    public static final String RESOURCE = "Resource ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getArguments().getInt(GLNFragment.RESOURCE), container, false);
        return rootView;
    }

}
