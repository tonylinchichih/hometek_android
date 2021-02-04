package com.intellex.hometek.ui;

import androidx.fragment.app.Fragment;

/** A simple {@link Fragment} subclass. */
public abstract class MyBaseFragment extends Fragment {

    public MyBaseFragment() {
        // Required empty public constructor
    }

    public abstract void update(boolean skip);
}
