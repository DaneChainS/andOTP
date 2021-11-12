package org.shadowice.flocke.andotp.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shadowice.flocke.andotp.R;

public class AboutFragment extends BaseAboutFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        TextView appDescription = v.findViewById(R.id.about_text_description);
        String newDescription = getString(R.string.about_description) + getString(R.string.about_description_addition);
        appDescription.setText(newDescription);


        return v;
    }
}