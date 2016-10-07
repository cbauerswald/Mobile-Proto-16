package cecelia.lesson7hw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockItemAdapter extends ArrayAdapter<StockItem> {
// remember to delete unused comments and imports
//    @BindView(R.id.stock_ticker)
//    TextView stockTicker;
//    @BindView(R.id.stock_price)
//    TextView stockPrice;

    SQLiteDatabase writeDb;
    SQLiteDatabase readDb;

    public StockItemAdapter(Context context, ArrayList<StockItem> stocks, StockHelper stockHelper) {
        super(context, 0, stocks);

        this.writeDb = stockHelper.getWritableDatabase();
        this.readDb = stockHelper.getReadableDatabase();

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final StockItem stockItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.stockitem, parent, false);
        }
        ButterKnife.bind(this, convertView);

        TextView stockTicker = (TextView) convertView.findViewById(R.id.stock_ticker);
        TextView stockPrice = (TextView) convertView.findViewById(R.id.stock_price);
        Button deleteButton = (Button) convertView.findViewById(R.id.delete_button);

        stockTicker.setText(String.valueOf(stockItem.ticker));
        stockPrice.setText(String.valueOf(stockItem.price));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteToDo(stockItem, position);
            }
        });

        return convertView;
    }

    // remember to rename your methods ;-)
    private void deleteToDo(StockItem stockItem, int position) {
        StockItemAdapter.this.remove(stockItem);
        deleteItemFromDatabase(position);
    }

    private void deleteItemFromDatabase(int position) {
        //put together  query
        String selection = StockHelper.ID_SELECTION_QUERY;
        String selectionArgs[] = {getItemDbId(position)};

        writeDb.delete(StockContract.StockEntry.TABLE_NAME, selection, selectionArgs);
    }

    private String getItemDbId(int position) {
        Cursor toDoDbCursor = readDb.query(StockContract.StockEntry.TABLE_NAME, StockHelper.itemProjection, null, null, null, null, null, null);
        toDoDbCursor.moveToPosition(position);
        String id = toDoDbCursor.getString(
                toDoDbCursor.getColumnIndexOrThrow(StockContract.StockEntry._ID)
        );
        return id;
    }



}