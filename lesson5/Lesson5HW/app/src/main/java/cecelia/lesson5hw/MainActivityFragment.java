package cecelia.lesson5hw;

//remember to delete unused imports
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {
    @BindView(R.id.add_todo_button) FloatingActionButton addTodoButton;
    @BindView(R.id.to_do_list)  ListView listView;
    @BindView(R.id.settings_button) FloatingActionButton settingsButton;

    ToDoHelper toDoDbHelper;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        this.toDoDbHelper = new ToDoHelper(getActivity());

        ToDoAdapter todoAdapter = createToDoAdapter();
        ListView listView = createListView(view, todoAdapter);

        setOnClickListeners(todoAdapter);

        return view;
    }

    private void setOnClickListeners(final ToDoAdapter todoAdapter) {
        //when you click on list text, a alert dialog opens to edit the Todo w/ the arg -1 to indicate a new todo
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoAdapter.createEditToDoAlertDialog(-1);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
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
    }

    private ToDoAdapter createToDoAdapter() {
        ArrayList<ToDoItem> toDos = readItemsFromDatabase();
        final ToDoAdapter todoAdapter = new ToDoAdapter(getActivity(), toDos, this.toDoDbHelper);
        return todoAdapter;
    }

    private ListView createListView(View view, ToDoAdapter todoAdapter) {
        listView.setAdapter(todoAdapter);
        return listView;
    }

    private ArrayList<ToDoItem> readItemsFromDatabase() {
        SQLiteDatabase readDb = this.toDoDbHelper.getReadableDatabase();
        ArrayList<ToDoItem> allItems = new ArrayList<ToDoItem>();
        Cursor toDoDbCursor = readDb.query(ToDoContract.ToDoEntry.TABLE_NAME, ToDoHelper.itemProjection, null, null, null, null, null, null);
        Boolean hasItems = toDoDbCursor.moveToFirst();
        Boolean atLastElement = false;

        while (!atLastElement && hasItems) {
            addDbToDoItemtoList(allItems, toDoDbCursor);
            atLastElement = toDoDbCursor.isLast();
            toDoDbCursor.moveToNext();
        }

        return allItems;
    }

    private void addDbToDoItemtoList(ArrayList<ToDoItem> allItems, Cursor toDoDbCursor) {
        String itemDescription = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_ITEM)
        );
        Boolean itemStatus =  toDoDbCursor.getInt(
                toDoDbCursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_STATUS)
        ) > 0;
        allItems.add(new ToDoItem(itemDescription, itemStatus));
    }

}
