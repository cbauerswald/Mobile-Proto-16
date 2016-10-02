package cecelia.lesson7hw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cecelia on 9/29/16.
 */
public class StockHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StockContract.StockEntry.TABLE_NAME + " (" +
                    StockContract.StockEntry._ID + " INTEGER PRIMARY KEY," +
                    StockContract.StockEntry.COLUMN_NAME_TICKER + TEXT_TYPE + COMMA_SEP +
                    StockContract.StockEntry.COLUMN_NAME_PRICE + TEXT_TYPE + " )";

    public static final String ID_SELECTION_QUERY = StockContract.StockEntry._ID + " Like ?";

    public static String[] itemProjection = {
            StockContract.StockEntry._ID,
            StockContract.StockEntry.COLUMN_NAME_TICKER,
            StockContract.StockEntry.COLUMN_NAME_PRICE
    };

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StockContract.StockEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Todo.db";

    public StockHelper(Context context) {
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
