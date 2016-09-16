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
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //putting textViews in an array for easier looping
        ArrayList<String> toDos = new ArrayList<String>();

        Button addTodoButton = (Button) view.findViewById(R.id.add_todo_button);

        final ToDoAdapter todoAdapter = new ToDoAdapter(getActivity(), toDos);

        final ListView listView = (ListView) view.findViewById(R.id.to_do_list);
        listView.setAdapter(todoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) adapterView.getAdapter();
                final String value = (String) adapterView.getItemAtPosition(position);
                AlertDialog alert = createEditToDoAlertDialog("edit", (ToDoAdapter)adapter, position, value);
                alert.show();
            }
        });

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alert = createEditToDoAlertDialog("create", todoAdapter, -1, "");
                alert.show();
            }
        });
        return view;
    }

    private AlertDialog createEditToDoAlertDialog(final String action, final ToDoAdapter adapter, final int position, final String value) {
        //used code from:
        // http://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        // http://stackoverflow.com/questions/13584063/adding-edittext-to-alert-dialog

        //creating the alert dialog and setting the custom view to it
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.todo_text_dialog, null);
        builder.setView(layout);
        builder.setCancelable(true);

        //grabbing the input as final so that we can change from the listener
        final EditText todoInput = (EditText) layout.findViewById(R.id.todoInput);
        if (!value.isEmpty()) {
            todoInput.setText(value);
        }

        //setting the positive button to record the new to do unless no new to do was entered
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = todoInput.getText().toString();
                if (action == "edit") {
                    editToDo(textInput, adapter, value, position);
                } else if (action == "create") {
                    createToDo(textInput, adapter);
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

    public void editToDo(String textInput, ToDoAdapter adapter, String value, int position) {
        adapter.remove(value);
        if (!textInput.isEmpty()) {
            //remove the original item and insert the edited item in the same position
            adapter.insert(textInput, position);
        }
    }

    public void createToDo(String textInput, ToDoAdapter adapter) {
        if (!textInput.isEmpty()) {
            //remove the original item and insert the edited item in the same position
            adapter.add(textInput);
        }
    }

}
