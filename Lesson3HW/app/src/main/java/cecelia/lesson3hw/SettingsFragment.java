package cecelia.lesson3hw;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //grabbing buttons
        Button switchFragsButton = (Button) view.findViewById(R.id.to_main_page);

        final Button blueButton = (Button) view.findViewById(R.id.blue_button); //final so that it can be accessed in onClickListener, same below
        final Button greenButton = (Button) view.findViewById(R.id.green_button);
        final Button roseButton = (Button) view.findViewById(R.id.rose_button);

//        the way you map colors to buttons is pretty cool! a maybe simpler way of doing this is
//        using the Color library (import android.graphics.Color; Color.red;), but I like how you
//        used values from the color.xml
        //connecting buttons to their color through a map
        final Map<Button, Integer> buttonsToColors= new HashMap<Button, Integer>();
        buttonsToColors.put(blueButton, R.color.blue);
        buttonsToColors.put(greenButton, R.color.green);
        buttonsToColors.put(roseButton, R.color.rose);

        //setting up buttons to change background colors
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(blueButton, buttonsToColors);
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(greenButton, buttonsToColors);
            }
        });

        roseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(roseButton, buttonsToColors);
            }
        });

        //setting up button to change the Fragment
        switchFragsButton.setOnClickListener(new View.OnClickListener() {
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

    private void setBackgroundColor(Button button, Map<Button, Integer> buttonsToColors) {
        int colorId = buttonsToColors.get(button);
        int color = ContextCompat.getColor(getActivity(), colorId);
//        another way to do this is to do use findViewByID and change the background of the coordinator
//        layout in activity_main.xml
        View view = getActivity().getWindow().getDecorView(); //changes the background of the activity, so that it stays for all fragments
        view.setBackgroundColor(color);
    }
}
