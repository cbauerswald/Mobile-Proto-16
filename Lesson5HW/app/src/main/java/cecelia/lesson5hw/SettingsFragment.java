package cecelia.lesson5hw;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {

    @BindView(R.id.to_main_page) Button toMainPage;
    @BindView(R.id.blue_button) RadioButton blueButton;
    @BindView(R.id.white_button) RadioButton whiteButton;
    @BindView(R.id.pink_button) RadioButton pinkButton;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        //setting up buttons to change background colors
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whiteButton.setChecked(false);
                pinkButton.setChecked(false);
                ((MainActivity) getActivity()).setBackgroundColor(R.color.blue);
            }
        });

        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueButton.setChecked(false);
                pinkButton.setChecked(false);
                ((MainActivity) getActivity()).setBackgroundColor(R.color.white);
            }
        });

        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueButton.setChecked(false);
                whiteButton.setChecked(false);
                ((MainActivity) getActivity()).setBackgroundColor(R.color.rose);
            }
        });

        //setting up button to change the Fragment
        toMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // replace SettingsFragment with MainActivityFragment
                //"CURRENT_FRAGMENT" label makes it easier to grab that element some other time, should that be necessary
                fragmentTransaction.replace(R.id.fragment_holder, new MainActivityFragment(), "CURRENT_FRAGMENT");

                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
