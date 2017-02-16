package com.example.root.gln;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.RatingCompat;
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
