package cecelia.lesson3hw;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cecelia on 9/15/16.
 */
public class ToDoAdapter extends ArrayAdapter<String> {

    public ToDoAdapter(Context context, ArrayList<String> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        initializeToDo(convertView, todo);
        ImageView deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);
        ImageView doneButton = initializeDoneButton(convertView);

        setOnClickListeners(doneButton, deleteButton, todo);

        return convertView;
    }

    private void setOnClickListeners(ImageView doneButton, ImageView deleteButton, final String todo) {
        //delete button removes to do from the list
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoAdapter.this.remove(todo);
            }
        });

        //done button displays a bright green check mark next to item but item stays on screen
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if item is already checked (1), uncheck (0), and vice versa.
                if ((int) view.getTag() == 0) {
                    view.setTag(1);
                    view.setBackgroundColor(Color.GREEN);
                } else if ((int) view.getTag() == 1) {
                    view.setTag(0);
                    view.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    private ImageView initializeDoneButton(View convertView) {
        ImageView doneButton = (ImageView) convertView.findViewById(R.id.done_button);
        //tag button with 0 initially, meaning "not done" (1 means done)
        doneButton.setTag(0);
        return doneButton;
    }

    private void initializeToDo(View convertView, String todo) {
        TextView itemTodo = (TextView) convertView.findViewById(R.id.todo_item);
        itemTodo.setText(todo);
    }
}
