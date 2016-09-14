package cecelia.lesson3hw;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;


import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        Button switchFragsButton = (Button) view.findViewById(R.id.to_settings);
        TextView todo1 = (TextView) view.findViewById(R.id.textView1);
        TextView todo2 = (TextView) view.findViewById(R.id.textView2);
        TextView todo3 = (TextView) view.findViewById(R.id.textView3);
        TextView todo4 = (TextView) view.findViewById(R.id.textView4);
        ArrayList<TextView> todoViews = new ArrayList<TextView>();
        todoViews.add(todo1);
        todoViews.add(todo2);
        todoViews.add(todo3);
        todoViews.add(todo4);


        switchFragsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FragmentManager fragmentManager = MainActivityFragment.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment_main = fragmentManager.findFragmentById(1);
            fragmentTransaction.remove(fragment_main);
            fragmentTransaction.add(new SettingsFragment(), "SETTINGS_FRAGMENT");

            fragmentTransaction.commit();
            }
        });

        for (TextView todo: todoViews) {

            todo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = (TextView) view;

                    AlertDialog alert = createAlertDialog(textView);
                    alert.show();
                }
            });
        }

        return view;
    }

    private AlertDialog createAlertDialog(final TextView clickedView) {
        //creating the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //setting up the layout for the alert dialog
        View layout = inflater.inflate(R.layout.todo_text_dialog, null);
        builder.setView(layout);
        final EditText todoInput = (EditText)layout.findViewById(R.id.todoInput);
        builder.setCancelable(true);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = todoInput.getText().toString();
                if (!textInput.isEmpty()) {
                    clickedView.setText(textInput);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        return alert;
    }
}
