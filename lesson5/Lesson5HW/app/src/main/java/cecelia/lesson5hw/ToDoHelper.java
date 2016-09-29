package cecelia.lesson5hw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cecelia on 9/19/16.
 */
public class ToDoHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ToDoContract.ToDoEntry.TABLE_NAME + " (" +
                    ToDoContract.ToDoEntry._ID + " INTEGER PRIMARY KEY," +
                    ToDoContract.ToDoEntry.COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                    ToDoContract.ToDoEntry.COLUMN_NAME_STATUS + INT_TYPE + " )";

    public static final String ID_SELECTION_QUERY = ToDoContract.ToDoEntry._ID + " Like ?";

    public static String[] itemProjection = {
            ToDoContract.ToDoEntry._ID,
            ToDoContract.ToDoEntry.COLUMN_NAME_ITEM,
            ToDoContract.ToDoEntry.COLUMN_NAME_STATUS
    };

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ToDoContract.ToDoEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Todo.db";

    public ToDoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
