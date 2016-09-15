package cecelia.lesson3hw;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

        Button switchFragsButton = (Button) view.findViewById(R.id.to_main_page);

        final Button blueButton = (Button) view.findViewById(R.id.blue_button);
        final Button greenButton = (Button) view.findViewById(R.id.green_button);
        final Button roseButton = (Button) view.findViewById(R.id.rose_button);

        final Map<Button, Integer> buttonsToColors= new HashMap<Button, Integer>();
        buttonsToColors.put(blueButton, R.color.blue);
        buttonsToColors.put(greenButton, R.color.green);
        buttonsToColors.put(roseButton, R.color.rose);

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(view, buttonsToColors.get(blueButton));
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(view, buttonsToColors.get(greenButton));
            }
        });

        roseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(view, buttonsToColors.get(roseButton));
            }
        });

        switchFragsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_holder, new MainActivityFragment(), "CURRENT_FRAGMENT");

                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void setBackgroundColor(View v, int colorId) {
        int color = ContextCompat.getColor(getActivity(), colorId);
        View view = getActivity().getWindow().getDecorView();
        view.setBackgroundColor(color);

    }
}
