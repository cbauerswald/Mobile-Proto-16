package cecelia.lesson5hw;

import android.provider.BaseColumns;

/**
 * Created by Cecelia on 9/19/16.
 */
public class ToDoContract {

    private ToDoContract() {
    }

    public static class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_ITEM = "todo_item";
        public static final String COLUMN_NAME_STATUS = "status";
    }
}
