package cecelia.lesson3hw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        String todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent,false);
        }

        TextView item_todo = (TextView) convertView.findViewById(R.id.todo_item);

        item_todo.setText(todo);

        return convertView;
    }
}
