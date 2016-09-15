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
import android.widget.TextView;
import android.widget.EditText;


import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        //grabbing views
        Button switchFragsButton = (Button) view.findViewById(R.id.to_settings);
        TextView todo1 = (TextView) view.findViewById(R.id.textView1);
        TextView todo2 = (TextView) view.findViewById(R.id.textView2);
        TextView todo3 = (TextView) view.findViewById(R.id.textView3);
        TextView todo4 = (TextView) view.findViewById(R.id.textView4);
        TextView todo5 = (TextView) view.findViewById(R.id.textView5);

        //putting textViews in an array for easier looping
        ArrayList<TextView> todoViews = new ArrayList<TextView>();
        todoViews.add(todo1);
        todoViews.add(todo2);
        todoViews.add(todo3);
        todoViews.add(todo4);
        todoViews.add(todo5);

        //setting up button to switch fragment
        switchFragsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FragmentManager fragmentManager = MainActivityFragment.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // replace SettingsFragment with MainActivityFragment
            //"CURRENT_FRAGMENT" label makes it easier to grab that element some other time, should that be necessary
            fragmentTransaction.replace(R.id.fragment_holder, new SettingsFragment(), "CURRENT_FRAGMENT");

            fragmentTransaction.commit();
            }
        });

        //setting up textViews to open alert dialog on click
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
        //used code from:
        // http://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        // http://stackoverflow.com/questions/13584063/adding-edittext-to-alert-dialog

        //creating the alert dialog and setting the custom view to it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.todo_text_dialog, null);
        builder.setView(layout);

        //grabbing the input as final so that we can change from the listener
        final EditText todoInput = (EditText)layout.findViewById(R.id.todoInput);

        builder.setCancelable(true);

        //setting the positive button to record the new to do unless no new to do was entered
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = todoInput.getText().toString();
                if (!textInput.isEmpty()) {
                    clickedView.setText(textInput);
                }
            }
        });
        //setting the negative button to return to the main page without making any edits
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
