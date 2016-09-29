package cecelia.lesson5hw;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.view.WindowManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    @Nullable @BindView(R.id.delete_button) ImageView deleteButton;
    @Nullable @BindView(R.id.done_box) CheckBox doneBox;
    @Nullable @BindView(R.id.todo_item) TextView itemTodo;


    SQLiteDatabase writeDb;
    SQLiteDatabase readDb;

    public ToDoAdapter(Context context, ArrayList<ToDoItem> todos, ToDoHelper toDoHelper) {
        super(context, 0, todos);
        this.writeDb = toDoHelper.getWritableDatabase();
        this.readDb = toDoHelper.getReadableDatabase();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ToDoItem todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        ButterKnife.bind(this, convertView);
        initializeListViewEntry(todo);
        setOnClickListeners(todo, position);

        return convertView;
    }

    private void setOnClickListeners(final ToDoItem todo, final int position) {
        itemTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String value = (String)((TextView) view).getText();
                int position = getItemPosition(view);
                createEditToDoAlertDialog(position);

            }
        });

        //delete button removes to do from the list
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteToDo(todo, position);
            }
        });

        //done button displays a bright green check mark next to item but item stays on screen
        doneBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if item is already checked (1), uncheck (0), and vice versa.
                todo.completed = !todo.completed;
                editStatusInDatabase(position, todo.completed);
            }
        });
    }

    public void createEditToDoAlertDialog(int position) {
        //used code from:
        // http://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
        // http://stackoverflow.com/questions/13584063/adding-edittext-to-alert-dialog

        //creating the alert dialog builder
        AlertDialog.Builder builder = createBuilder();

        //setting custom view to alert dialog
        View layout = setBuilderCustomLayout(builder);
        EditText todoInput = (EditText) layout.findViewById(R.id.todo_input);

        ToDoItem toDoItem;
        if (position > -1) {
            //if the todo item already exists
            toDoItem = this.getItem(position);
            todoInput.setText(toDoItem.description);
            todoInput.setSelection(todoInput.getText().length());
        } else {
            //if a new todo items needs to be created
            toDoItem = new ToDoItem("", false);
        }

        setDialogPositiveNegativeButtons(builder, position, todoInput, toDoItem);

        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alert.show();
    }

    private AlertDialog.Builder createBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        return builder;
    }

    private View setBuilderCustomLayout(AlertDialog.Builder builder) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.todo_text_dialog, null);
        builder.setView(layout);
        return layout;
    }

    private void setDialogPositiveNegativeButtons(AlertDialog.Builder builder, final int position, final EditText todoInput, final ToDoItem value) {
        //setting the positive button to record the new to do unless no new to do was entered
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = todoInput.getText().toString();
                if (position >= 0) {
                    editToDo(textInput, position, value);
                } else {
                    createToDo(textInput, value);
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
    }

    private void editToDo(String textInput, int position, ToDoItem oldToDoItem) {
        this.remove(oldToDoItem);
        if (!textInput.isEmpty()) {
            //remove the original item and insert the edited item in the same position
            this.insert(new ToDoItem(textInput, oldToDoItem.completed), position);
            editDescriptionInDatabase(position, textInput);
        }
    }

    private void createToDo(String textInput, ToDoItem newToDo) {
        if (!textInput.isEmpty()) {
            //remove the original item and insert the edited item in the same position;
            newToDo.description = textInput;
            this.add(newToDo);
            addItemToDatabase(newToDo);
        }
    }

    private void deleteToDo(ToDoItem todo, int position) {
        ToDoAdapter.this.remove(todo);
        deleteItemFromDatabase(position);
    }

    private void initializeListViewEntry(ToDoItem todo) {
        itemTodo.setText(todo.description);
        doneBox.setChecked(todo.completed);
    }

    private int getItemPosition(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        return listView.getPositionForView(parentRow);
    }

    private void addItemToDatabase(ToDoItem item) {
        ContentValues values = new ContentValues();
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_ITEM, item.description);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_STATUS, item.completed);
        this.writeDb.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, values);
    }

    private void editDescriptionInDatabase(int position, String newDescription) {
        ContentValues values = new ContentValues();
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_ITEM, newDescription);

        String selection = ToDoHelper.ID_SELECTION_QUERY;
        String[] selectionArgs = {getItemDbId(position)};

        readDb.update(ToDoContract.ToDoEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private void editStatusInDatabase(int position, Boolean newStatus) {
        ContentValues values = new ContentValues();
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_STATUS, newStatus);

        String selection = ToDoHelper.ID_SELECTION_QUERY;
        String[] selectionArgs = {getItemDbId(position)};

        readDb.update(ToDoContract.ToDoEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private void deleteItemFromDatabase(int position) {
        //put together  query
        String selection = ToDoHelper.ID_SELECTION_QUERY;
        String selectionArgs[] = {getItemDbId(position)};

        writeDb.delete(ToDoContract.ToDoEntry.TABLE_NAME, selection, selectionArgs);
    }

    private String getItemDbId(int position) {
        Cursor toDoDbCursor = readDb.query(ToDoContract.ToDoEntry.TABLE_NAME, ToDoHelper.itemProjection, null, null, null, null, null, null);
        toDoDbCursor.moveToPosition(position);
        String id = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry._ID)
        );
        return id;
    }
}
